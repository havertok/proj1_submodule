package com.revature.proj1.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.revature.proj1.beans.Employee;
import com.revature.proj1.beans.Reimbursement;

//container for our maps, lists, et al
public class Company {

	public static Map<String, Employee> employeeMap = new HashMap<>(); // EMPLOYEE Table
	public static List<Reimbursement> reimbursements = new ArrayList<>(); // REIMBURSEMENT Table
	public static List<Integer> approvedReimbursements = new ArrayList<>(); // APPROVED_RE Table

	// Performs utility functions on our maps (like get Employee by name and adding
	// Reimbursments to employee.
	// Also build underling list

	/*
	 * Employee has a list of: <Integer> Reimbursement id - meReimbursements
	 * <String> Employee id - underlings
	 * 
	 * Reimbursement has String - approving manager set when a reimbursement is
	 * approved
	 * 
	 */
	public static void buildRelations() {
		addReimbursementsToEmployee();
		addUnderlingsToManager();
		System.out.println("Fin buildRelations() @ Company::34");
	}

	private static void addReimbursementsToEmployee() {
		ArrayList<Reimbursement> tempR = null;
		// This uses the Stream() api and the filter method introduced in java 8 to
		// filter out
		// Reimbursements of matching username and setting that as that users list of
		// reimbursements
		// also a lambda function
		for (String e : employeeMap.keySet()) {
			tempR = (ArrayList<Reimbursement>) reimbursements.stream().filter(reim -> reim.getEmpUsername().equals(e))
					.collect(Collectors.toList());
//			System.out.println("Company^ addReimToEmp list: "+tempR);
//			System.out.println("Company^ employeMappGet "+e+"  "+employeeMap.get(e));
			employeeMap.get(e).setMyReimbursements(tempR);
		}
	}

	// Now to add emp usernames to manager lists
	private static void addUnderlingsToManager() {
		ArrayList<Employee> tempE = null;
		for (Employee e : employeeMap.values()) {
			// System.out.println(e);
			tempE = (ArrayList<Employee>) employeeMap.values().stream().filter(emp -> {
				// An unmanaged employee is stored as a null, not an empty string
				if (emp.getManagername() != null) {
					return emp.getManagername().equals(e.getUsername());
				} else {
					return false;
				}
			}).collect(Collectors.toList());

			// System.out.println("tempList: "+tempE);
			e.addUnderlings(tempE);
		}
	}

	// All approved reimbursements are stored as a list of integers, but we want the
	// full reimbursement object, luckily, we can just pass in a managers name and get the
	// reimbursements
	public static ArrayList<Reimbursement> getApprovedReimsByName(String manName){
		ArrayList<Reimbursement> tempR = null;
		for(Reimbursement r: reimbursements) {
			//nested for readability
			if(approvedReimbursements.contains(r.getId())) {
				System.out.println("manName: "+manName+" r.id"+r.getId()+" man:"+r.getApprovingManager());
				if((r != null) && (r.getApprovingManager() != null)) {
					if(r.getApprovingManager().equals(manName)) {
						tempR.add(r);
					}
				}
			}
		}
		System.out.println("approvedReims: "+ approvedReimbursements);
		System.out.println("TempR: "+tempR);
		return tempR;
	}

}
