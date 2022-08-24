package org.gihan.integration;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.http.config.EnableIntegrationGraphController;

@EnableDiscoveryClient
@SpringBootApplication
@EnableIntegrationGraphController(allowedOrigins = "*")
@ComponentScan({"org.codeobe.integration","org.gihan.integration"})
public class HrServiceApplication {


	
	public static void main(String[] args) {
		SpringApplication.run(HrServiceApplication.class, args);
	}
}

