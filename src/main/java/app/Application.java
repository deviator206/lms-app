package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import config.FileStorageProperties;

@SpringBootApplication
@ComponentScan(basePackages = { "controller", "repository", "service", "security", "config" })
/*
 * public class Application {
 * 
 * public static void main(String[] args) {
 * SpringApplication.run(Application.class, args); }
 * 
 * 
 * }
 */

public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

}
