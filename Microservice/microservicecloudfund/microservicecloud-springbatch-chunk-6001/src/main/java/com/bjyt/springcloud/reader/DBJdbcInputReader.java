package com.bjyt.springcloud.reader;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import com.bjyt.springcloud.entity.Customer;

@Component
public class DBJdbcInputReader{
	
	@Autowired
	private DataSource dataSource;
    
	@Bean
	public JdbcPagingItemReader<Customer> dbJdbcCustomerInputReader() {
		JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();
		reader.setDataSource(this.dataSource);
		reader.setFetchSize(100);
		reader.setRowMapper((rs,rowNum)->{
			return Customer.builder().id(rs.getInt("id"))
					                       .firstName(rs.getString("firstName"))
					                       .lastName(rs.getString("lastName"))
					                       .birthdate(rs.getString("birthdate"))
					                       .build();
		});
		
		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("id,firstName,lastName,birthdate");
		queryProvider.setFromClause("FROM customer");
		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("ID", Order.ASCENDING);
		queryProvider.setSortKeys(sortKeys);
		reader.setQueryProvider(queryProvider);
		
		return reader;
	}
}
