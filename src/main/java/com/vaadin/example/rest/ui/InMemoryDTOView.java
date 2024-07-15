package com.vaadin.example.rest.ui;

import com.vaadin.example.rest.data.CommentDTO;
import com.vaadin.example.rest.data.RestClientService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("In-Memory DTO | Vaadin REST Examples")
@Route(value = "in-memory-dto", layout = MainLayout.class)
public class InMemoryDTOView extends VerticalLayout {

	public InMemoryDTOView(@Autowired RestClientService service) {
		// First example uses a Data Transfer Object (DTO) class that we've created. The
		// Vaadin Grid works well with entity classes, so this is quite straightforward:
		final Grid<CommentDTO> commentsGrid = new Grid<>(CommentDTO.class);

		// Fetch all entities and show
		final Button fetchComments = new Button("Fetch all comments",
				e -> commentsGrid.setItems(service.getAllComments()));
		fetchComments.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		add(fetchComments, commentsGrid);

	}

}
