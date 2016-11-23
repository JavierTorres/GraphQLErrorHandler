package com.torres.graphql.schema

import com.torres.graphql.Main
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.web.WebAppConfiguration

import scala.collection.JavaConversions._

/**
 * Created by javierbracerotorres on 23/11/2016.
 */
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Array(classOf[Main]))
class SchemaExecutorTest extends SchemaTest(classOf[SchemaExecutor]) {

  @Autowired val helloWorldSchema: HelloWorldSchema = null

  "Schema executor" should "no return errors" in {

    val query = "{hello}"
    val variables = mapAsJavaMap(Map()).asInstanceOf[java.util.Map[String, Object]]

    val result = new SchemaExecutor().execute(helloWorldSchema, query, null, variables)

    result.getErrors shouldBe empty
  }

}
