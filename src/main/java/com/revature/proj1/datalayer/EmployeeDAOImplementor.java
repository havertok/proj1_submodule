package com.revature.proj1.datalayer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.revature.proj1.beans.Employee;
import com.revature.proj1.beans.Reimbursement;

import oracle.jdbc.OracleTypes;

public class EmployeeDAOImplementor implements EmployeeDAOInterface {
	public static ConnFactory cF = ConnFactory.getInstance();
	
	public HashMap<String, Employee> grabEmployeeMap() { 
		HashMap<String, Employee> empMap = new HashMap<>();
		String sql = "SELECT * FROM EMPLOYEE";
		Employee emp = null;
		try (Connection conn = cF.getConnection()){ 
			PreparedStatement prep = conn.prepareStatement(sql);
			ResultSet rs = prep.executeQuery();
			while(rs.next()) {
				emp = new Employee(rs.getString("USERNAME"), rs.getString("USERPASS"), rs.getString("EMAIL"),
						rs.getString("FIRSTNAME"), rs.getString("LASTNAME"), rs.getString("MANAGER_USERNAME"));
				//System.out.println("putting... "+ emp);
				empMap.put(emp.getUsername(), emp);
				//after Reimbursement map is pulled, add to emp reim list via java
			}
			return empMap;
		} catch (SQLException e) {
			//Change to .log for file logger (somehow)
			e.printStackTrace();
		}
		return null; //our statement has failed utterly
	}
	
	//Might do a try with resources and pass in a connection
	public void pushEmployeeMap(Map<String, Employee> empMap) {
		for(Employee emp : empMap.values()) {
			addEmployeeDB(emp);
		}

	}
	
	//This shouldn't violate PK restraint since that exception is handled in the stored procedure
	//As a bonus, if a user changes his password and pushes the map, the db will store that change
	public void addEmployeeDB(Employee emp) {
		String sql = "{call ADD_EMPLOYEE(?,?,?,?,?,?)";
		try (Connection conn = cF.getConnection()) {
			CallableStatement call = conn.prepareCall(sql);
			call.setString(1, emp.getUsername());
			call.setString(2, emp.getPassword());
			call.setString(3, emp.getEmail());
			call.setString(4, emp.getFname());
			call.setString(5, emp.getLname());
			call.setString(6, emp.getManagername());
			call.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//We want separation of concerns, so in CompanyDBUtilities, a getEmployeeByName needs to 
	//getMyReims too
	public Employee getEmployeeByName(String uname) {
		String sql = "SELECT * FROM EMPLOYEE WHERE USERNAME = ?";
		Employee emp = null;
		try(Connection conn = cF.getConnection()){
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.setString(1, uname);
			ResultSet rs = prep.executeQuery();
			while(rs.next()) {
				emp = new Employee(rs.getString("USERNAME"), rs.getString("USERPASS"), rs.getString("EMAIL"),
						rs.getString("FIRSTNAME"), rs.getString("LASTNAME"), rs.getString("MANAGER_USERNAME"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return emp;
	}
	
	public ArrayList<Employee> getUnderlings(String manName){
		ArrayList<Employee> eList = new ArrayList<>();
		Employee emp = null;
		String sql = "{call GRAB_UNDERLINGS(?,?)";
		try(Connection conn = cF.getConnection()){
			CallableStatement call = conn.prepareCall(sql);
			call.setString(1, manName);
			call.registerOutParameter(2, OracleTypes.CURSOR); //Our SQL returns a cursor, jdbc needs to know that
			call.execute(); //EXECUTE
			ResultSet rs = (ResultSet) call.getObject(2);  //Convert our cursor into a result set
			while(rs.next()) {
				emp = new Employee(rs.getString("USERNAME"), rs.getString("USERPASS"), rs.getString("EMAIL"),
						rs.getString("FIRSTNAME"), rs.getString("LASTNAME"), rs.getString("MANAGER_USERNAME"));
				//System.out.println("EmpDaoWhile ||  emp:: "+emp);
				eList.add(emp);
			}
			//System.out.println("EmpDao line 103:: "+eList);
			return eList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return eList;
	}

}
