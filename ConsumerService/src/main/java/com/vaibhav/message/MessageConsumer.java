package com.vaibhav.message;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.vaibhav.constant.ServiceConstants;
import com.vaibhav.vo.Employee;

@Component
public class MessageConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

	public void receiveMessage() throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(ServiceConstants.HOST_NAME);
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(ServiceConstants.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, ServiceConstants.EXCHANGE_NAME, ServiceConstants.ABOVE_30);
		channel.queueBind(queueName, ServiceConstants.EXCHANGE_NAME, ServiceConstants.BELOW_30);
		LOGGER.info("[*] Waiting for messages");
		DeliverCallback callback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			Employee employee = new ObjectMapper().readValue(message, Employee.class);
			LOGGER.info("[*] Received Employee details - RoutingKey[{}] - Details[{}]",
					delivery.getEnvelope().getRoutingKey(), employee);
			try {
				if (delivery.getEnvelope().getRoutingKey().equals(ServiceConstants.ABOVE_30)) {
					processAboveAge30Data(employee);
				} else {
					processBelowAge30Data(employee);
				}
			} catch (Exception e) {
				LOGGER.info("[*] Done");
			} finally {

			}
		};
		boolean autoAck = true;
		channel.basicConsume(queueName, autoAck, callback, consumerTag -> {
		});

	}

	public void processAboveAge30Data(Employee employee) throws InterruptedException {
		// just a small mimic of business processing
		Thread.sleep(1000);
	}

	public void processBelowAge30Data(Employee employee) throws InterruptedException {
		// just a small mimic of business processing
		Thread.sleep(1000);
	}

}
