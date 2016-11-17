package com.torres.graphql.schema;

import graphql.ExecutionResult;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by javierbracerotorres on 16/11/2016.
 */
@Component
public class SchemaExecutor {

    public ExecutionResult execute(Schema schema, String requestString, Object context, Map<String, Object> variables) {
        return schema.toGraphQL().execute(requestString, context, variables);
    }
}
