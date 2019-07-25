package com.revature.proj1.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.proj1.beans.Employee;
import com.revature.proj1.beans.Reimbursement;

@WebServlet("/session")
public class SessionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// return a JSON representation of the currently authenticated user for this
	// JSESSIONID.
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate"); //Should disable caching
		
		HttpSession session = req.getSession(false);
		if (session != null && session.getAttribute("username") != null) {
			try {
				String username = session.getAttribute("username").toString();
				String fname = session.getAttribute("fname").toString();
				String lname = session.getAttribute("lname").toString();
				String email = session.getAttribute("email").toString();
				String supervisor = session.getAttribute("managername").toString();
				
				@SuppressWarnings("unchecked")
				List<Reimbursement> myReims = (ArrayList<Reimbursement>) session.getAttribute("myReimbursements");
				Employee emp = new Employee(username, null, email, fname, lname, supervisor);
				emp.setMyReimbursements(myReims);
				//This is what sends our JSON object
				resp.getWriter().write((new ObjectMapper()).writeValueAsString(emp));
			} catch (Exception e) {
				e.printStackTrace();
				resp.getWriter().write("{\"session\":null}");
			}
		} else {
			if(session == null) {
				resp.sendRedirect("403.html");
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
