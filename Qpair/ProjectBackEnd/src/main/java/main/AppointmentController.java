package main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import databaseConnection.DBConnection;
import model.Appointment;
import model.Doctor;
import model.HelperAppointment;
import model.Patient;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
public class AppointmentController {

	@CrossOrigin(origins = "http://localhost:63342")
	@RequestMapping("/")
	public String index() {

		return "Greetings!!!";
	}
	
	/*
	 * { 
	 *   "date" : "12/16/2016", 
	 *   "time" : "2:00 A.M.",
	 *   "notes":"This is really important", 
	 *   "doc_name":"James", 
	 *   "pat_name":"Shailey" 
	 * }
	 */
	@CrossOrigin(origins = "http://localhost:63342")
	@RequestMapping(value = "/newAppointment", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Object> createLevel(@RequestBody Appointment appointment) {

		HelperAppointment ha = new HelperAppointment();
		
		DBConnection dbCon = new DBConnection();
		Connection conn = dbCon.getConnection();
		Statement stmt = null;
		
		int doc_id = ha.getDocIdFromName(appointment.getDoc_name());
		int pat_id = ha.getDocIdFromName(appointment.getDoc_name());
		
		try {

			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;

			sql = "INSERT INTO appointments ( date, time, notes, doc_id, pat_id) VALUES ('" + appointment.getDate() + "','"
					+ appointment.getTime() + "', '"
					+ appointment.getNotes() + "', '"
					+ doc_id + "', '"
					+ pat_id + "')";

			System.out.println(sql);
			stmt.executeUpdate(sql);
			stmt.close();
			conn.close();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se1) {
				se1.printStackTrace();
				return new ResponseEntity<Object>(se1.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se2) {
				se2.printStackTrace();
				return new ResponseEntity<Object>(se2.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			} // end finally try
		} // end try

		return new ResponseEntity<Object>(appointment, HttpStatus.OK);
	}

	@CrossOrigin(origins = "http://localhost:63342")
	@RequestMapping(value = "/deleteAppointment", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Object> deleteEmail(@RequestBody Appointment appointment) {

		HelperAppointment helper = new HelperAppointment();
		DBConnection dbCon = new DBConnection();
		Connection conn = dbCon.getConnection();
		Statement stmt = null;
		try {

			stmt = conn.createStatement();
			String sql;

			sql = "DELETE FROM appointments WHERE `appointment_id`='" + appointment.getAppointment_id()+"'";

			stmt.executeUpdate(sql);
			System.out.println(sql);

			System.out.println(">>> email id : " + appointment.getAppointment_id() + " deleted!");

			stmt.close();
			conn.close();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se1) {
				se1.printStackTrace();
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se2) {
				se2.printStackTrace();
			} // end finally try
		} // end try

		return new ResponseEntity<Object>(appointment, HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "http://localhost:63342")
	@RequestMapping(value = "/getAppointments", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> getAppointments() {

		HelperAppointment ha = new HelperAppointment();
		DBConnection dbCon = new DBConnection();
		Connection conn = dbCon.getConnection();
		Statement stmt = null;
		List<Map<String, Object>> appointmentList = new ArrayList<Map<String,Object>>();
		ArrayList<Appointment> arr = new ArrayList<Appointment>();
		
		try {
			Map <String, Object> temp = new HashMap <String,Object>();
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			sql = "Select * from appointments";
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				// Get the names of Doctor and Patients
				
				String doc_name = ha.getDocNameFromId(rs.getInt("doc_id"));
				String pat_name = ha.getPatNameFromId(rs.getInt("pat_id"));
				Appointment appointment = new Appointment();
				appointment.setAppointment_id(rs.getInt("appointment_id"));
				appointment.setDoc_name(doc_name);
				appointment.setPatient_name(pat_name);
				appointment.setNotes(rs.getString("notes"));
				appointment.setDate(String.valueOf(rs.getDate("date")));
				appointment.setTime(String.valueOf(rs.getTime("time")));
				
				arr.add(appointment);
			}	
			stmt.close();
			conn.close();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se1) {
				se1.printStackTrace();
				return new ResponseEntity<Object>(se1.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se2) {
				se2.printStackTrace();
				return new ResponseEntity<Object>(se2.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			} // end finally try
		} // end try

		return new ResponseEntity<Object>(arr, HttpStatus.OK);
	}
	@CrossOrigin(origins = "http://localhost:63342")
	@RequestMapping(value = "/getDoctors", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> getDoctors() {
		
		HelperAppointment ha = new HelperAppointment();
		DBConnection dbCon = new DBConnection();
		Connection conn = dbCon.getConnection();
		Statement stmt = null;
		
		ArrayList<Doctor> docList = new ArrayList<Doctor>();
		try {
		
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			sql = "Select * from doctor";
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				// Get the names of Doctor and Patients
				System.out.println("Name"+ rs.getString("name"));
				Doctor newDoc = new Doctor();
				newDoc.setName(rs.getString("name"));
				newDoc.setPhoneNumber(Long.parseLong(rs.getString("ph_number")));		
				docList.add(newDoc);		
			}	
			stmt.close();
			conn.close();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se1) {
				se1.printStackTrace();
				return new ResponseEntity<Object>(se1.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se2) {
				se2.printStackTrace();
				return new ResponseEntity<Object>(se2.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			} // end finally try
		} // end try

		return new ResponseEntity<Object>(docList, HttpStatus.OK);
	}
	@CrossOrigin(origins = "http://localhost:63342")
	@RequestMapping(value = "/getPatients", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Object> getPatients() {

		HelperAppointment ha = new HelperAppointment();
		DBConnection dbCon = new DBConnection();
		Connection conn = dbCon.getConnection();
		Statement stmt = null;
		
		ArrayList<Patient> patList = new ArrayList<Patient>();
		try {
			Map <String, Object> temp = new HashMap <String,Object>();
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			sql = "Select * from patient";
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				// Get the names of Doctor and Patients
				Patient patient = new Patient();
				patient.setName(rs.getString("name"));
				patient.setPhoneNumber(Long.parseLong(rs.getString("name")));
				patList.add(patient);
			}	
			stmt.close();
			conn.close();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se1) {
				se1.printStackTrace();
				return new ResponseEntity<Object>(se1.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se2) {
				se2.printStackTrace();
				return new ResponseEntity<Object>(se2.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			} // end finally try
		} // end try

		return new ResponseEntity<Object>(patList, HttpStatus.OK);
	}
	
	/*
	 * { 
	 *   "appointment_id":1
	 *   "date" : "12/16/2016", 
	 *   "time" : "2:00 A.M.",
	 *   "notes":"This is really important", 
	 *   "doc_name":"James", 
	 *   "pat_name":"harsha" 
	 * }
	 */
	
	
	
	
	
	@CrossOrigin(origins = "http://localhost:63342")
	@RequestMapping(value = "/editAppointment", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Object> approveRequest(@RequestBody Appointment appointment) {

		HelperAppointment helper = new HelperAppointment();
		DBConnection dbCon = new DBConnection();
		Connection conn = dbCon.getConnection();

		Statement stmt = null;
		try {

			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			System.out.println(appointment.getAppointment_id());
			int appointmentId = appointment.getAppointment_id();
			int docId = helper.getDocIdFromName(appointment.getDoc_name());
			System.out.println(appointment.getPatient_name());
			int patId = helper.getPatIdFromName(appointment.getPatient_name());
			
			sql = " UPDATE appointments SET date='" + appointment.getDate() + "' , time = '"
					+ appointment.getTime()+ "' , doc_id = '"
					+docId + "' , pat_id = '"
					+patId+ "' , notes = '"
					+appointment.getNotes()+"' WHERE appointment_id = '"+ appointmentId+ "' ";

			System.out.println(sql);
			stmt.executeUpdate(sql);

			stmt.close();
			conn.close();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se1) {
				se1.printStackTrace();
				return new ResponseEntity<Object>(se1.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se2) {
				se2.printStackTrace();
				return new ResponseEntity<Object>(se2.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			} // end finally try
		} // end try

		return new ResponseEntity<Object>(appointment, HttpStatus.OK);
	}

	
	
	
}
