package com.bjyt.springcloud.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.bjyt.springcloud.entity.Customer;

@Component
public class FirstNameUpperCaseProcessor implements ItemProcessor<Customer,Customer>{

@Override
public Customer process(Customer item) throws Exception{
	return new Customer(item.getId(),item.getFirstName().toUpperCase(),item.getLastName(),item.getBirthdate());
  }
}
