package com.torres.graphql.schema

import com.torres.graphql.Main
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{GivenWhenThen, BeforeAndAfter, FlatSpec, Matchers}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.TestContextManager
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

/**
 * Created by javierbracerotorres on 23/11/2016.
 */
@RunWith(classOf[JUnitRunner])
@SpringApplicationConfiguration(classes = Array(classOf[Main]))
abstract class SchemaTest(controllerClass: Class[_]) extends FlatSpec with Matchers with MockitoSugar with BeforeAndAfter with GivenWhenThen {
  behavior of controllerClass.getSimpleName

  before {
    new TestContextManager(this.getClass).prepareTestInstance(this)
  }
}
