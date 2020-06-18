package com.bjyt.flink.window;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext;

public class StreamDataSourceA extends RichParallelSourceFunction<Tuple3<String,String,Long>>{

	private volatile boolean running = true;
	
	public void run(SourceContext<Tuple3<String,String,Long>> ctx) throws InterruptedException{
		Tuple3[] elements = new Tuple3[] {
				//10 sec/毫秒
				Tuple3.of("a", "1", 1000000050000L),//[50000-60000)(59999)10s
				Tuple3.of("a", "2", 1000000054000L),//[50000-600000) 54000-5002=48998<50000, the record of (a,1) won't be triggered
				Tuple3.of("a", "3", 1000000079900L),//[70000-80000) 79900-5002=74898>54000,the record of (a,2) will be triggered
				Tuple3.of("a", "4", 1000000115000L),//[110000-120000) //115000-5000 >=109999,(b,5) and (b,6) will be discarded, 115000-5002=109998 < 109999 (b,5) and (b,6) won't be discard
				Tuple3.of("b", "5", 1000000100000L),//[100000-110000)//data comes delay, will be discard,can't join
				Tuple3.of("b", "6", 1000000108000L) //[100000-110000)//data comes delay, will be discard,can't join
		};
		int count = 0;
		while(running && count <elements.length) {
			ctx.collect(new Tuple3<>((String) elements[count].f0,(String)elements[count].f1,(Long)elements[count].f2));
			count++;
			Thread.sleep(1000);
		}
		//Thread.sleep(100000000);
	}

	@Override
	public void cancel() {
		running = false;
	}

}
