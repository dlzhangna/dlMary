package com.bjyt.flink.project.async;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import com.alibaba.druid.pool.DruidDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class AsyncMySQLRequest extends RichAsyncFunction<String,String>{
	
	private transient DruidDataSource dataSource;
	
	private transient ExecutorService executorService;
	
	public void open(Configuration parameters) throws Exception{
		executorService = Executors.newFixedThreadPool(20);
		
		dataSource = new DruidDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		dataSource.setUrl("jdbc:mysql://localhost:3306/fund1?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&verifyServerCertificate=false&useSSL=false&allowPublicKeyRetrieval=true");
		dataSource.setInitialSize(5);
		dataSource.setMinIdle(10);
		dataSource.setMaxActive(20);
	}

	public void close() throws Exception{
		dataSource.close();
		executorService.shutdown();
	}

	@Override
	public void asyncInvoke(String id, ResultFuture<String> resultFuture) throws Exception {
        System.out.println("id:" + id);
		Future<String> future = executorService.submit(()->{
			System.out.println("submit");
			return queryFromMySql(id);
		});
		
		CompletableFuture.supplyAsync(new Supplier<String>(){
			@Override
			public String get() {
				try {
					return future.get();
				}catch (Exception e) {
					return null;
				}
			}
		}).thenAccept((String dbResult)->{
			resultFuture.complete(Collections.singleton(dbResult));
		});
	}
	
	private String queryFromMySql(String param) throws SQLException{
		System.out.println("param:" + param);
		String sql = "SELECT id,name FROM t_activities WHERE id = ?";
		String result = null;
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			connection = dataSource.getConnection();
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, param);
			rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getString("name");
			}
			System.out.println("result:" + result);
		}finally {
			if(rs!=null) {
				rs.close();
			}
			if(stmt!=null) {
				stmt.close();
			}
			if(result !=null) {
				
			}
			return result;
		}
	}
}
