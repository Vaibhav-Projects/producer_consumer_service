package com.vaibhav.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaibhav.constant.ServiceConstants;
import com.vaibhav.vo.Employee;
import com.vaibhav.vo.Employees;

@Component
public class MessageService {

	@Autowired
	RestTemplate restTemplate;

	ObjectMapper mapper;

	public List<Employee> getAllEmployeeDetails() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
		Employees employees = restTemplate
				.exchange(ServiceConstants.API_URL, HttpMethod.GET, requestEntity, Employees.class).getBody();
		System.out.println(employees);
		return employees.getData();
	}
}
