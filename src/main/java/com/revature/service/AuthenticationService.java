package com.revature.service;

import com.revature.proj1.beans.Credentials;
import com.revature.proj1.beans.Employee;
import com.revature.proj1.utils.CompanyDBUtilities;

public class AuthenticationService {
	
	public Employee authenticateEmployee(Credentials creds) {
		Employee e = CompanyDBUtilities.getEmployeeByName(creds.getUsername());
		
		if(e != null && creds.getPassword().equals(e.getPassword())) {
			return e;
		}
		return null;
	}
	
}
