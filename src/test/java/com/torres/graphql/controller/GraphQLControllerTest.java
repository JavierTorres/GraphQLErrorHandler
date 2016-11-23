package com.torres.graphql.controller;

import com.torres.graphql.fetcher.HelloWorldFetcher;
import com.torres.graphql.model.GraphQLServerRequest;
import com.torres.graphql.query.HelloWorldQuery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by javierbracerotorres on 15/11/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class GraphQLControllerTest {

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private MediaType contentType = MediaType.APPLICATION_JSON;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private HelloWorldQuery helloWorldQuery;

    @Autowired
    private HelloWorldFetcher helloWorldFetcher;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Test
    public void testGraphQLOk() throws Exception {
        String request = json(new GraphQLServerRequest("{ hello }"));
        helloWorldFetcher.setConnectionError(false);

        this.mockMvc.perform(post("/graphql")
                .contentType(contentType)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"data\":{\"hello\":\"World\"}}"));
    }

    @Test
    public void testGraphQLWithFetchingErrors() throws Exception {
        String request = json(new GraphQLServerRequest("{ hello }"));
        helloWorldFetcher.setConnectionError(true);

        this.mockMvc.perform(post("/graphql")
                .contentType(contentType)
                .content(request))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"data\":{\"hello\":null}," +
                        "\"errors\":[{\"code\":\"301\",\"message\":\"No connection\"}]}"));
    }

    @Test
    public void testGraphQLWithInvalidBody() throws Exception {
        String request = json(new GraphQLServerRequest("{ INVALID BODY }"));

        this.mockMvc.perform(post("/graphql")
                .contentType(contentType)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"data\":null," +
                        "\"errors\":[" +
                        "{\"message\":\"Validation error of type FieldUndefined: Field INVALID is undefined\"}," +
                        "{\"message\":\"Validation error of type FieldUndefined: Field BODY is undefined\"}]}"));
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
