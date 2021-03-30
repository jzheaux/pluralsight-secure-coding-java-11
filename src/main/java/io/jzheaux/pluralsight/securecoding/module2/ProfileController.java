package io.jzheaux.pluralsight.securecoding.module2;

public class ProfileController {
	public Person updateProfile(Person p, String name, int age) {
		if (age < 13 || age > 120) {
			throw new IllegalArgumentException("invalid age");
		}
		p.name = name;
		p.age = age;
		return p;
	}
}
