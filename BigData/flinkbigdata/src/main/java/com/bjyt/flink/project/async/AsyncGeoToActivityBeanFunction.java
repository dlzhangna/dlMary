package com.bjyt.flink.project.async;

import java.util.concurrent.CompletableFuture;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjyt.flink.project.bean.ActivityBean;

import java.util.Collections;
import java.util.concurrent.Future;
import java.util.function.Supplier;

public class AsyncGeoToActivityBeanFunction extends RichAsyncFunction<String,ActivityBean>{

	private transient CloseableHttpAsyncClient httpClient = null;
	
	@Override
	public void open(Configuration parameters) throws Exception{
		super.open(parameters);
//		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3000).build();
//		CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom().setMaxConnTotal(20).setDefaultRequestConfig(requestConfig).build();
//		httpClient.start();
	}
	
	@Override
	public void asyncInvoke(String line, ResultFuture<ActivityBean> resultFuture) throws Exception {
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3000).build();
		CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom().setMaxConnTotal(20).setDefaultRequestConfig(requestConfig).build();
		httpClient.start();
		String[] fields = line.split(",");
		String uid = fields[0];
		String aid = fields[1];
		String time = fields[2];
		int eventType = Integer.parseInt(fields[3]);
		String longitude = fields[4];
		String latitude = fields[5];
		String url = "https://restapi.amap.com/v3/geocode/regeo?key=f68ad64351432be46a8029cd4ee4a41b&location=" + longitude + "," + latitude;
		HttpGet httpGet = new HttpGet(url);
		Future<HttpResponse> future = httpClient.execute(httpGet, null);
		CompletableFuture.supplyAsync(new Supplier<String>() {

			@Override
			public String get() {
				try {
					HttpResponse response = future.get();
					String province = null;
					if(response.getStatusLine().getStatusCode() == 200) {
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
					return province;
			    } catch(Exception e) {
			    	return null;
			    }	
			}	
		}).thenAccept((String province)->{
		  resultFuture.complete(Collections.singleton(ActivityBean.of(uid,aid,null,time,eventType,province)));
		});
	}
	
	public void close(Configuration parameters) throws Exception{
		super.close();
		httpClient.close();
	}
}
