package model;

import java.util.List;
import java.util.Map;

public class GetAppointmentList {
	
	
	private int number = 0;
	private String date = "";
	private String time;
	private String doc_name;
	private String pat_name;
	private List<Map<String, Object>> appointment_list;
	
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDoc_name() {
		return doc_name;
	}
	public void setDoc_name(String doc_name) {
		this.doc_name = doc_name;
	}
	public String getPat_name() {
		return pat_name;
	}
	public void setPat_name(String pat_name) {
		this.pat_name = pat_name;
	}
	public List<Map<String, Object>> getAppointment_list() {
		return appointment_list;
	}
	public void setAppointment_list(List<Map<String, Object>> appointment_list) {
		this.appointment_list = appointment_list;
	}

}
