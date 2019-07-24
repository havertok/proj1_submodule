package com.revature.proj1.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.revature.proj1.beans.Employee;
import com.revature.proj1.beans.Reimbursement;
import com.revature.proj1.datalayer.EmployeeDAOImplementor;
import com.revature.proj1.datalayer.ReimbursementDAOImplementor;

//This will take care of populating and pushing the maps stored in the Company utility class
public class CompanyDBUtilities {
	
	public static EmployeeDAOImplementor getEmpDAO() {
		return new EmployeeDAOImplementor();
	}
	
	public static ReimbursementDAOImplementor getReimDAO() {
		return new ReimbursementDAOImplementor();
	}

	public static HashMap<String, Employee> grabEmployeeMap() {
		return getEmpDAO().grabEmployeeMap();
	}

	public static void pushEmployeeMap() {
		getEmpDAO().pushEmployeeMap(Company.employeeMap);
	}

	public static ArrayList<Reimbursement> grabReimbursmentList() {
		return getReimDAO().grabReimbursmentList();
	}

	public static void pushReimbursementList() {
		getReimDAO().pushReimbursementList(Company.reimbursements);
	}
	
	public static void approveReim(int rid, int toStatus, String manName) {
		getReimDAO().approveReimbursement(rid, toStatus, manName);
	}

	public static void addUpdateReim(Reimbursement r) {
		getReimDAO().addReimbursementDB(r);
	}
	
	public static ArrayList<Reimbursement> grabApprovedReimbursements() {
		return getReimDAO().getApprovedReims();
	}
	
	public static ArrayList<Reimbursement> grabApprovedReimsByName(String manName){
		return getReimDAO().getApprovedReimsByManName(manName);
	}
	
	//START NEW METHODS 7/17 NO-STATE-IN-JAVA
	//Tempted to call this recursively to fill out the whole chain of command.  
	public static Employee getEmployeeByName(String uname) {
		Employee emp = null;
		emp = getEmpDAO().getEmployeeByName(uname);
		if(emp != null) {
			emp.setMyReimbursements(getReimDAO().getMyReimList(uname));
			emp.addUnderlings(getEmpDAO().getUnderlings(uname));
			for(Employee e : emp.getUnderlings()) {
				e.setMyReimbursements(getReimDAO().getMyReimList(e.getUsername()));
				e.addUnderlings(getEmpDAO().getUnderlings(e.getUsername()));
			}
		}
		return emp;
	}
	
	//Should filter out our employee map based on whether any employee has a managerName contained within the Employee map
	public static List<Employee> grabManagers() {
		HashMap<String, Employee> filterMe = grabEmployeeMap();
		ArrayList<Employee> result = (ArrayList<Employee>) filterMe.values().stream()
				.filter(empName -> filterMe.containsKey(empName.getManagername())).collect(Collectors.toList());
		return result;
	}
	
	
	public static List<Reimbursement> getPendingReimbursements(){
		ArrayList<Reimbursement> filterMe = grabReimbursmentList();
		ArrayList<Reimbursement> result = (ArrayList<Reimbursement>) filterMe.stream().filter(item -> item.getStatus() == 0)
				.collect(Collectors.toList());
		return result;
	}
}
