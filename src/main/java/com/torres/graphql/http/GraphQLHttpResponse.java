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
    private static final String CODE = "code";
    private static final String CODE_FROM = CODE + "=";
    private static final String CODE_TO = "}";
    private static final String MESSAGE = "message";
    private static final String MESSAGE_FROM = MESSAGE + "='";
    private static final String MESSAGE_TO = "',";

    private static final Logger log = LoggerFactory.getLogger(GraphQLHttpResponse.class);

    Map<String, Object> response = new HashMap<>();

    private Optional<ErrorType> errorType = Optional.empty();

    public GraphQLHttpResponse(ExecutionResult executionResult) {
        if (!executionResult.getErrors().isEmpty()) {

            List<Map<String, Object>> errors = executionResult.getErrors().stream()
                    .map(e -> {
                        Map<String, Object> error = new HashMap<>();

                        errorType = Optional.of(e.getErrorType());

                        if (e.getMessage().contains(CODE_FROM) && e.getMessage().contains(MESSAGE_FROM)) {
                            error.put(CODE, parseMessageFromTo(e.getMessage(), CODE_FROM, CODE_TO));
                            error.put(MESSAGE, parseMessageFromTo(e.getMessage(), MESSAGE_FROM, MESSAGE_TO));

                        } else {
                            // Not containing error code
                            error.put(MESSAGE, e.getMessage());
                        }

                        return error;
                    })
                    .collect(Collectors.toList());

            response.put(ERRORS, errors);
            log.error("Http Response Errors: {}", errors);
        }

        response.put(DATA, executionResult.getData());
    }

    private String parseMessageFromTo(String m, String from, String to) {
        return m.substring(m.indexOf(from) + from.length(), m.indexOf(to));
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
