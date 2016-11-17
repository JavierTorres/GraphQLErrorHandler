package com.torres.graphql.fetcher;

import com.torres.graphql.error.DataFetchingException;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;
import static com.torres.graphql.error.DataFetchingException.Type.NO_CONNECTION;

/**
 * Created by javierbracerotorres on 15/11/2016.
 */
@Component
public class HelloWorldFetcher implements DataFetcher {

    private boolean connectionError = false;

    @Override
    public Object get(DataFetchingEnvironment dataFetchingEnvironment) {
        if (connectionError) {
            throw new DataFetchingException(NO_CONNECTION);
        }

        return "World";
    }

    public void setConnectionError(boolean connectionError) {
        this.connectionError = connectionError;
    }
}
