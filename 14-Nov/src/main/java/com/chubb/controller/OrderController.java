package com.chubb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chubb.request.Order1;
import com.chubb.service.OrderService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
public class OrderController { 
	@Autowired
	OrderService service;
	@GetMapping("/order") 
	String getOrder() {
		return "hello";
	}
	@PostMapping("/order") 
	Order1 saveOrder(@RequestBody @Valid Order1 order) {
		log.debug("logger added");
		service.insertOrder(order);
		return order;
	}
}