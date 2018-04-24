package org.vaadin.teemu.spring.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Bevergae entity. Class for storing Beverage information for online store.
 */
@Entity
public class Beverage {

	@Id
	@GeneratedValue
	private Long id;

	private String name;
	private Origin origin;

	public Beverage() {
	}

	public Beverage(String name, Origin origin) {
		this.name = name;
		this.origin = origin;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Origin getOrigin() {
		return origin;
	}

	public void setOrigin(Origin origin) {
		this.origin = origin;
	}

	public Long getId() {
		return id;
	}
}
