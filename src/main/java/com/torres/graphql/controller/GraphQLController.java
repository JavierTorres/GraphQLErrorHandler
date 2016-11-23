package com.torres.graphql.controller;

import com.torres.graphql.http.GraphQLHttpResponse;
import com.torres.graphql.schema.HelloWorldSchema;
import com.torres.graphql.schema.SchemaExecutor;
import graphql.ExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@EnableAutoConfiguration
public class GraphQLController {

    @Autowired
    HelloWorldSchema helloWorldSchema;

    @Autowired
    SchemaExecutor schemaExecutor;

    private static final Logger log = LoggerFactory.getLogger(GraphQLController.class);

    /*
    @Autowired
    public GraphQLController(HelloWorldSchema helloWorldSchema, SchemaExecutor schemaExecutor) {
        this.helloWorldSchema = helloWorldSchema;
        this.schemaExecutor = schemaExecutor;
    }*/

    @RequestMapping(value = "/graphql", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> graphql(@RequestBody Map body) {
        String query = (String) body.get("query");
        Map<String, Object> variables = (Map<String, Object>) body.get("variables");

        ExecutionResult result = schemaExecutor.execute(helloWorldSchema, query, (Object) null, variables);

        GraphQLHttpResponse response = new GraphQLHttpResponse(result);

        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
