package com.revature.proj1.beans;

import java.io.File;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

public class Reimbursement implements Serializable {
	
	/**
	 * I implement Serializable, for employee contains a list of Reimbursements, they become serialized
	 * as inner classes when written as a JSON object
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String empUsername;
	private String approvingManager; //used only when a reimbursement is approved
	private int status;
	private double amount;
	private LocalDate dateMade; //date in format "yyyy-mm-dd"
	private String notes;
	private byte[] reciept;
	
	//We will get and set the ID according to the SQL side's sequencer
	//No boolean? It's not a new Reimbursement, so set the Date from the SQL
	public Reimbursement(String empUsername, int status, double amount, String notes) {
		super();
		this.empUsername = empUsername;
		this.status = status;
		this.amount = amount;
		this.notes = notes; //stores a memo for what the reimbursement is for
		this.dateMade = LocalDate.now();
		this.id = this.hashCode();
	}
	
	//Insert method to create a new Reim (via super) and make a date for that Reim, push to DB
	public Reimbursement(String empUsername, int status, double amount, String notes, String date) {
		super();
		this.empUsername = empUsername;
		this.status = status;
		this.amount = amount;
		this.notes = notes; //stores a memo for what the reimbursement is for
		this.id = this.hashCode();
		this.dateMade = LocalDate.parse(date); //From java.sql, should work nicely with oracld DB
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmpUsername() {
		return empUsername;
	}

	public void setEmpUsername(String empUsername) {
		this.empUsername = empUsername;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDate getDateMade() {
		return dateMade;
	}

	public void setDateMade(LocalDate dateMade) {
		this.dateMade = dateMade;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getApprovingManager() {
		return approvingManager;
	}

	public void setApprovingManager(String approvingManager) {
		this.approvingManager = approvingManager;
	}

	public byte[] getReciept() {
		return reciept;
	}

	public void setReciept(byte[] reciept) {
		this.reciept = reciept;
	}

	//The object hashcode is the ID, status, normally part of the auto generated
	//hashcode has been removed (otherwise duplicate entries 19283 when approved is added as 19284)
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((empUsername == null) ? 0 : empUsername.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
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
		Reimbursement other = (Reimbursement) obj;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (approvingManager == null) {
			if (other.approvingManager != null)
				return false;
		} else if (!approvingManager.equals(other.approvingManager))
			return false;
		if (empUsername == null) {
			if (other.empUsername != null)
				return false;
		} else if (!empUsername.equals(other.empUsername))
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Reimbursement [id=" + id + ", empUsername=" + empUsername + ", status=" + status + ", amount=" + amount
				+ ", notes=" + notes + "]";
	}
	
}
