package com.vaadin.example.rest.data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Example Spring service that connects to a REST API.
 * <p>
 * The class has three different examples for fetching data.
 * <p>
 * {@link #getAllComments()} uses a DTO class to map the JSON results from a 3rd
 * party API. It fetches all available results immediately.
 * <p>
 * {@link #getAllPosts()} does not use a DTO for the results, but lets the user
 * process the result JSON in the UI class instead. It fetches all available
 * results immediately.
 * <p>
 * {@link #fetchData(int, int)}  demonstrate the two
 * methods needed for creating lazy databinding, where we don't fetch
 * all results immediately, but only a portion at a time. This is done to reduce
 * unnecessary memory consumption.
 */
@SuppressWarnings("serial")
@Service
public class RestClientService implements Serializable {

	private final RestClient jsonplaceholderClient = RestClient.create("https://jsonplaceholder.typicode.com");
	private final RestClient localClient;

	public RestClientService(@Value("${server.port}") String serverPort) {
		localClient = RestClient.create("http://localhost:" + serverPort );
	}

	/**
	 * Returns parsed {@link CommentDTO} objects from the REST service.
	 *
	 * Useful when the response data has a known structure.
	 */
	public List<CommentDTO> getAllComments() {

		System.out.println("Fetching all Comment objects through REST..");

		// Fetch from 3rd party API; configure fetch

		// do fetch and map result
		List<CommentDTO> comments = jsonplaceholderClient.get().uri("comments").retrieve()
				.body(new ParameterizedTypeReference<>() {});

		System.out.println(String.format("...received %d items.", comments.size()));

		return comments;
	}

	/**
	 * Returns non-parsed JSON response objects from the REST service.
	 *
	 * Useful when you don't want to create a DTO class, or the response data has a
	 * dynamic structure.
	 */
	public List<JsonNode> getAllPosts() {

		System.out.println("Fetching all Post objects through REST..");

		// do fetch and use Jackson's raw JsonNode instead of properly mapped DTO
		final List<JsonNode> posts = jsonplaceholderClient.get().uri("posts").retrieve()
				.body(new ParameterizedTypeReference<>() {});

		System.out.println(String.format("...received %d items.", posts.size()));

		return posts;

	}

	/**
	 * Fetches the specified amount of data items starting from index 'offset' from
	 * the REST API.
	 */
	public Stream<MessageDTO> fetchData(int limit, int offset) {
		System.out.println(String.format("Fetching partial data set %d through %d...", offset, offset + limit));


		// We use a local provider for this bigger data set.
		// The API has two parameters, 'count' and 'offset'.

		// Other than that, this method is similar to #getAllComments().
		final List<MessageDTO> posts = localClient.get().uri(uriBuilder ->
            uriBuilder.path("data")
					.queryParam("limit", limit)
					.queryParam("offset", offset)
					.build())
				.retrieve()
				.body(new ParameterizedTypeReference<>() {});

		System.out.println(String.format("...received %d items.", posts.size()));
		return posts.stream();
	}

}
