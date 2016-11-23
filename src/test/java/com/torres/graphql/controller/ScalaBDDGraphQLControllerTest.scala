package com.torres.graphql.controller

import com.torres.graphql.fetcher.HelloWorldFetcher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders._
import org.springframework.test.web.servlet.result.MockMvcResultMatchers._

/**
 * Created by javierbracerotorres on 22/11/2016.
 */
@WebAppConfiguration
class ScalaBDDGraphQLControllerTest extends ControllerTest(classOf[GraphQLController]) {

  @Autowired
  val helloWorldFetcher: HelloWorldFetcher = null

  it should "return World when no errors" in {

    val request =
      """
      {
        "query":
          "{ hello }",
          "variables":{}
      }
      """

    Given("the request body: " + request)
    When("calling the url /graphql")
    helloWorldFetcher.setConnectionError(false)

    val response =
      """
      {
        "data":
          {
            "hello":"World"
          }
      }
      """
    Then("the response must be " + response)

    mvc.perform(post("/graphql")
      .contentType(MediaType.APPLICATION_JSON)
      .content(request))
      .andExpect(status().isOk())
      .andExpect(content().json(response));

  }

  it should "return an error when fetching error" in {

    val request =
      """
      {
        "query":
          "{ hello }",
          "variables":{}
      }
      """

    Given("the request body: " + request)
    When("calling the url /graphql and there is a fetching error")
    helloWorldFetcher.setConnectionError(true)

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
    Then("the response must be " + response)

    mvc.perform(post("/graphql")
      .contentType(MediaType.APPLICATION_JSON_UTF8)
      .content(request))
      .andExpect(status().isInternalServerError)
      .andExpect(content().json(response));

  }

  it should "return an error when the body request is invalid" in {

    val request =
      """
      {
        "query":
          "{ INVALID BODY }",
          "variables":{}
      }
      """

    Given("the request body: " + request)
    When("calling the url /graphql")
    helloWorldFetcher.setConnectionError(false)

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
    Then("the response must contain two validations errors" + response)

    mvc.perform(post("/graphql")
      .contentType(MediaType.APPLICATION_JSON_UTF8)
      .content(request))
      .andExpect(status().isBadRequest)
      .andExpect(content().json(response));

  }
}