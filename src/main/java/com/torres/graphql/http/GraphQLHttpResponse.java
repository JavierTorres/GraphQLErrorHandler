package com.torres.graphql.http;

import com.fasterxml.jackson.annotation.*;
import graphql.ErrorType;
import graphql.ExecutionResult;
import graphql.GraphQLError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by javierbracerotorres on 16/11/2016.
 */
public class GraphQLHttpResponse {
    private static final String ERRORS = "errors";
    private static final String DATA = "data";

    private static final Logger log = LoggerFactory.getLogger(GraphQLHttpResponse.class);

    Map<String, Object> response = new HashMap<>();

    private Optional<ErrorType> errorType = Optional.empty();

    public GraphQLHttpResponse(ExecutionResult executionResult) {
        if (!executionResult.getErrors().isEmpty()) {

            List<Map<String, Object>> errors = executionResult.getErrors().stream()
                    .map(e -> {
                        Map<String, Object> error = new HashMap<>();

                        errorType = Optional.of(e.getErrorType());

                        if (e.getMessage().contains("code=") && e.getMessage().contains("message=")) {
                            error.put("code", e.getMessage().substring(e.getMessage().indexOf("code=") + "code=".length(), e.getMessage().indexOf("}")));
                            error.put("message", e.getMessage().substring(e.getMessage().indexOf("message='") + "message='".length(), e.getMessage().indexOf("',")));

                        } else {
                            // Not containing error code
                            error.put("message", e.getMessage());
                        }

                        return error;
                    })
                    .collect(Collectors.toList());

            response.put(ERRORS, errors);
            log.error("Http Response Errors: {}", errors);
        }

        response.put(DATA, executionResult.getData());
    }

    public HttpStatus getHttpStatus() {
        if (!errorType.isPresent()) {
            return HttpStatus.OK;
        }

        switch (errorType.get()) {
            case InvalidSyntax:
            case ValidationError:
                return HttpStatus.BAD_REQUEST;

            case DataFetchingException:
                return HttpStatus.INTERNAL_SERVER_ERROR;

            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @JsonValue
    public Map<String, Object> getResponse() {
        return response;
    }

    @JsonIgnore
    public List<GraphQLError> getErrors() {
        return response.containsKey(ERRORS) ? (List<GraphQLError>) response.get(ERRORS) : new ArrayList<>();
    }
}
