package com.vaadin.example.rest.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.example.rest.data.AsyncRestClientService;
import com.vaadin.example.rest.data.CommentDTO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Asynchronous In-Memory DTO | Vaadin REST Examples")
@Route(value = "async-in-memory-dto", layout = MainLayout.class)
public class AsyncInMemoryDTOView extends Main {

	private final Grid<CommentDTO> commentsGrid;
	private final Span statusLabel;
	private final AsyncRestClientService service;

	public AsyncInMemoryDTOView(@Autowired AsyncRestClientService service) {
		this.service = service;

		commentsGrid = new Grid<CommentDTO>(CommentDTO.class);

		statusLabel = new Span(" Fetching results, please wait...");
		statusLabel.setVisible(false);

		// Fetch all entities and show
		final Button fetchComments = new Button("Fetch all comments", e -> startFetch());
		fetchComments.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		add(fetchComments, statusLabel, commentsGrid);

	}

	private void startFetch() {

		// In this method we ask our service to start fetching the results from REST,
		// and wait for the result.

		// These are run immediately, to give the user feedback that we are doing
		// something
		statusLabel.setVisible(true);
		commentsGrid.setEnabled(false);

		// Calling the service to start the op. The callback e provide is called when
		// the results are available.
		service.getAllCommentsAsync(result -> {

			// We now have the results. But, because this call might happen outside normal
			// Vaadin calls, we need to make sure the HTTP Session data of this app isn't
			// violated. For this we use UI#access()
			getUI().get().access(() -> {

				// Finally, we can modify the UI state. These changes are sent to the users
				// browser immediately, because we have enable Websocket Server Push (@Push
				// annotation in MainLayout).
				statusLabel.setVisible(false);
				commentsGrid.setEnabled(true);
				commentsGrid.setItems(result);
			});
		});
	}
}
