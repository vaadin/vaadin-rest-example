package com.vaadin.example.rest.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vaadin.example.rest.Application;

/**
 * Example Spring service that connects to a REST API asynchronously.
 * <p>
 *
 * @see RestClientService
 */
@SuppressWarnings("serial")
@Service
public class AsyncRestClientService implements Serializable {

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	/**
	 * Default executor, configured in the {@link Application} class
	 */
	@Autowired
	private Executor threadPool;

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
	public void getAllCommentsAsync(AsyncRestCallback<List<CommentDTO>> readyCallback) {

		System.out.println("Creating separate Thread for fetching and processing results.");

		// create a new background process that calls the REST API.
		threadPool.execute(() -> {
			System.out.println("Fetching all Comment objects through REST..");

			final RestTemplate template = restTemplateBuilder.build();

			// Fetch from 3rd party API
			final CommentDTO[] comments = template.getForObject("https://jsonplaceholder.typicode.com/comments",
					CommentDTO[].class);

			System.out.println(String.format("...received %d items.", comments.length));

			// call the ui with the data
			readyCallback.operationFinished(Arrays.asList(comments));
		});

	}

}
