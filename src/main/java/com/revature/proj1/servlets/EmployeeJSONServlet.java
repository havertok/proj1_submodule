package com.revature.proj1.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.proj1.beans.Employee;
import com.revature.proj1.utils.CompanyDBUtilities;

@WebServlet("/getunderlings")
public class EmployeeJSONServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EmployeeJSONServlet() {
		super();
	}

	/**
	 * GET gets all the subordinates
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// response.setHeader("Cache-Control", "private, no-store, no-cache,
		// must-revalidate"); //should disable caching

		HttpSession session = request.getSession(false);
		if (session == null) {
			response.sendError(403);
		} else {
			String username = session.getAttribute("username").toString();
			Employee emp = CompanyDBUtilities.getEmployeeByName(username);
			response.getWriter().write((new ObjectMapper()).writeValueAsString(emp.getUnderlings()));
		}
	}

	/**
	 * POST gets all the managers
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.getWriter().write((new ObjectMapper()).writeValueAsString(CompanyDBUtilities.grabManagers()));
	}
}
