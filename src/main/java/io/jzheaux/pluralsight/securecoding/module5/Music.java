package io.jzheaux.pluralsight.securecoding.module5;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity(name="music")
public class Music {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length=64)
	private String title;

	@Column
	private String filename;

	Music() {}

	Music(String title, String filename) {
		this.title = title;
		this.filename = filename;
	}

	public String getTitle() {
		return title;
	}
}
