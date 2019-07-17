package com.revature.proj1.datalayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.proj1.beans.Employee;
import com.revature.proj1.utils.Company;
import com.revature.proj1.utils.CompanyDBUtilities;

public class EmployeeDAOTests {
	private static EmployeeDAOImplementor empImp = new EmployeeDAOImplementor();
	private static HashMap<String, Employee> tempEmp = new HashMap<>();
	HashMap<String, Employee> pulledMap;
	static Employee testPass, e1, e2;
	
	@BeforeClass
	public static void initEmpMap() {
		//no manager, :. manager name is null, empty strings are recieved from sql
		//as null, but remain empty strings (not null) in java
		e1 = new Employee("User","Pass","Email","Fname","Lname", null);
		e2 = new Employee("User2","Pass2","Email2","Fname2","Lname2","User");
		Employee e3 = new Employee("User3","Pass3","Email3","Fname3","Lname3","User2");
		tempEmp.put(e1.getUsername(), e1);
		tempEmp.put(e2.getUsername(), e2);
		tempEmp.put(e3.getUsername(), e3);
		empImp.pushEmployeeMap(tempEmp);//in the real world, CompanyDBUtils would do this
//		for (Employee e : tempEmp.values()) {
//			System.out.println(e);
//		}
//		System.out.println("=================BeforeClassDone===================");
	}
	
	//We want to make sure that the maps we push/pull contain the same key,value pairs
	@Test
	public void ensureGrabMapIsSameAsPushMap() {
		pulledMap = empImp.grabEmployeeMap();
		
		boolean noMatchKey = false;
		for(String key : tempEmp.keySet()) {
			if(!pulledMap.containsKey(key)) {
				noMatchKey = true;
			}
		}
		assertEquals(noMatchKey, false);
	}
	
	@Test
	public void ensureKeyPairIsGood() {
//		for(Employee e: tempEmp.values()) {
//			System.out.println(e);
//		}
		pulledMap = empImp.grabEmployeeMap();
//		System.out.println("Pulled the map");
//		for(Employee e: pulledMap.values()) {
//			System.out.println(e);
//		}
		for(String key : tempEmp.keySet()) {
			assertEquals(tempEmp.get(key), pulledMap.get(key));
		}
	}
	
	//Tests that a password for an existing user, when changed, is properly updated
	//also no constraint violations (not sure how to do that with junit)
	@Test 
	public void ensurePasswordIsChanged() {
		testPass = new Employee("Testman","password","Email","Fname","Lname", "User2");
		tempEmp.put(testPass.getUsername(), testPass);
		empImp.pushEmployeeMap(tempEmp);
		String ante = testPass.getPassword();
		
		tempEmp.get(testPass.getUsername()).setPassword("ChangedPassword");
		empImp.pushEmployeeMap(tempEmp);
		pulledMap = empImp.grabEmployeeMap();
		assertNotEquals(ante, pulledMap.get(testPass.getUsername()).getPassword());
	}
	
	//BeforeClass inits some vals, so this should be ok
	@Test
	public void ensureDatabaseAndCompanyBuildRelationShipsWorks() {
		CompanyDBUtilities.grabAllMaps();
		Company.buildRelations();
		assertEquals(e1.getUnderlings().contains(e2), true);
		assertEquals(e2.getUnderlings().contains(e1), false);
	}

}
