package com.bjyt.flink.project.async;

import java.net.InetAddress;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.common.settings.Settings;

public class AsyncEsRequest extends RichAsyncFunction<String,Tuple2<String,String>>{
	
	private static transient TransportClient transportClient;
	
	public void open(Configuration parameters) throws Exception{
		Settings settings = Settings.builder().put("cluster.name","docker-cluster").build();
		//transportClient = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		transportClient = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
	}

	public void close() throws Exception{
		transportClient.close();
	}

	public void asyncInvoke(String key, final ResultFuture<Tuple2<String,String>> resultFuture) throws Exception {
        ActionFuture<GetResponse> actionFuture = transportClient.get(new GetRequest("doit","users",key));
        CompletableFuture.supplyAsync(new Supplier<String>() {
        	
        	@Override
        	public String get() {
        		try {
        			GetResponse response = actionFuture.get();
        			return response.getSource().get("name").toString();
        		} catch (InterruptedException | ExecutionException e) {
        			return null;
        		}
        	}
        }).thenAccept((String dbResult)->{
        	resultFuture.complete(Collections.singleton(new Tuple2<>(key,dbResult)));
        });
	}

}
