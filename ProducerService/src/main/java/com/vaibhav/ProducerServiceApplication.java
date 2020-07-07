package com.vaibhav;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.vaibhav.message.MessageProducer;

@SpringBootApplication
public class ProducerServiceApplication implements CommandLineRunner {

	@Autowired
	MessageProducer messageProducer;

	public static void main(String[] args) {
		SpringApplication.run(ProducerServiceApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Override
	public void run(String... args) throws Exception {
		messageProducer.sendMessage();
	}

}
