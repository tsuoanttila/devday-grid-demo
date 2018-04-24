package org.vaadin.teemu.spring.data;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Class for generating development time test data for Beverage online store.
 */
@Component
public class TestDataGenerator implements CommandLineRunner {

	@Autowired
	BeverageRepository repository;

	@Override
	public void run(String... args) throws Exception {
		Stream.of(
				Stream.of("Slusho!", "Fizzy Bubblech", "Dark Planet Cola", "Cadre Cola", "Buzzz Cola", "Botijola",
						"Blue milk", "Adrenalode").map(name -> new Beverage(name, Origin.FILM)),
				Stream.of("Sprünt", "Slurm", "Killer Shrew", "Buzz Cola").map(name -> new Beverage(name, Origin.TV)),
				Stream.of("Nuka-Cola").map(name -> new Beverage(name, Origin.GAME))).reduce(Stream::concat).get()
				.forEach(repository::save);
	}
}
