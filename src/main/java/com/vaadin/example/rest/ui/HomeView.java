package com.vaadin.example.rest.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Section;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Vaadin REST Examples")
@Route(value = "", layout = MainLayout.class)
public class HomeView extends VerticalLayout {

	public HomeView() {
		add(new Section(new Paragraph(
				"This example app demonstrates how to call REST services and showing the results in a Vaadin Grid."),
				new Span("The sources for this application can be found "),
				new Anchor("https://github.com/vaadin/vaadin-rest-example", "here.")));

		add(new Example(InMemoryDTOView.class,
				"Calling a REST service using a DTO class for results",
				"""
				The first example uses a Data Transfer Object (DTO) class that we've created. Using a DTO is the 
				standard way of calling REST services in Java, and this approach works well with Vaadin components as 
				well.
				"""));

		add(new Example(InMemoryJSONView.class,
				"Calling a REST service with pure JSON results",
				"""
				The second example does not use a DTO, but raw JSON instead using the Jackson library included with 
				Spring. This approach works well when the received data is dynamic in nature, or you don't want to 
				create a DTO.
				"""));

		add(new Example(AsyncInMemoryDTOView.class,
				"Calling a REST service asynchronously",
				"""
				This example fetches the same data as the first example, but asychronously. This helps when the REST
				server is slow; starting the REST fetch does not block the application. The same method can be used
				with the second example as well, but not for lazy providers (the last example).
				"""));

		add(new Example(LazyDTOView.class,
				"Creating a lazy loading (paging) DataProvider that uses REST",
				"""
				The final example demonstrates how to create a lazy data provider for the Grid. Instead of fetching all
				results, we create a DataProvider that fetches only a portion of the data at a time. This approach works
				well for very big data sets that you don't want to load all at once.
				"""));

	}

	private static class Example extends Section {
		public Example(Class<? extends Component> viewClass, String linkText, String description) {
			add(new H2(new RouterLink(linkText, viewClass)));
			add(new Paragraph(description));
		}
	}
}
