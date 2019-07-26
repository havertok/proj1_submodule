package com.revature.proj1.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Logger;

import com.revature.proj1.beans.Credentials;
import com.revature.proj1.beans.Employee;
import com.revature.service.AuthenticationService;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AuthenticationService as = new AuthenticationService();

	/**
	 * Default constructor.
	 */
	public LoginServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("login.html").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate"); //should disable caching
		
		HttpSession session = request.getSession();
		Credentials creds = new Credentials(request.getParameter("username"), request.getParameter("password"));
		Employee emp = as.authenticateEmployee(creds);
		//System.out.println("LoginServlet::44" + emp);
		if (emp != null) {
			session.setAttribute("username", emp.getUsername());
			session.setAttribute("fname", emp.getFname());
			session.setAttribute("lname", emp.getLname());
			session.setAttribute("email", emp.getEmail());
			if (emp.getManagername() != null) {
				session.setAttribute("managername", emp.getManagername());
			} else {
				session.setAttribute("managername", "N/A");
			}
			session.setAttribute("myReimbursements", emp.getMyReimbursements());
			session.setAttribute("underlings", emp.getUnderlings());
			session.setAttribute("validUser", emp.getUsername());
			//Prevents browser from caching the session
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.sendRedirect("mainmenu");
		} else {
			session.setAttribute("validUser", "No such user or invalid password");
			response.sendRedirect("login");
		}

	}

}
