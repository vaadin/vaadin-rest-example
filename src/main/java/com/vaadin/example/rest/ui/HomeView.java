package com.vaadin.example.rest.ui;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Vaadin REST Examples")
@Route(value = "", layout = MainLayout.class)
public class HomeView extends Main {

	public HomeView() {
		add(
				new Section(
						new H2(new RouterLink("In-Memory DTO", InMemoryDTOView.class)),
						new Paragraph("First example uses a Data Transfer Object (DTO) class that we've created. The Vaadin Grid works well with entity classes, so this is quite straightforward:")
				),

				new Section(
						new H2(new RouterLink("In-Memory JSON", InMemoryJSONView.class)),
						new Paragraph("The second example does not use a DTO, but raw JSON instead using the Jackson library included with Spring. The data is a List of JSON nodes; for each column, we define how to get the correct data from the node.")
				),

				new Section(
						new H2(new RouterLink("Lazy DTO", LazyDTOView.class)),
						new Paragraph("The third example demonstrates how to create a lazy data provider for the Grid. Instead of fetching all results, we fetch only a portion at a time; the Grid can do this automatically if we give it callback methods."),
						new Paragraph("This way we don't waste CPU and memory on all 10 000 items the REST API can provide; we only fetch what the user sees and then fetch more if needed."),
						new Paragraph("Note that the REST service you connect to needs to support partial fetches. We need two methods in the API; a method that returns a specific number of items, and a method that returns the total amount of available data as an integer.")
				)
		);

	}
}
