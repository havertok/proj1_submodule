package com.revature.service;

import com.revature.proj1.utils.CompanyDBUtilities;

public class ChangeReimStatusService {
	
	public void changeStatus(int id, String toStatusStr, String manName) {
		int toStatus = 0;
		if(toStatusStr.equals("accept")) {
			toStatus = 1;
		} else if(toStatusStr.equals("reject")) {
			toStatus = 2;
		}
		CompanyDBUtilities.approveReim(id, toStatus, manName);
	}
	
}
