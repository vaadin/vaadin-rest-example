package com.vaadin.example.rest.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.example.rest.data.DataDTO;
import com.vaadin.example.rest.data.RestClientService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Lazy DTO | Vaadin REST Examples")
@Route(value = "lazy-dto", layout = MainLayout.class)
public class LazyDTOView extends Main {

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
		final Grid<DataDTO> dataGrid = new Grid<DataDTO>();

		dataGrid.addColumn(DataDTO::getTitle).setHeader("Post title").setWidth("300px");
		dataGrid.addColumn(DataDTO::getMessage).setHeader("Post body").getFlexGrow();

		final Button fetchData = new Button("Create lazy provider", e -> {

			// Here we give the DataProvider our two callback methods. The Grid will call
			// them on demand. The second type in the declaration (Void) is a filter type;
			// we won't use filters in this example.
			final DataProvider<DataDTO, Void> lazyProvider = DataProvider
					.fromCallbacks(q -> service.fetchData(q.getLimit(), q.getOffset()), q -> service.fetchCount());

			dataGrid.setDataProvider(lazyProvider);
		});

		fetchData.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		add(fetchData, dataGrid);
	}
}
