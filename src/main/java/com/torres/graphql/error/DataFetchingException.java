package com.torres.graphql.error;

import graphql.ErrorType;
import graphql.language.SourceLocation;

import java.util.List;

/**
 * Created by javierbracerotorres on 16/11/2016.
 */
public class DataFetchingException extends AbstractGraphQLException {

    public DataFetchingException(Type type) {
        super(type.description, type.code);
    }

    public enum Type {

        NO_CONNECTION(301, "No connection"),
        TIMEOUT(302, "Timeout exception");

        private final int code;
        private final String description;


        Type(int code, String description) {
            this.code = code;
            this.description = description;
        }
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.DataFetchingException;
    }
}
