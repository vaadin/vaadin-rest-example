package com.vaadin.example.rest.ui;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Vaadin REST Examples")
@Route(value = "", layout = MainLayout.class)
public class HomeView extends Main {

	public HomeView() {
		add(new Section(new H2(new RouterLink("In-Memory DTO", InMemoryDTOView.class)),
				new Paragraph("First example uses a Data Transfer Object (DTO) class that we've created.")),

				new Section(new H2(new RouterLink("In-Memory JSON", InMemoryJSONView.class)), new Paragraph(
						"The second example does not use a DTO, but raw JSON instead using the Jackson library included with Spring.")),

				new Section(new H2(new RouterLink("Lazy DTO", LazyDTOView.class)), new Paragraph(
						"The third example demonstrates how to create a lazy data provider for the Grid. Instead of fetching all results, we fetch only a portion at a time.")));

	}
}
