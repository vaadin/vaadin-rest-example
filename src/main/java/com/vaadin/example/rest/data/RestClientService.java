package com.vaadin.example.rest.data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;

import com.fasterxml.jackson.databind.JsonNode;
import com.vaadin.flow.data.provider.DataProvider;

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
 * {@link #fetchData(int, int)} and {@link #fetchCount()} demonstrate the two
 * methods needed for creating Lazy {@link DataProvider}s, where we don't fetch
 * all results immediately, but only a portion at a time. This is done to reduce
 * unnecessary memory consumption.
 */
@SuppressWarnings("serial")
@Service
public class RestClientService implements Serializable {

	/**
	 * The port changes depending on where we deploy the app
	 */
	@Value("${server.port}")
	private String serverPort;

	/**
	 * Returns parsed {@link CommentDTO} objects from the REST service.
	 *
	 * Useful when the response data has a known structure.
	 */
	public List<CommentDTO> getAllComments() {

		System.out.println("Fetching all Comment objects through REST..");

		// Fetch from 3rd party API; configure fetch
		final RequestHeadersSpec<?> spec = WebClient.create().get()
				.uri("https://jsonplaceholder.typicode.com/comments");

		// do fetch and map result
		final List<CommentDTO> comments = spec.retrieve().toEntityList(CommentDTO.class).block().getBody();

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

		// Fetch from 3rd party API; configure fetch
		final RequestHeadersSpec<?> spec = WebClient.create().get().uri("https://jsonplaceholder.typicode.com/posts");

		// do fetch and map result
		final List<JsonNode> posts = spec.retrieve().toEntityList(JsonNode.class).block().getBody();

		System.out.println(String.format("...received %d items.", posts.size()));

		return posts;

	}

	/**
	 * Fetches the specified amount of data items starting from index 'offset' from
	 * the REST API.
	 */
	public Stream<DataDTO> fetchData(int count, int offset) {

		System.out.println(String.format("Fetching partial data set %d through %d...", offset, offset + count));

		// We use a local provider for this bigger data set.
		// The API has two methods, 'data' and 'count'.

		// Other than that, this method is similar to #getAllComments().
		final String url = String.format("http://localhost:" + serverPort + "/data?count=%d&offset=%d", count, offset);

		final RequestHeadersSpec<?> spec = WebClient.create().get().uri(url);
		final List<DataDTO> posts = spec.retrieve().toEntityList(DataDTO.class).block().getBody();

		System.out.println(String.format("...received %d items.", posts.size()));
		return posts.stream();
	}

	/**
	 * Fetches the total number of items available through the REST API
	 */
	public int fetchCount() {

		System.out.println("fetching count...");

		// We use a local provider for this bigger data set.
		// The API has two methods, 'data' and 'count'.
		final String url = String.format("http://localhost:" + serverPort + "/count");

		final RequestHeadersSpec<?> spec = WebClient.create().get().uri(url);
		final Integer response = spec.retrieve().toEntity(Integer.class).block().getBody();

		System.out.println("...count is " + response);
		return response;

	}

}
