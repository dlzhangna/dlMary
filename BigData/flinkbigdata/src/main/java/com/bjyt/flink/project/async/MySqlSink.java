package com.bjyt.flink.project.async;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import com.bjyt.flink.project.bean.ActivityBean;

public class MySqlSink extends RichSinkFunction<ActivityBean>{
  private transient Connection connection = null;
  public void open(Configuration parameters) throws Exception{
//	  super.open(parameters);
//	  Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fund1?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&verifyServerCertificate=false&useSSL=false&allowPublicKeyRetrieval=true", "root", "root");
  }
  public void invoke(ActivityBean bean,Context context) throws Exception{
	    PreparedStatement pstm = null;
	  try {
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fund1?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&verifyServerCertificate=false&useSSL=false&allowPublicKeyRetrieval=true", "root", "root");
	    pstm = connection.prepareStatement("INSERT INTO t_activity_count(aid,event_type,counts) values (?,?,?) ON DUPLICATE KEY UPDATE counts = ?");
	    pstm.setString(1, bean.aid);
	    pstm.setInt(2, bean.eventType);
	    pstm.setInt(3, bean.count);
	    pstm.setInt(4, bean.count);
		int result = pstm.executeUpdate();
	} finally {
		if(pstm!=null) {
			pstm.close();
		}
	}
 }
  public void close() throws Exception{
	  super.close();
  }
  
}
