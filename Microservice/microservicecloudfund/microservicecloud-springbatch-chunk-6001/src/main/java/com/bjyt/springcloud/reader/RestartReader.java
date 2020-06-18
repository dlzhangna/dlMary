package com.bjyt.springcloud.reader;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.bjyt.springcloud.entity.Customer;

@Component("restartReader")
public class RestartReader implements ItemStreamReader<Customer>{
   
	private Long curLine = 0L;
	private boolean restart = false;
	
	private FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
	
	private ExecutionContext executionContext;
	
	public RestartReader() {
		reader.setResource(new ClassPathResource("ReStartCustomerInput.csv"));
        reader.setLinesToSkip(1);
		
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] {"id","firstName","lastName","birthdate"});
		
		DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
		lineMapper.setLineTokenizer(tokenizer);
		lineMapper.setFieldSetMapper((fieldSet->{
			return Customer.builder().id(fieldSet.readInt("id"))
				   .firstName(fieldSet.readString("firstName"))
				   .lastName(fieldSet.readString("lastName"))
				   .birthdate(fieldSet.readString("birthdate"))
				   .build();
		}));
		
		lineMapper.afterPropertiesSet();
		reader.setLineMapper(lineMapper);
	}
	
	
	@Override
	public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
//		System.out.println("invoke read method");
//		System.out.println("restartFlag:" + restart);
//		System.out.println("curLine in read method:" + curLine);
		Customer customer = null;
		this.curLine ++;
		if(restart) {
			reader.setLinesToSkip(this.curLine.intValue());
			restart = false;
			System.out.println("Start reading from Line: " + this.curLine);
		}
		
		reader.open(this.executionContext);
		customer = reader.read();
		
		if(customer != null) {
			if(customer.getFirstName().equals("WrongName"))
				throw new RuntimeException("Something wrong.Customer id: " + customer.getId());
		} else {
			curLine--;
		}
		return customer;
	}
	
	
	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		//System.out.println("invoke open method");
		this.executionContext = executionContext;
		//System.out.println("executionContext:" + executionContext.size());
//		if(executionContext.size()>0) {
//		System.out.println("executionContext:" + executionContext.getLong("curLine"));
//		System.out.println("executionContext.containsKey(\"curLine\"):" + executionContext.containsKey("curLine"));
//		}
		if(executionContext.size()>0 && executionContext.containsKey("curLine")) {
			this.curLine = executionContext.getLong("curLine");
			System.out.println("curLine from open method: " + curLine);
			this.restart = true;
		} else {
			this.curLine = 0L;
		}
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		System.out.println("update curLine: " + this.curLine);
		executionContext.put("curLine", this.curLine);
	}

	@Override
	public void close() throws ItemStreamException {
		// TODO Auto-generated method stub
		
	}

	

}
