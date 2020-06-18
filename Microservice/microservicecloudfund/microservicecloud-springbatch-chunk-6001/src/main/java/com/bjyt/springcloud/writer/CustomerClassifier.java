package com.bjyt.springcloud.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

import com.bjyt.springcloud.entity.Customer;

public class CustomerClassifier implements Classifier<Customer, ItemWriter<? super Customer>> {
	 
    /**
	 * 
	 */
	private static final long serialVersionUID = -8551104731498027929L;
	private ItemWriter<Customer> xmlWriter;
    private ItemWriter<Customer> jsonWriter;
 
    public CustomerClassifier(ItemWriter<Customer> xmlWriter, ItemWriter<Customer>
            jsonWriter) {
        this.xmlWriter = xmlWriter;
        this.jsonWriter = jsonWriter;
 
    }
 
    @Override
    public ItemWriter<? super Customer> classify(Customer item) {
 
        return item.getId()%2 == 0 ? xmlWriter:jsonWriter;
    }
}
