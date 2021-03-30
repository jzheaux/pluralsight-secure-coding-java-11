package io.jzheaux.pluralsight.securecoding.module4;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

@Component("module4AppInitializer")
public class AppInitializer implements SmartInitializingSingleton {
	private final MessageRepository messages;

	public AppInitializer(MessageRepository messages) {
		this.messages = messages;
	}

	@Override
	public void afterSingletonsInstantiated() {
		this.messages.save(new Message(1L, "hello, Chuck Norris!"));
		this.messages.save(new Message(1L, "give me liberty, or give me death! - Chuck Norris"));
		this.messages.save(new Message(1L, "Chuck Norris likes trains"));
	}
}
