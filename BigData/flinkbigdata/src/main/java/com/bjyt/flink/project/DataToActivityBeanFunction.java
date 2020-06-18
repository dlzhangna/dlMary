package com.bjyt.flink.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

import com.bjyt.flink.project.bean.ActivityBean;

public class DataToActivityBeanFunction extends RichMapFunction<String, ActivityBean> {

	private transient Connection connection = null;

	@Override
	public void open(Configuration parameters) throws Exception {
		//super.open(parameters);
//		Class.forName("com.mysql.jdbc.Driver").newInstance();
//		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fund1?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&verifyServerCertificate=false&useSSL=false&allowPublicKeyRetrieval=true", "root", "root");
//	    System.out.println("connection:" + connection);
	}

	@Override
	public ActivityBean map(String line) throws Exception {
//		System.out.println("map method");
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fund1?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&verifyServerCertificate=false&useSSL=false&allowPublicKeyRetrieval=true", "root", "root");
//	    System.out.println("connection:" + connection);
		String[] fields = line.split(",");
		String uid = fields[0];
		String aid = fields[1];
//		System.out.println("uid:" + uid);
//		System.out.println("aid:" + aid);
		// Search name by the aid
		PreparedStatement prepareStatement = connection.prepareStatement("SELECT name FROM t_activities WHERE id = ?");
		prepareStatement.setString(1, aid);
		ResultSet resultSet = prepareStatement.executeQuery();
		String name = "";
		while (resultSet.next()) {
			name = resultSet.getString(1);
		}
//		System.out.println("name:" + name);
		prepareStatement.close();
		String time = fields[2];
//		System.out.println("time:" + time);
		int eventType = Integer.parseInt(fields[3]);
		String province = fields[4];
		return ActivityBean.of(uid, aid, name, time, eventType, province);
	}

	@Override
	public void close() throws Exception {
		super.close();
		connection.close();
	}
}