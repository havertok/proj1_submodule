package com.revature.proj1.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Employee implements Serializable {
	
	/**
	 * I implement Serializable, for employee contains a list of Employees (underlings), they become serialized
	 * as inner classes when written as a JSON object
	 */
	private static final long serialVersionUID = 1L;

	private String username;
	private transient String password; //don't want this passed on
	private String email;
	private String fname;
	private String lname;
	private String managername;
	private boolean isManager;
	private List<Reimbursement> myReimbursements;
	// Managers will use these, an employee with an empty underling list is by def
	// not a manager
	// when displaying reims, check against this list
	private List<Employee> underlings;

	public Employee(String username, String password, String email, String fname, String lname, String managername) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.fname = fname;
		this.lname = lname;
		this.managername = managername;
		this.isManager = false;
		myReimbursements = new ArrayList<Reimbursement>();
		underlings = new ArrayList<Employee>();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getManagername() {
		return managername;
	}

	public void setManagername(String managername) {
		this.managername = managername;
	}

	public List<Reimbursement> getMyReimbursements() {
		return myReimbursements;
	}

	public void setMyReimbursements(List<Reimbursement> myReimbursements) {
		this.myReimbursements.addAll(myReimbursements);
	}

	public void addUnderling(Employee e) {
		isManager = true;
		this.underlings.add(e);
	}

	public void addUnderlings(List<Employee> underlist) {
		if (underlist != null) {
			isManager = true;
			this.underlings.addAll(underlist);
		}
	}

	public void removeUnderling(Employee e) {
		this.underlings.remove(e);
	}

	public List<Employee> getUnderlings() {
		return underlings;
	}

	public boolean isManager() {
		return isManager;
	}

	public void setManager(boolean isManager) {
		this.isManager = isManager;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((fname == null) ? 0 : fname.hashCode());
		result = prime * result + (isManager ? 1231 : 1237);
		result = prime * result + ((lname == null) ? 0 : lname.hashCode());
		result = prime * result + ((managername == null) ? 0 : managername.hashCode());
		result = prime * result + ((myReimbursements == null) ? 0 : myReimbursements.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((underlings == null) ? 0 : underlings.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fname == null) {
			if (other.fname != null)
				return false;
		} else if (!fname.equals(other.fname))
			return false;
		if (isManager != other.isManager)
			return false;
		if (lname == null) {
			if (other.lname != null)
				return false;
		} else if (!lname.equals(other.lname))
			return false;
		if (managername == null) {
			if (other.managername != null)
				return false;
		} else if (!managername.equals(other.managername))
			return false;
		if (myReimbursements == null) {
			if (other.myReimbursements != null)
				return false;
		} else if (!myReimbursements.equals(other.myReimbursements))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (underlings == null) {
			if (other.underlings != null)
				return false;
		} else if (!underlings.equals(other.underlings))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Employee [username=" + username + ", password=" + password + ", fname=" + fname + ", lname=" + lname
				+ ", managername=" + managername + "]";
	}

}
