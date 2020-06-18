package com.bjyt.springcloud.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.bjyt.springcloud.entity.Customer;

@Component("customerWriter")
public class CustomerWriter implements ItemWriter<Customer>{

	@Override
	public void write(List<? extends Customer> items) throws Exception {
		for(Customer customer:items)
			System.out.println(customer);
	}

 
}
