package com.bjyt.flink.project.bean;

public class ActBean {
  public String uid;
  //activity id
  public String aid;
  public String time;
  //event type
  public Integer type;
  public String province;
  public Integer count;
  
  public ActBean() {

  }
  
  public ActBean(String uid, String aid, String time, Integer type, String province) {
		super();
		this.uid = uid;
		this.aid = aid;
		this.time = time;
		this.type = type;
		this.province = province;
	}
  
  public ActBean(String uid, String aid, String time, Integer type, String province,Integer count) {
		super();
		this.uid = uid;
		this.aid = aid;
		this.time = time;
		this.type = type;
		this.province = province;
		this.count = count;
	}
  
  public static ActBean of(String uid,String aid,String time,Integer type,String province) {
	  return new ActBean(uid,aid,time,type,province);
  }
  
  public static ActBean of(String uid,String aid,String time,Integer type,String province,Integer count) {
	  return new ActBean(uid,aid,time,type,province,count);
  }
  
  @Override
	public String toString() {
		return "ActBean [uid=" + uid + ", aid=" + aid + ", time=" + time + ", type=" + type + ", province=" + province
				+ ", count=" + count + "]";
	}
}
