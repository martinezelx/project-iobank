package io.builders.iobank;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author martinezelx
 * @since 0.0.1
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "ioBank", version = "1.0", description = "Rest API to simulate a small bank"))
public class IoBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(IoBankApplication.class, args);
	}
}