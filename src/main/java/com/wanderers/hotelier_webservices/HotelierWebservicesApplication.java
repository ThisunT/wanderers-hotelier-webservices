package com.wanderers.hotelier_webservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication(
		nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@ComponentScan(
		basePackages = {"com.wanderers.hotelier_webservices.rest", "com.wanderers.hotelier_webservices.server", "com.wanderers.hotelier_webservices.config", "com.wanderers.hotelier_webservices.mapper"},
		nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class HotelierWebservicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelierWebservicesApplication.class, args);
	}

}
