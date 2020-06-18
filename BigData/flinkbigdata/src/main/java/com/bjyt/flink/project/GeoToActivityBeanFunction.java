package com.bjyt.flink.project;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjyt.flink.project.bean.ActivityBean;

public class GeoToActivityBeanFunction extends RichMapFunction<String, ActivityBean>{
	
	private CloseableHttpClient httpClient = null;
	
	@Override
	public void open(Configuration parameters) throws Exception{
		super.open(parameters);
		httpClient = HttpClients.createDefault();
	}
	

	@Override
	public ActivityBean map(String line) throws Exception {
		String[] fields = line.split(",");
		String uid = fields[0];
		String aid = fields[1];
		String time = fields[2];
		int eventType = Integer.parseInt(fields[3]);
		String longitude = fields[4];
		String latitude = fields[5];
		String url = "https://restapi.amap.com/v3/geocode/regeo?key=f68ad64351432be46a8029cd4ee4a41b&location=" + longitude + "," + latitude;
		HttpGet httpGet = new HttpGet(url);
		//block, no Async, if execute doesn't complete, won't next command
	    CloseableHttpResponse response = httpClient.execute(httpGet);
	    String province = null;
	    try {
	    	int status = response.getStatusLine().getStatusCode();
	    	if(status == 200) {
	    		String result = EntityUtils.toString(response.getEntity());
	    		System.out.println(result);
	    		JSONObject jsonObj = JSON.parseObject(result);
	    		JSONObject regeocode = jsonObj.getJSONObject("regeocode");
	    		if(regeocode!=null && !regeocode.isEmpty()) {
	    			JSONObject address = regeocode.getJSONObject("addressComponent");
	    			province = address.getString("province");
	    			String city = address.getString("city");
	    			String businessAreas = address.getString("businessAreas");
	    		}
	    	}
	    	System.out.println("province:" + province);
	    } finally {
	    	response.close();
	    }
		return ActivityBean.of(uid, aid,"",time,eventType,province,Double.parseDouble(longitude),Double.parseDouble(latitude));
	}
	
	@Override
	public void close() throws Exception{
		super.close();
	}

}
