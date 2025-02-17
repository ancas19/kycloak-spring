package co.com.api.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = "co.com.api.rest")
@EnableConfigurationProperties
public class SpringBootKeycloakApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootKeycloakApplication.class, args);
	}

}
