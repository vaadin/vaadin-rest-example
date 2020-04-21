package com.vaadin.example.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	/**
	 * Returns parsed {@link CommentDTO} objects from the REST service.
	 *
	 * Useful when the response data has a known structure.
	 */
	public List<CommentDTO> getAllComments() {

		System.out.println("Fetching all Comment objects through REST..");

		final RestTemplate template = restTemplateBuilder.build();

		// Fetch from 3rd party API
		final CommentDTO[] comments = template.getForObject("https://jsonplaceholder.typicode.com/comments",
				CommentDTO[].class);

		return Arrays.asList(comments);
	}

	/**
	 * Returns non-parsed JSON response objects from the REST service.
	 *
	 * Useful when you don't want to create a DTO class, or the response data has a
	 * dynamic structure.
	 */
	public List<JsonNode> getAllPosts() {

		System.out.println("Fetching all Post objects through REST..");

		final RestTemplate template = restTemplateBuilder.build();

		// Fetch from 3rd party API
		final ResponseEntity<String> response = template.getForEntity("https://jsonplaceholder.typicode.com/posts",
				String.class);

		try {
			final ObjectMapper mapper = new ObjectMapper();
			final JsonNode root = mapper.readTree(response.getBody());

			if (!root.isArray()) {
				// Something went wrong, we should get an array
				return null;
			}

			final List<JsonNode> posts = new ArrayList<>();
			root.forEach(n -> posts.add(n));
			return posts;

		} catch (final JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Fetches the specified amount of data items starting from index 'offset' from
	 * the REST API.
	 */
	public Stream<DataDTO> fetchData(int count, int offset) {

		System.out.println(String.format("Fetching partial data set %d through %d...", offset, offset + count));

		// We use a local provider for this bigger data set.
		// The API has two methods, 'data' and 'count'.

		// Other than that, this method is similar to #getAllPosts().
		final RestTemplate template = restTemplateBuilder.build();
		final String url = String.format("http://localhost:8080/data?count=%d&offset=%d", count, offset);
		final DataDTO[] response = template.getForObject(url, DataDTO[].class);

		final List<DataDTO> posts = Arrays.asList(response);

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
		final RestTemplate template = restTemplateBuilder.build();
		final String url = String.format("http://localhost:8080/count");
		final Integer response = template.getForObject(url, Integer.class);

		System.out.println("...count is " + response);
		return response;

	}

}
