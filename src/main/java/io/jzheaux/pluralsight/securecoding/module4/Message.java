package io.jzheaux.pluralsight.securecoding.module4;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.ObjectStreamField;
import java.io.Serializable;

@Entity(name="messages")
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long forumId;

	@Column(length=2048)
	private String text;

	Message() {}

	public Message(Long forumId, String text) {
		this.forumId = forumId;
		this.text = text;
	}

	Long getForumId() {
		return forumId;
	}

	String getText() {
		return text;
	}
}
