package com.vaibhav;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaibhav.message.MessageConsumer;

@SpringBootApplication
public class ConsumerServiceApplication implements CommandLineRunner{

	@Autowired
	MessageConsumer messageConsumer;
	
	public static void main(String[] args) {
		SpringApplication.run(ConsumerServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		messageConsumer.receiveMessage();
	}

}
