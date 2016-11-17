package com.torres.graphql.error;

import graphql.GraphQLError;

/**
 * Created by javierbracerotorres on 16/11/2016.
 */
public interface GraphQLErrorWithCode extends GraphQLError {
    int getCode();
}
