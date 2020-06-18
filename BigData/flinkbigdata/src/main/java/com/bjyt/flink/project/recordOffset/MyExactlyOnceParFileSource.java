package com.bjyt.flink.project.recordOffset;

import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.Iterator;

import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

//String:which file
//String:which record/data
public class MyExactlyOnceParFileSource extends RichParallelSourceFunction<Tuple2<String,String>> implements CheckpointedFunction{
	
	//path:C:\flink-1.10.0-bin-scala_2.11\flink-1.10.0\data\input\(0.txt,1.txt)
	private String path;
	
	private boolean flag = true;
	
	private long offset = 0;
	
	private transient ListState<Long> offsetState;
    
	public MyExactlyOnceParFileSource() {}
	
	public MyExactlyOnceParFileSource(String path) {
		this.path = path;
	}
	@Override
	public void run(SourceContext<Tuple2<String, String>> ctx) throws Exception {
		//get history value of offset
		Iterator<Long> iterator = offsetState.get().iterator();
		while(iterator.hasNext()) {
			offset = iterator.next();
		}
		
		int subtaskIndex = getRuntimeContext().getIndexOfThisSubtask();
		
		//C:\flink\0.txt
		RandomAccessFile randomAccessFile = new RandomAccessFile(path+"/" + subtaskIndex + ".txt","rw");

		//Read data from specified location
		randomAccessFile.seek(offset);
		
		//get a lock
		final Object lock = ctx.getCheckpointLock();
		while(flag) {
			String line = randomAccessFile.readLine();
			if(line !=null) {
				line = new String(line.getBytes("ISO-8859-1"),"UTF-8");
				synchronized(lock) {
				 offset = randomAccessFile.getFilePointer();
				 //send data
				 ctx.collect(Tuple2.of(subtaskIndex + "", line));
				}
			}else {
				Thread.sleep(1000);
			}
		}
	}

	@Override
	public void cancel() {
		boolean flag = false;		
	}
    
	/*
	 * store status to StateBacked regular
	 * @see org.apache.flink.streaming.api.checkpoint.CheckpointedFunction#snapshotState(org.apache.flink.runtime.state.FunctionSnapshotContext)
	 */
	@Override
	public void snapshotState(FunctionSnapshotContext context) throws Exception {
		//clear history state value, update the latest state value
		offsetState.clear();
		
		offsetState.add(offset);
	}
    
	/*
	 * initialize OperatorState,constructor method is invoke,then invoke this method
	 * @param context
	 * @throws Exception
	 */
	@Override
	public void initializeState(FunctionInitializationContext context) throws Exception {
		// Define a state descriptor
		ListStateDescriptor<Long> stateDescriptor = new ListStateDescriptor<Long>("offset-state",Long.class);
		//ListStateDescriptor<Long> stateDescriptor = new ListStateDescriptor<Long>("offset-state",Type.LONG);
		//ListStateDescriptor<Long> stateDescriptor = new ListStateDescriptor<Long>("offset-state",TypeInformation.of(new TypeHint<Long>() {}));
		// Init operator state or get history operator state
		//here get the history state
		offsetState = context.getOperatorStateStore().getListState(stateDescriptor);
		
	}
}
