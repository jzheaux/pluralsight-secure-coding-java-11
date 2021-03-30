package io.jzheaux.pluralsight.securecoding.module3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.UUID;

@Service
public class UserService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final UserRepository users;

	public UserService(UserRepository users) {
		this.users = users;
	}

	public User create(String email, String password, String fullName, String imageLocation) {
		String id = UUID.randomUUID().toString().substring(19);
		User user = new User(id, email, password, fullName, imageLocation);
		try {
			return new User(this.users.save(user));
		} catch (RuntimeException e) {
			logger.error("Failed to register user " + user, e);
			return null;
		}
	}

	public User lookup(String email, String password) {
		User user = this.users.findByEmail(email);
		if (user == null) {
			return null;
		}
		if (user.getPassword().equals(password)) {
			return new User(user);
		}
		return null;
	}
}
