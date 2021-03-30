package io.jzheaux.pluralsight.securecoding.module3;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

@Component
public class AppInitializer implements SmartInitializingSingleton {
	private final UserService users;

	public AppInitializer(UserService users) {
		this.users = users;
	}

	@Override
	public void afterSingletonsInstantiated() {
		this.users.create("bob@jzheaux.io", "hipp0cats!!", "Bob", "bob.jpg'}\r\njava.lang.OutOfMemoryError: Java heap space\r\n2021-02-12 14:58:41.274 ERROR 6852 --- [           main] i.j.p.securecoding.module3.UserService   : Failed to register user User{id=a138-27783993e9fb, email='bob@jzheaux.io', password='hipp0cats!!!!!!!!!!!', fullName='Bob', photoFile='bob.jpg");
		this.users.create("josh@jzheaux.io", "ilovehipp0cats!!!!", "Josh", "josh.jpg");
		this.users.create("josh@jzheaux.io", "ilovehipp0cats!!!", "Josh", "josh.jpg");
		this.users.create("josh@jzheaux.io", "ilovehipp0cats!!", "Josh", "josh.jpg");
		this.users.create("mary@jzheaux.io", "hipp0cats", "Mary", "mary.jpg");
		this.users.create("cal@jzheaux.io", "hipp0cats", "Callan", "mary.jpg");
	}
}
