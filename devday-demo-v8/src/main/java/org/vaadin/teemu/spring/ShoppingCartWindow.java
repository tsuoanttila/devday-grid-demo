package org.vaadin.teemu.spring;

import org.vaadin.teemu.spring.data.Beverage;
import org.vaadin.teemu.spring.data.ShoppingCart;

import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ShoppingCartWindow extends Window {

	public ShoppingCartWindow(ShoppingCart cart) {
		VerticalLayout windowContent = new VerticalLayout();
		setContent(windowContent);
		for (Beverage beverage : cart.getBeverages()) {
			Label c = new Label(beverage.getName());
			c.addStyleName(beverage.getOrigin().name().toLowerCase());
			windowContent.addComponent(c);
		}
		setSizeUndefined();
		setCaption("Shopping Cart");
		setModal(true);
		UI.getCurrent().addWindow(this);
	}
}
