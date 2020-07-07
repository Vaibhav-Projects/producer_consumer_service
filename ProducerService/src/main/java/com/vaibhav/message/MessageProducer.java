package com.vaibhav.message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.vaibhav.constant.ServiceConstants;
import com.vaibhav.service.MessageService;
import com.vaibhav.vo.Employee;

@Component
public class MessageProducer {

	@Autowired
	MessageService messageService;

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageProducer.class);

	public void sendMessage() throws IOException, TimeoutException, InterruptedException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(ServiceConstants.HOST_NAME);
		try (Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
			channel.exchangeDeclare(ServiceConstants.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
			channel.confirmSelect();
			List<Employee> employees = messageService.getAllEmployeeDetails();
			int batchSize = ServiceConstants.BATCH_SIZE;
			int outstandingMessageCount = 0;
			for (Employee employee : employees) {
				if (Integer.valueOf(employee.getEmployeeAge()) > 30) {
					channel.basicPublish(ServiceConstants.EXCHANGE_NAME, ServiceConstants.ABOVE_30,
							MessageProperties.PERSISTENT_TEXT_PLAIN,
							employee.toString().getBytes(StandardCharsets.UTF_8));
				}
				if (Integer.valueOf(employee.getEmployeeAge()) < 30) {
					channel.basicPublish(ServiceConstants.EXCHANGE_NAME, ServiceConstants.BELOW_30,
							MessageProperties.PERSISTENT_TEXT_PLAIN,
							employee.toString().getBytes(StandardCharsets.UTF_8));
				}
				outstandingMessageCount++;
				if (outstandingMessageCount == batchSize) {
					channel.waitForConfirmsOrDie(5_0000);
					outstandingMessageCount = 0;
				}
				LOGGER.info("[*] Sending Employee Detail - {}", employee);
			}

		}
	}
}
