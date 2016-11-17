package com.torres.graphql.query;

import com.torres.graphql.fetcher.HelloWorldFetcher;
import graphql.schema.GraphQLObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.Scalars.GraphQLString;

/**
 * Created by javierbracerotorres on 15/11/2016.
 */
@Component
public class HelloWorldQuery {
    @Autowired
    HelloWorldFetcher fetcher;

    public GraphQLObjectType getQueryType() {
        return newObject()
                .name("helloWorldQuery")
                .field(newFieldDefinition()
                        .type(GraphQLString)
                        .name("hello")
                        .dataFetcher(fetcher))
                .build();
    }
}
