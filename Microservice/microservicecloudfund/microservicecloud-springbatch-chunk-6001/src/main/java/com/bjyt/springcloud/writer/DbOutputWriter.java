package com.bjyt.springcloud.writer;

import javax.sql.DataSource;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.bjyt.springcloud.entity.Customer;

@Configuration
public class DbOutputWriter{
    
	@Autowired
	public DataSource dataSource;
	
	@Bean
	public JdbcBatchItemWriter<Customer> dbCustomerOutputFlatFileWriter() {
		JdbcBatchItemWriter<Customer> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(dataSource);
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		itemWriter.setSql("insert into customer(firstName,lastName,birthdate) values (:firstName,:lastName,:birthdate)");
	    return itemWriter;
	}
}
