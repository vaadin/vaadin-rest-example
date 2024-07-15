package com.vaadin.example.rest.ui;

import com.vaadin.example.rest.data.MessageDTO;
import com.vaadin.example.rest.data.RestClientService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Lazy DTO | Vaadin REST Examples")
@Route(value = "lazy-dto", layout = MainLayout.class)
public class LazyDTOView extends VerticalLayout {

	public LazyDTOView(@Autowired RestClientService service) {
		// The third example demonstrates how to create a lazy data provider for the
		// Grid. Instead of fetching all results, we fetch only a portion at a time;
		// the Grid can do this automatically if we give it callback methods.

		// This way we don't waste CPU and memory on all 10 000 items the REST API can
		// provide; we only fetch what the user sees and then fetch more if needed.

		// Note that the REST service you connect to needs to support partial fetches.
		// We need two methods in the API; a method that returns a specific number of
		// items, and a method that returns the total amount of available data as an
		// integer.
		final Grid<MessageDTO> dataGrid = new Grid<MessageDTO>();

		dataGrid.addColumn(MessageDTO::getTitle).setHeader("Post title").setWidth("300px");
		dataGrid.addColumn(MessageDTO::getMessage).setHeader("Post body").getFlexGrow();

		// Giving Grid a callback it can utilize to fetch data as needed when the user scrolls
		dataGrid.setItems( q -> service.fetchData(q.getLimit(), q.getOffset()));

		add(dataGrid);
	}
}
