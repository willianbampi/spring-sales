package com.next.ecommerce.sales;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SalesApplication {
	
	@Value("${application.name}")
	private String applicationName;

	@Autowired
	@Qualifier(value = "applicationMessage")
	private String applicationMessage;
	
	@GetMapping("/hello")
	public String hello() {
		return applicationName + " " + applicationMessage;
	}
	public static void main(String[] args) {
		SpringApplication.run(SalesApplication.class, args);
	}

}
