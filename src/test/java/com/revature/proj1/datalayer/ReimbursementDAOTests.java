package com.revature.proj1.datalayer;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import org.junit.BeforeClass;
import org.junit.Test;
import com.revature.proj1.beans.Employee;
import com.revature.proj1.beans.Reimbursement;

public class ReimbursementDAOTests {
	static ReimbursementDAOImplementor reimImp = new ReimbursementDAOImplementor();
	private static EmployeeDAOImplementor empImp = new EmployeeDAOImplementor();
	private static ArrayList<Reimbursement> tempReim = new ArrayList<>();

	@BeforeClass
	public static void init() {
		Reimbursement r1 = new Reimbursement("User", 0, 200.00, "These are notes, saying for what this request was made", "2019-05-21");
		Reimbursement r2 = new Reimbursement("User", 0, 200.00, "This is r2", "1999-01-01");
		Reimbursement r3 = new Reimbursement("User", 0, 200.00, "This is r3, who cares what the id is?", "2019-7-17");
		
		Employee e1 = new Employee("User","Pass","Email","Fname","Lname", null); //copied from emp tests
		empImp.addEmployeeDB(e1);
		tempReim.add(r1);
		tempReim.add(r2);
		tempReim.add(r3);
		reimImp.pushReimbursementList(tempReim);
		reimImp.approveReimbursement(r3);
		tempReim = reimImp.grabReimbursmentList();
	}
	
	//Grabbing all approved reims from a manager should not be empty (but they can be, if manager
	//did not approve of any reims).
	@Test
	public void ensureGrabbingReimListNotEmpty() {
		ArrayList<Reimbursement> reimIdList = new ArrayList<>();
		reimIdList = reimImp.getMyReimList("User"); //should give 2
		for(Reimbursement r : reimIdList) {
			//System.out.println("Reim: " + r);
		}
		assertEquals(reimIdList.isEmpty(), false);
		reimIdList = reimImp.getApprovedReimsByManName("Billy");//no such user exists :. the list should be empty
		assertEquals(reimIdList.isEmpty(), true);
	}
	
	//The stored procedure changes status and adds approving manager name to junction table
	@Test
	public void ensureRejectChangesState() {
		Reimbursement r4 = new Reimbursement("User3", 0, 3000.35, "APPROVED");
		tempReim.add(r4);
		reimImp.pushReimbursementList(tempReim); 
		//Now we reject r4
		reimImp.rejectReimbursement(r4);
		//and refresh the list
		tempReim = reimImp.grabReimbursmentList();
		for(Reimbursement r : tempReim) {
			if(r.getId() == r4.getId()) {
				assertEquals(r.getStatus(), r4.getStatus());
			}
		}
	}
	
	@Test
	public void ensureDateIsStored() {
		
	}
	
	//Last test is for images
	
}
