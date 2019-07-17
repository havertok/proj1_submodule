package com.revature.proj1.utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.proj1.beans.Employee;
import com.revature.proj1.beans.Reimbursement;

public class CompanyTests {
	//we want this employee accessible everywhere in this tester
	public static Employee e1; 
	
	@BeforeClass
	public static void dummyLists() {
		e1 = new Employee("E1", "pass", "e1@comp", "Dilly", "B", null);
		Reimbursement r1 = new Reimbursement(e1.getUsername(), 0, 100, "Reim 1 not approved");
		Reimbursement r2 = new Reimbursement(e1.getUsername(), 0, 200, "Reim 2 APPROVED");
		Reimbursement r3 = new Reimbursement(e1.getUsername(), 2, 300, "Reim 3 REJECTED");
		Reimbursement r4 = new Reimbursement(e1.getUsername(), 1, 400, "Reim 4 not approved");
		Company.employeeMap.put(e1.getUsername(), e1);
		Company.reimbursements.add(r1);
		Company.reimbursements.add(r2);
		Company.reimbursements.add(r3);
		Company.reimbursements.add(r4);
		CompanyDBUtilities.pushAllMaps();
		CompanyDBUtilities.approveReim(r2, e1.getUsername());
		CompanyDBUtilities.grabAllMaps();
		Company.buildRelations();
	}
	
	//Comment out company.GrabAllMaps. It will clear out these dummy values
	@Test
	public void ensureCompanyBuildRelationsSetsReimList() {
		assertEquals(Company.employeeMap.get(e1.getUsername()).getMyReimbursements().isEmpty(), false);
	}
	
	@Test
	public void ensureManagersAreSet() {
		Employee e1Underling1 = new Employee("Pissant", "puissant", "tiny@wat", "caliban", "wat", e1.getUsername());
		Employee e1Underling2 = new Employee("Dude", "bro", "dude", "BRO", "DUDE", e1.getUsername());
		Company.employeeMap.put(e1Underling1.getUsername(), e1Underling1);
		Company.employeeMap.put(e1Underling2.getUsername(), e1Underling2);

		CompanyDBUtilities.pushAllMaps();
		CompanyDBUtilities.grabAllMaps();
		Company.buildRelations();
		
		//System.out.println("ensureManagersAreSet() @ 43" + Company.employeeMap.get(e1.getUsername()).getUnderlings());
		assertEquals(Company.employeeMap.get(e1.getUsername()).getUnderlings().contains(
				Company.employeeMap.get(e1Underling1.getUsername())), true);
		//assertEquals(Company.employeeMap.get(e1.getUsername()).getUnderlings().contains(e1Underling2), true);
	}
	
	@Test
	public void ensureApprovedByManNameReturnsNonEmpty() {
		ArrayList<Reimbursement> compareMe = Company.getApprovedReimsByName(e1.getUsername());
		System.out.println("ensureManNameNotEmpty() @ CompanyTests::56  "+compareMe); //should only print out one reim
		assertEquals(compareMe.isEmpty(), false);
	}
}
