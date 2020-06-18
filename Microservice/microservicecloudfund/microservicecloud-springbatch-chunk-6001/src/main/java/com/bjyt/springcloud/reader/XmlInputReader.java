package com.bjyt.springcloud.reader;

import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;
import com.bjyt.springcloud.entity.Customer;

@Component
public class XmlInputReader{
    
	@Bean
	public StaxEventItemReader<Customer> xmlCustomerInputReader() {
		StaxEventItemReader<Customer> reader = new StaxEventItemReader<>();
		 
        reader.setResource(new ClassPathResource("customerInput.xml"));
        reader.setFragmentRootElementName("customer");
 
 
        XStreamMarshaller unMarshaller = new XStreamMarshaller();
        Map<String,Object> map = new HashMap<>();
        map.put("customer",Customer.class);
        try {
			unMarshaller.setAliases(map);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        reader.setUnmarshaller(unMarshaller);
 
        return reader;
	}
}
