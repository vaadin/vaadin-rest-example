package com.vaadin.example.rest.data;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;

/**
 * Example Spring service that connects to a REST API asynchronously.
 * <p>
 *
 * @see RestClientService
 */
@SuppressWarnings("serial")
@Service
public class AsyncRestClientService implements Serializable {

    /**
     * Generic callback interface for asynchronous operations.
     *
     * @param <T> the result type
     */
    public static interface AsyncRestCallback<T> {
        void operationFinished(T results);
    }

    /**
     * Returns parsed {@link CommentDTO} objects from the REST service,
     * asynchronously.
     */
    public void getAllCommentsAsync(AsyncRestCallback<List<CommentDTO>> callback) {

        System.out.println("Setting up fetching all Comment objects through REST..");

        // Configure fetch as normal
        RequestHeadersSpec<?> spec = WebClient.create().get().uri("https://jsonplaceholder.typicode.com/comments");

        // But instead of 'block', do 'subscribe'. This means the fetch will run on a
        // separate thread and notify us when it's ready by calling our lambda
        // operation.
        spec.retrieve().toEntityList(CommentDTO.class).subscribe(result -> {

            // This code block is run whenever the results are back

            // get results as usual
            final List<CommentDTO> comments = result.getBody();

            System.out.println(String.format("...received %d items.", comments.size()));

            // call the ui with the data
            callback.operationFinished(comments);
        });

    }

}
