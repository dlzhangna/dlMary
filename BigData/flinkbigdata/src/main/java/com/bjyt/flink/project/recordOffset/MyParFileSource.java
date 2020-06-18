package com.bjyt.flink.project.recordOffset;

import java.io.RandomAccessFile;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

//String:which file
//String:which record/data
public class MyParFileSource extends RichParallelSourceFunction<Tuple2<String,String>>{
	
	//path:C:\flink-1.10.0-bin-scala_2.11\flink-1.10.0\data\input\(0.txt,1.txt)
	private String path;
	
	private boolean flag = true;
    
	public MyParFileSource() {}
	
	public MyParFileSource(String path) {
		this.path = path;
	}
	@Override
	public void run(SourceContext<Tuple2<String, String>> ctx) throws Exception {
		int subtaskIndex = getRuntimeContext().getIndexOfThisSubtask();
		//C:\flink\0.txt
		RandomAccessFile randomAccessFile = new RandomAccessFile(path+"/" + subtaskIndex + ".txt","rw");
		while(flag) {
			String line = randomAccessFile.readLine();
			if(line !=null) {
				line = new String(line.getBytes("ISO-8859-1"),"UTF-8");
				//send data
				ctx.collect(Tuple2.of(subtaskIndex + "", line));
			}else {
				Thread.sleep(1000);
			}
		}
	}

	@Override
	public void cancel() {
		boolean flag = false;		
	}

	

}
