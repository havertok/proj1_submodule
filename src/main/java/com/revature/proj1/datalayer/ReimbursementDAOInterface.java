package com.revature.proj1.datalayer;

import java.util.ArrayList;
import java.util.List;

import com.revature.proj1.beans.Reimbursement;

//The implementing interface will perform operations on Reimbursement table ONLY
//An employee will call this to create and store a reimbursement to the table
public interface ReimbursementDAOInterface {
	
	public ArrayList<Reimbursement> grabReimbursmentList();
	public void pushReimbursementList(List<Reimbursement> reimList);
	public void approveReimbursement(Reimbursement r);
	public void rejectReimbursement(Reimbursement r);
	
}
