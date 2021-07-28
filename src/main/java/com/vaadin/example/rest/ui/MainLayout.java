package com.vaadin.example.rest.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.RouterLink;

/**
 * Example application that demonstrates how to Use Spring to fetch data from a
 * REST source and how to show it in a Vaadin Grid.
 * <p>
 * 3rd party data is fetched from https://jsonplaceholder.typicode.com/ using
 * our {@link RestClientService) class.
 */
@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout implements AfterNavigationObserver {

	private final H1 pageTitle;
	private final RouterLink home;
	private final RouterLink inMemoryDTO;
	private final RouterLink asyncInMemoryDTO;
	private final RouterLink inMemoryJSON;
	private final RouterLink lazyDTO;

	public MainLayout() {
		// Navigation
		home = new RouterLink("Home", HomeView.class);
		inMemoryDTO = new RouterLink("In-Memory DTO", InMemoryDTOView.class);
		asyncInMemoryDTO = new RouterLink("Asynchronous DTO", AsyncInMemoryDTOView.class);
		inMemoryJSON = new RouterLink("In-Memory JSON", InMemoryJSONView.class);
		lazyDTO = new RouterLink("Lazy DTO", LazyDTOView.class);

		final UnorderedList list = new UnorderedList(new ListItem(home), new ListItem(inMemoryDTO),
				new ListItem(asyncInMemoryDTO), new ListItem(inMemoryJSON), new ListItem(lazyDTO));
		final Nav navigation = new Nav(list);
		addToDrawer(navigation);
		setPrimarySection(Section.DRAWER);
		setDrawerOpened(false);

		// Header
		pageTitle = new H1("Home");
		final Header header = new Header(new DrawerToggle(), pageTitle);
		addToNavbar(header);
	}

	private RouterLink[] getRouterLinks() {
		return new RouterLink[] { home, inMemoryDTO, inMemoryJSON, lazyDTO };
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		for (final RouterLink routerLink : getRouterLinks()) {
			if (routerLink.getHighlightCondition().shouldHighlight(routerLink, event)) {
				pageTitle.setText(routerLink.getText());
			}
		}
	}
}
