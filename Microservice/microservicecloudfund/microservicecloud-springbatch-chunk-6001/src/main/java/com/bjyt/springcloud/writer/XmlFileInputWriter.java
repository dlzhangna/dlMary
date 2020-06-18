package com.bjyt.springcloud.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.bjyt.springcloud.entity.Customer;

@Component("xmlFileInputWriter")
public class XmlFileInputWriter implements ItemWriter<Customer>{
	@Override
	public void write(List items) throws Exception {
		for(int i =0;i<items.size();i++) {
			System.out.println(items.get(i));
		}
	}
}
