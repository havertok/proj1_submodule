package com.revature.service;

import com.revature.proj1.beans.Reimbursement;

public class generateNewReimbursement {
	
	public Reimbursement generateReimbursement(Double amount, String notes, String username) {
		
		Reimbursement returnMe = new Reimbursement(username, 0, amount, notes);
		return returnMe;
	}
	
}
