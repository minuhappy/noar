package com.noar.laboratory;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
@ComponentScan(basePackages = { "com.noar.*" })
public class ApplicationMain {
	public static void main(String[] args) {
		SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(ApplicationMain.class);

		// 서버 구동 후 Application Context 객체 반환.
		ConfigurableApplicationContext configurableApplicationContext = springApplicationBuilder.run(args);

		Environment env = configurableApplicationContext.getBean(Environment.class);
		System.out.println(env.getClass().getSimpleName());
		
	}
}