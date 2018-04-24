package org.vaadin.teemu.spring.data;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JpaRepository for storing Beverages.
 */
public interface BeverageRepository extends JpaRepository<Beverage, Long> {
}
