package com.bjyt.springcloud.reader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.bjyt.springcloud.entity.Customer;

@Component
public class FlatFileInputToDBReader{
    
	@Bean
	public FlatFileItemReader<Customer> flatFileCustomerInputToDBReader() {
		FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("customerInputToDB.csv"));
		reader.setLinesToSkip(1);
		
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] {"firstName","lastName","birthdate"});
		
		DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
		lineMapper.setLineTokenizer(tokenizer);
		lineMapper.setFieldSetMapper((fieldSet->{
			return Customer.builder()
				   .firstName(fieldSet.readString("firstName"))
				   .lastName(fieldSet.readString("lastName"))
				   .birthdate(fieldSet.readString("birthdate"))
				   .build();
		}));
		
		lineMapper.afterPropertiesSet();
		reader.setLineMapper(lineMapper);
		return reader;
	}
}
