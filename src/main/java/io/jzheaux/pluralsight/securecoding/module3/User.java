package io.jzheaux.pluralsight.securecoding.module3;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.ObjectStreamField;
import java.io.Serializable;

@Entity(name="users")
public class User implements Serializable {
	@Id
	private String id;

	private String email;

	@Column(length=16)
	private String password;

	private String fullName;

	private String photoFile;

	User() {}

	public User(String id, String email, String password, String fullName, String photoFile) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.fullName = fullName;
		this.photoFile = photoFile;
	}

	public User(User user) {
		this.id = user.id;
		this.email = user.email;
		this.fullName = user.fullName;
		this.photoFile = user.photoFile;
	}

	String getPassword() {
		return password;
	}

	public String getFullName() {
		return fullName;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", fullName='" + fullName + '\'' +
				", photoFile='" + photoFile + '\'' +
				'}';
	}
}
