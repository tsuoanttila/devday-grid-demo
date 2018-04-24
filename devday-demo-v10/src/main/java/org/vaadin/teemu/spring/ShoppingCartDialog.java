package org.vaadin.teemu.spring;

import org.vaadin.teemu.spring.data.Beverage;
import org.vaadin.teemu.spring.data.ShoppingCart;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ShoppingCartDialog extends Dialog {

	public ShoppingCartDialog(ShoppingCart cart) {
		add(new Label("Shopping Cart"));
		VerticalLayout layout = new VerticalLayout();
		for (Beverage beverage : cart.getBeverages()) {
			Label c = new Label(beverage.getName());
			c.addClassName(beverage.getOrigin().name().toLowerCase());
			layout.add(c);
		}
		add(layout);
		open();
	}
}
