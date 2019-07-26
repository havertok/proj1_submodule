package com.revature.service;

import javax.mail.MessagingException;

import com.revature.proj1.beans.Employee;
import com.revature.proj1.beans.Reimbursement;
import com.revature.proj1.utils.CompanyDBUtilities;

public class ChangeReimStatusService {

	public void changeStatus(int id, String toStatusStr, String manName) {

		int toStatus = 0;
		if (toStatusStr.equals("accept")) {
			toStatus = 1;
		} else if (toStatusStr.equals("reject")) {
			toStatus = 2;
		}

		CompanyDBUtilities.approveReim(id, toStatus, manName);
		Reimbursement reim = CompanyDBUtilities.getReimById(id);
		Employee toEmp = CompanyDBUtilities.getEmployeeByName(reim.getEmpUsername());
		sendMail(toEmp, reim);
	}

	// use employee and reim to build message
	// String host, String port, String username, String password, String toAddress,
	// String subject, String message
	public void sendMail(Employee toEmp, Reimbursement reim) {
		String host = "smtp.gmail.com";
		String port = "587";
		String username = "edward.rob.owen";
		String password = "Banhammer732!";

		String statusString = reim.getStatus() == 1 ? "APPROVED by " + toEmp.getManagername() : "REJECTED!";

		String mailTo = toEmp.getEmail();
		String subject = "Important message concerning Reimbursement:" + reim.getId();
		String message = "Hello, " + toEmp.getFname() + " " + toEmp.getLname() + "." + "\n"
				+ "\tYour reimbursement for: " + reim.getNotes() + ".\n" + "Valued at $" + reim.getAmount() + " on "
				+ reim.getDateMade() + " has been: " + statusString + "\nHave a nice day!";

		MailService mailGnome = new MailService();
		try {
			mailGnome.sendPlainTextEmail(host, port, username, password, mailTo, subject, message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
