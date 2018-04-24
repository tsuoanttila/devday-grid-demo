package org.vaadin.teemu.spring;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.vaadin.teemu.spring.data.Beverage;
import org.vaadin.teemu.spring.data.BeverageRepository;
import org.vaadin.teemu.spring.data.Origin;
import org.vaadin.teemu.spring.data.ShoppingCart;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.CallbackDataProvider.CountCallback;
import com.vaadin.flow.data.provider.CallbackDataProvider.FetchCallback;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;

/**
 * The main view contains a simple label element and a template element.
 */
@HtmlImport("styles/shared-styles.html")
@Route("")
@PageTitle("Vaadin 10 Demo")
public class MainView extends VerticalLayout {

	private Binder<Beverage> binder = new Binder<>();
	private Grid<Beverage> grid = new Grid<>();
	private TextField nameEditor = new TextField();
	private Button saveButton = new Button("Save");
	private Button showButton = new Button("Show");

	@Autowired
	BeverageRepository repo;

	public MainView() {
		add(new H2("DevDay Grid Demo for Vaadin 10"));

		grid.addComponentColumn(beverage -> {
			Div div = new Div();
			div.setClassName(beverage.getOrigin().name().toLowerCase());
			div.setText(beverage.getName());
			return div;
		}).setHeader("Name").setSortProperty("name");
		grid.addColumn(TemplateRenderer
				.<Beverage>of("<div style='display: flex; flex-direction: column; align-items: flex-start;'>"
						+ "<button on-click='handleEdit'>Edit</button>"
						+ "<button on-click='handleRemove'>Remove</button>"
						+ "<template is='dom-if' if='[[item.isFromGame]]'>Recommended for gamers.</template>"
						+ "</div>")
				.withProperty("isFromGame", b -> Origin.GAME.equals(b.getOrigin()))
				.withEventHandler("handleEdit", binder::setBean)
				.withEventHandler("handleRemove", beverage -> delete(beverage))).setHeader("Actions").setWidth("200px");

		// Define callbacks for Spring Data
		FetchCallback<Beverage, Object> fetchCallback = query -> repo.findAll(getSortingFromQuery(query)).stream()
				.skip(query.getOffset()).limit(query.getLimit());
		CountCallback<Beverage, Object> countCallback = query -> (int) repo.count();
		ValueProvider<Beverage, Object> idProvider = Beverage::getId;

		grid.setDataProvider(new CallbackDataProvider<>(fetchCallback, countCallback, idProvider));

		// Save functionality and Binder features
		saveButton.addClickListener(e -> {
			if (binder.getBean() != null) {
				save(binder.getBean());
			}
		});
		binder.bind(nameEditor, Beverage::getName, Beverage::setName);

		// Create a shopping cart and ability to display its contents
		ShoppingCart shoppingCart = new ShoppingCart();
		showButton.addClickListener(e -> new ShoppingCartDialog(shoppingCart));

		// Bind multi selection from Grid to the ShoppingCart
		Binder<ShoppingCart> shoppingCartBinder = new Binder<>();
		grid.setSelectionMode(SelectionMode.MULTI);
		shoppingCartBinder.bind(grid.asMultiSelect(), ShoppingCart::getBeverages, ShoppingCart::setBeverages);
		shoppingCartBinder.setBean(shoppingCart);

		add(grid, new HorizontalLayout(nameEditor, saveButton), showButton);
	}

	// Helpers methods for handling Spring Data
	private void save(Beverage beverage) {
		grid.getDataProvider().refreshItem(repo.save(beverage));
	}

	private void delete(Beverage beverage) {
		repo.delete(beverage);
		grid.getDataProvider().refreshAll();
	}

	private static Sort getSortingFromQuery(Query<?, ?> query) {
		return Sort.by(query.getSortOrders().stream()
				.map(o -> SortDirection.ASCENDING.equals(o.getDirection()) ? Order.asc(o.getSorted())
						: Order.desc(o.getSorted()))
				.toArray(Order[]::new));
	}
}
