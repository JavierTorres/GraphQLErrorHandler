package com.torres.graphql.error;

/**
 * Created by javierbracerotorres on 16/11/2016.
 */
public abstract class AbstractGraphQLException extends RuntimeException implements GraphQLErrorWithCode {
    private final String message;
    private final int code;

    public AbstractGraphQLException(String message, int code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return "{message='" + message + '\'' + ", code=" + code + '}';
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "{message='" + message + '\'' + ", code=" + code + '}';
    }
}
