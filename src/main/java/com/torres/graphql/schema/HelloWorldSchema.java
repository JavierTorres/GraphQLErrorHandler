package com.torres.graphql.schema;

import com.torres.graphql.query.HelloWorldQuery;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by javierbracerotorres on 15/11/2016.
 */
@Component
public class HelloWorldSchema implements Schema {

    @Autowired
    HelloWorldQuery query;

    @Override
    public GraphQL toGraphQL() {
        GraphQLSchema schema = GraphQLSchema.newSchema()
                .query(query.getQueryType())
                .build();

        return new GraphQL(schema);
    }
}
