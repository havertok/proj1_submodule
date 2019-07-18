package com.revature.proj1.datalayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.proj1.beans.Employee;
import com.revature.proj1.beans.Reimbursement;
import com.revature.proj1.utils.Company;
import com.revature.proj1.utils.CompanyDBUtilities;

public class EmployeeDAOTests {
	private static EmployeeDAOImplementor empImp = new EmployeeDAOImplementor();
	private static ReimbursementDAOImplementor reimImp = new ReimbursementDAOImplementor();
	static Employee e1, e2, e3;
	
	@BeforeClass
	public static void initEmpMap() {
		//no manager, :. manager name is null, empty strings are recieved from sql
		//as null, but remain empty strings (not null) in java
		e1 = new Employee("User","Pass","Email","Fname","Lname", null);
		e2 = new Employee("User2","Pass2","Email2","Fname2","Lname2","User");
		e3 = new Employee("User3","Pass3","Email3","Fname3","Lname3","User2");
		empImp.addEmployeeDB(e1);
		empImp.addEmployeeDB(e2);
		empImp.addEmployeeDB(e3);
	}
	
	@Test
	public void assertE1UnderlingListContainsE2() {
		e1.addUnderlings(empImp.getUnderlings(e1.getUsername()));
		for(Employee e: e1.getUnderlings()) {
			System.out.println("assertE1 ::line 30 \n"+e);
		}
		assertEquals(e1.getUnderlings().contains(e2), true);
	}
	
	@Test
	public void assertAddAfterPasswordChangeChangesPassword() {
		String ante = e1.getPassword();
		e1.setPassword("cheesewhiz");
		empImp.addEmployeeDB(e1);
		Employee test = empImp.getEmployeeByName(e1.getUsername());
		assertEquals(test.getPassword().equals(ante), false);
	}
	
}
