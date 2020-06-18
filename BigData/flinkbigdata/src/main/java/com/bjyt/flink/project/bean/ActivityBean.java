package com.bjyt.flink.project.bean;

public class ActivityBean {
	public String uid;

	public String aid;

	public String activityName;

	public String time;

	public int eventType;

	public String province;
	
	public double longitude;
	
	public double latitude;
	
	public int count = 1;

	public ActivityBean() {

	}
	
	public ActivityBean(String uid, String aid, String activityName, String time, int eventType, String province) {
		super();
		this.uid = uid;
		this.aid = aid;
		this.activityName = activityName;
		this.time = time;
		this.eventType = eventType;
		this.province = province;
	}

	public ActivityBean(String uid, String aid, String activityName, String time, int eventType, String province,double longitude,double latitude) {
		super();
		this.uid = uid;
		this.aid = aid;
		this.activityName = activityName;
		this.time = time;
		this.eventType = eventType;
		this.province = province;
		this.longitude = longitude;
		this.latitude = latitude;
	}
   
	public static ActivityBean of(String uid, String aid, String activityName, String time, int eventType,
			String province) {
		return new ActivityBean(uid, aid, activityName, time, eventType, province);
	}
	
	public static ActivityBean of(String uid, String aid, String activityName, String time, int eventType,
			String province,double longitude,double latitude) {
		return new ActivityBean(uid, aid, activityName, time, eventType, province,longitude,latitude);
	}
	
	@Override
	public String toString() {
		return "ActivityBean [uid=" + uid + ", aid=" + aid + ", activityName=" + activityName + ", time=" + time
				+ ", eventType=" + eventType + ", province=" + province + ", longitude=" + longitude + ", latitude="
				+ latitude + ", count=" + count + "]";
	}

}
