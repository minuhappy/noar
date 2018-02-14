package com.noar.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ComponentScan(basePackages = { "com.noar.*" })
@ImportResource({ "classpath:/WEB-INF/application-context.xml", "classpath:/WEB-INF/dataSource-context.xml" })
public class ApplicationMain {
	public static void main(String[] args) {
		SpringApplication.run(ApplicationMain.class, args);
	}
}