package io.jzheaux.pluralsight.securecoding.module2;

import java.util.HashMap;
import java.util.Map;

public class PersonService {
	private final Map<String, Person> people = new HashMap<>();

	public Person addPerson(String name, int age) {
		return this.people.computeIfAbsent(name, (k) -> new Person());
	}

	public Person removePerson(String name) {
		return this.people.remove(name);
	}
}
