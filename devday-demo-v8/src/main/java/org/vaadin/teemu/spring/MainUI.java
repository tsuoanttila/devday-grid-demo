package org.vaadin.teemu.spring;

import com.vaadin.annotations.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.vaadin.teemu.spring.data.Beverage;
import org.vaadin.teemu.spring.data.BeverageRepository;
import org.vaadin.teemu.spring.data.Origin;
import org.vaadin.teemu.spring.data.ShoppingCart;

import com.vaadin.data.Binder;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.CallbackDataProvider.CountCallback;
import com.vaadin.data.provider.CallbackDataProvider.FetchCallback;
import com.vaadin.data.provider.Query;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
@Title("Vaadin 8 Demo")
public class MainUI extends UI {

	private Binder<Beverage> binder = new Binder<>();
	private Grid<Beverage> grid = new Grid<>();
	private TextField nameEditor = new TextField();
	private Button saveButton = new Button("Save");
	private Button showButton = new Button("Show");

	@Autowired
	BeverageRepository repo;

	@Override
	protected void init(VaadinRequest request) {
		// Inject styles in a not-so-neat way..
		Page.getCurrent().getStyles().add(".tv { background-color: lightblue !important; }");
		Page.getCurrent().getStyles().add(".game { background-color: lightgreen !important; }");
		Page.getCurrent().getStyles().add(".film { background-color: yellow !important; }");

		VerticalLayout content = new VerticalLayout();
		setContent(content);

		content.addComponent(new Label("DevDay Grid Demo for Vaadin 8"));

		grid.addColumn(Beverage::getName).setStyleGenerator(b -> b.getOrigin().name().toLowerCase())
				.setSortProperty("name").setCaption("Name");
		grid.addComponentColumn(beverage -> {
			VerticalLayout layout = new VerticalLayout();

			// Make layout take a bit less space
			layout.setMargin(false);
			layout.setSpacing(false);

			layout.addComponent(new Button("Edit", e -> binder.setBean(beverage)));
			layout.addComponent(new Button("Remove", e -> delete(beverage)));
			if (Origin.GAME.equals(beverage.getOrigin())) {
				layout.addComponent(new Label("Recommended for gamers."));
			}
			return layout;
		});

		// Make Grid display all the things
		grid.setBodyRowHeight(100);
		grid.setSizeFull();

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
		showButton.addClickListener(e -> new ShoppingCartWindow(shoppingCart));

		// Bind multi selection from Grid to the ShoppingCart
		grid.setSelectionMode(SelectionMode.MULTI);
		Binder<ShoppingCart> shoppingCartBinder = new Binder<>();
		shoppingCartBinder.bind(grid.asMultiSelect(), ShoppingCart::getBeverages, ShoppingCart::setBeverages);
		shoppingCartBinder.setBean(shoppingCart);

		content.addComponents(grid, new HorizontalLayout(nameEditor, saveButton), showButton);
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
