package com.torres.graphql

import com.torres.graphql.fetcher.HelloWorldFetcher
import org.junit.runner.RunWith
import org.junit.{Before, Test}
import org.scalatest.junit.JUnitSuite
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.{content, status}
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(classOf[SpringJUnit4ClassRunner])
@WebAppConfiguration
@SpringBootTest
class ScalaGraphQLControllerTest extends JUnitSuite {
  @Autowired
  val webApplicationContext: WebApplicationContext = null

  @Autowired
  val helloWorldFetcher: HelloWorldFetcher = null

  var mockMvc: MockMvc = _

  @Before
  def setup():Unit = {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
  }

  @Test
  def testGraphQLOk():Unit = {
    val request =
      """
      {
        "query":
          "{ hello }",
          "variables":{}
      }
      """

    val response =
      """
      {
        "data":
          {
            "hello":"World"
          }
      }
      """

    helloWorldFetcher.setConnectionError(false)
    mockMvc.perform(post("/graphql")
      .contentType(MediaType.APPLICATION_JSON_UTF8)
      .content(request))
      .andExpect(status().isOk())
      .andExpect(content().json(response));
  }

  @Test
  def testGraphQLWithFetchingErrors():Unit = {
    val request =
      """
      {
        "query":
          "{ hello }",
          "variables":{}
      }
      """

    val response =
      """
      {
        "data":
          {
            "hello":null
          },
        "errors":[{"code":"301", "message": "No connection"}]
      }
      """
    helloWorldFetcher.setConnectionError(true)

    mockMvc.perform(post("/graphql")
      .contentType(MediaType.APPLICATION_JSON_UTF8)
      .content(request))
      .andExpect(status().isInternalServerError)
      .andExpect(content().json(response));
  }

  @Test
  def testGraphQLWithInvalidBody():Unit = {
    val request =
      """
      {
        "query":
          "{ INVALID BODY }",
          "variables":{}
      }
      """

    val response =
      """
      {
        "data": null,
        "errors":[
          {"message": "Validation error of type FieldUndefined: Field INVALID is undefined"},
          {"message": "Validation error of type FieldUndefined: Field BODY is undefined"}
        ]
      }
      """
    helloWorldFetcher.setConnectionError(true)

    mockMvc.perform(post("/graphql")
      .contentType(MediaType.APPLICATION_JSON_UTF8)
      .content(request))
      .andExpect(status().isBadRequest)
      .andExpect(content().json(response));
  }
}
