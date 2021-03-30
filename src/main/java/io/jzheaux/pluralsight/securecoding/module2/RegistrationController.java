package io.jzheaux.pluralsight.securecoding.module2;

public class RegistrationController {
	public Person createPerson(String name, int age) {
		if (age < 13 || age > 120) {
			throw new IllegalArgumentException("invalid age");
		}
		Person p = new Person();
		p.name = name;
		p.age = age;
		return p;
	}
}
