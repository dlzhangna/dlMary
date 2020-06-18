package com.bjyt.flink.table.function;

import org.apache.flink.table.functions.FunctionContext;
import org.apache.flink.table.functions.ScalarFunction;
import org.apache.flink.types.Row;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple4;

//ScalarFunction mean 1:1 input 1 line, output 1 line
public class IpLocation extends ScalarFunction{
  private List<Tuple4<Long,Long,String,String>> lines = new ArrayList<>();
  
  @Override
  public void open(FunctionContext context) throws Exception{
	  //Get the cache file
	  File cachedFile = context.getCachedFile("ip-rules");
	  BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(new FileInputStream(cachedFile)));
	  
	  String line = null;
	  while((line = bufferedReader.readLine())!=null) {
		  String[] fields = line.split("[|]");
		  Long startNum = Long.parseLong(fields[2]);
		  Long endNum = Long.parseLong(fields[3]);
		  String province = fields[6];
		  String city = fields[7];
		  lines.add(Tuple4.of(startNum, endNum, province, city)); 
	  }
  }
  
  //method name must be eval
  public Row eval(String ip) {
	  Long ipNum= ip2Long(ip);
	  return binarySearch(ipNum);
  }
  public TypeInformation<Row> getResultType(){
	  return Types.ROW_NAMED(new String[] {"province", "city"});
  }
  
  private Long ip2Long(String ip) {
		  String[] fragments = ip.split("[.]");
		  Long ipNum = 0L;
		  for(int i=0;i<fragments.length;i++) {
			  ipNum = Long.parseLong(fragments[i]) | ipNum << 8L;
		  }
		  return ipNum;
	  }
	  
  private Row binarySearch(Long ipNum) {
     Row result = null;
     int index = -1;
     int low =0;
     int high = lines.size() -1;
     while(low<=high) {
    	 int middle = (low + high)/2;
    	 if((ipNum >= lines.get(middle).f0) && (ipNum <= lines.get(middle).f1))
    		 index = middle;
    	 if(ipNum < lines.get(middle).f0)
    		 high = middle -1;
    	 else {
    		 low = middle +1;
    	 }
     }
     if(index !=-1) {
    	 Tuple4<Long,Long,String,String> tp4 = lines.get(index);
    	 result = Row.of(tp4.f2,tp4.f3);
     }
     return result;
  }
			  
}
