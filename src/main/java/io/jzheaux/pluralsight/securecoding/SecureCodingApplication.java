package io.jzheaux.pluralsight.securecoding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ForwardedHeaderFilter;

@SpringBootApplication
public class SecureCodingApplication {

	@Bean
	ForwardedHeaderFilter forwardedHeaderFilter() {
		return new ForwardedHeaderFilter();
	}

	public static void main(String[] args) {
		SpringApplication.run(SecureCodingApplication.class, args);
	}

}
