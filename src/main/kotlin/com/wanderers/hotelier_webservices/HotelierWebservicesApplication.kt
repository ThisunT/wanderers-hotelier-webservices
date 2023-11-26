package com.wanderers.hotelier_webservices

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator

@SpringBootApplication(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator::class)
@ComponentScan(
	basePackages = ["com.wanderers.hotelier_webservices.rest", "com.wanderers.hotelier_webservices.server", "com.wanderers.hotelier_webservices.config"],
	nameGenerator = FullyQualifiedAnnotationBeanNameGenerator::class
)
object HotelierWebservicesApplication {
	@kotlin.jvm.JvmStatic
	fun main(args: Array<String>) {
		SpringApplication.run(HotelierWebservicesApplication::class.java, *args)
	}
}