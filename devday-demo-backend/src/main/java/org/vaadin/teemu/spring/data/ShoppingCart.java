package org.vaadin.teemu.spring.data;

import java.util.Collections;
import java.util.Set;

/**
 * Shared ShoppingCart object for storing selected beverages and displaying
 * them.
 */
public class ShoppingCart {
	private Set<Beverage> beverages = Collections.emptySet();

	public Set<Beverage> getBeverages() {
		return beverages;
	}

	public void setBeverages(Set<Beverage> beverages) {
		this.beverages = beverages;
	}
}