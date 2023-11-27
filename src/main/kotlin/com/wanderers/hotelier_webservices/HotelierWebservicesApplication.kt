package com.wanderers.hotelier_webservices;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator

@ComponentScan(	basePackages = ["com.wanderers.hotelier_webservices.rest", "com.wanderers.hotelier_webservices.server", "com.wanderers.hotelier_webservices.config"])
@SpringBootApplication(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator::class)
class HotelierWebservicesApplication

fun main(args: Array<String>) {
	runApplication<HotelierWebservicesApplication>(*args)
}