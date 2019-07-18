package com.revature.proj1.utils;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.proj1.beans.Employee;
import com.revature.proj1.beans.Reimbursement;
import com.revature.proj1.datalayer.EmployeeDAOImplementor;
import com.revature.proj1.datalayer.ReimbursementDAOImplementor;

public class CompanyDBUtilitiesTests {

	private static EmployeeDAOImplementor empImp = new EmployeeDAOImplementor();
	private static ReimbursementDAOImplementor reimImp = new ReimbursementDAOImplementor();
	static Employee e1, e2, e3;
	static Reimbursement r1, r2, r3;
	
	@BeforeClass
	public static void init() {
		e1 = new Employee("User","Pass","Email","Fname","Lname", null);
		e2 = new Employee("User2","Pass2","Email2","Fname2","Lname2","User");
		e3 = new Employee("User3","Pass3","Email3","Fname3","Lname3","User2");
		empImp.addEmployeeDB(e1);
		empImp.addEmployeeDB(e2);
		empImp.addEmployeeDB(e3);
		r1 = new Reimbursement("User", 0, 200.00, "These are notes, saying for what this request was made", "2019-05-21");
		r2 = new Reimbursement("User", 0, 200.00, "This is r2", "1999-01-01");
		r3 = new Reimbursement("User", 0, 200.00, "This is r3, who cares what the id is?", "2019-7-17");
		reimImp.addReimbursementDB(r1);
		reimImp.addReimbursementDB(r2);
		reimImp.addReimbursementDB(r3);
	}
	
	@Test
	public void assertAddedReimIsReallyAdded() {
		Reimbursement testReim = new Reimbursement(e3.getUsername(), 0, 20000.00, "Ivejustbeenaddedhelpme", "2019-7-17");
		reimImp.addReimbursementDB(testReim);
		Employee test = CompanyDBUtilities.getEmployeeByName(e3.getUsername());  //This function gets employe AND his reim list
		assertEquals(test.getMyReimbursements().contains(testReim), true);
	}
}
