package io.jzheaux.pluralsight.securecoding.module3;

import io.jzheaux.pluralsight.securecoding.module2.Person;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;

public class UserTest {
	@Test
	void testDateFormattingPlaceholders() {
		LocalDate registrationDate = LocalDate.now();
		LocalDate birthDate = LocalDate.of(1980, 1, 1);
		System.out.printf("Registered a %1$tB user on %s%n", birthDate, registrationDate);
		System.out.printf("Registered a %tB user on %s%n", birthDate, registrationDate);
	}

	@Test
	void testSerialization() throws IOException, ClassNotFoundException {
		User a = new User("id", "email", "password", "name", "filename");
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		try (ObjectOutputStream oos = new ObjectOutputStream(boas)) {
			oos.writeObject(a);
		}
		byte[] bytes = boas.toByteArray();
		try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
			User b = (User) ois.readObject();
			System.out.println(a);
			System.out.println(b);
		}
	}
}
