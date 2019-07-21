package com.revature.proj1.utils;

import java.util.ArrayList;
import java.util.HashMap;

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

	public static void pushAllMaps() {
		pushEmployeeMap();
		pushReimbursementList();
	}

	public static void grabAllMaps() {
		Company.employeeMap = grabEmployeeMap();
		Company.reimbursements = grabReimbursmentList();
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
	
	public static void approveReim(Reimbursement r, String manName) {
		r.setApprovingManager(manName);
		getReimDAO().approveReimbursement(r);
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
	
	//TODO: Method to return full underling list, maybe.
	
}
