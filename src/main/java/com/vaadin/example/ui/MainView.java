package com.vaadin.example.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.vaadin.example.data.CommentDTO;
import com.vaadin.example.data.DataDTO;
import com.vaadin.example.data.RestClientService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

/**
 * Example application that demonstrates how to Use Spring to fetch data from a
 * REST source and how to show it in a Vaadin Grid.
 * <p>
 * 3rd party data is fetched from https://jsonplaceholder.typicode.com/ using
 * our {@link RestClientService) class.
 */
@Route
public class MainView extends VerticalLayout {

	public MainView(@Autowired RestClientService service) {

		inMemoryDTOExample(service);
		inMemoryJSONExample(service);
		lazyDTOExample(service);
	}

	private void inMemoryDTOExample(RestClientService service) {

		// First example uses a Data Transfer Object (DTO) class that we've created. The
		// Vaadin Grid works well with entity classes, so this is quite straightforward:
		final Grid<CommentDTO> commentsGrid = new Grid<CommentDTO>(CommentDTO.class);

		// Fetch all entities and show
		final Button fetchComments = new Button("Fetch all comments",
				e -> commentsGrid.setItems(service.getAllComments()));

		add(fetchComments, commentsGrid);
	}

	private void inMemoryJSONExample(RestClientService service) {

		// The second example does not use a DTO, but raw JSON instead using the Jackson
		// library included with Spring. The data is a List of JSON nodes; for each
		// column, we define how to get the correct data from the node.

		// This is useful when the REST API returns dynamic data
		final Grid<JsonNode> postsGrid = new Grid<JsonNode>();

		postsGrid.addColumn(node -> node.get("id")).setHeader("Id").setTextAlign(ColumnTextAlign.END).setWidth("50px");
		postsGrid.addColumn(node -> node.get("title")).setHeader("Post title").setWidth("300px");
		postsGrid.addColumn(node -> node.get("body")).setHeader("Post body").getFlexGrow();

		// TODO col widths aren't applied??

		// Fetch all data and show
		final Button fetchPosts = new Button("Fetch all posts", e -> postsGrid.setItems(service.getAllPosts()));

		add(fetchPosts, postsGrid);
	}

	private void lazyDTOExample(RestClientService service) {

		// The third example demonstrates how to create a lazy data provider for the
		// Grid. Instead of fetching all results, we fetch only a portion at a time;
		// the Grid can do this automatically if we give it callback methods.

		// This way we don't waste CPU and memory on all 10 000 items the REST API can
		// provide; we only fetch what the user sees and then fetch more if needed.

		// Note that the REST service you connect to needs to support partial fetches.
		// We need two methods in the API; a method that returns a specific number of
		// items, and a method that returns the total amount of available data as an
		// integer.
		final Grid<DataDTO> testgrid = new Grid<DataDTO>();

		testgrid.addColumn(DataDTO::getTitle).setHeader("Post title").setWidth("300px");
		testgrid.addColumn(DataDTO::getMessage).setHeader("Post body").getFlexGrow();

		final Button testbutton = new Button("Create lazy provider", e -> {

			// Here we give the DataProvider our two callback methods. The Grid will call
			// them on demand. The second type in the declaration (Void) is a filter type;
			// we won't use filters in this example.
			final DataProvider<DataDTO, Void> lazyProvider = DataProvider
					.fromCallbacks(q -> service.fetchData(q.getLimit(), q.getOffset()), q -> service.fetchCount());

			testgrid.setDataProvider(lazyProvider);
		});

		add(testbutton, testgrid);
	}
}
