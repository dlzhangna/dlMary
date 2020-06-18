package com.bjyt.springcloud.writer;

import org.springframework.batch.item.file.transform.LineAggregator;

import com.bjyt.springcloud.entity.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomerLineAggregator implements LineAggregator<Customer> {
    //JSON
    private ObjectMapper mapper = new ObjectMapper();
 
    @Override
    public String aggregate(Customer customer) {
 
        try {
            return mapper.writeValueAsString(customer);
        } catch (JsonProcessingException e) {
           throw new RuntimeException("Unable to serialize.",e);
        }
    }
}