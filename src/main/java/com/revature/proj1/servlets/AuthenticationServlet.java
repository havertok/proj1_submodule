package com.revature.proj1.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.proj1.beans.Credentials;
import com.revature.proj1.beans.Employee;
import com.revature.proj1.utils.CompanyDBUtilities;

/**
 * in the future, send the login POST req to this servlet, which will redirect
 */
@WebServlet("/authenticate")
public class AuthenticationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthenticationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Credentials creds = new Credentials(request.getParameter("username"), request.getParameter("password"));
		Employee eCheck = CompanyDBUtilities.getEmployeeByName(creds.getUsername());
		if(eCheck != null && eCheck.getPassword().equals(creds.getPassword())) {
			response.sendRedirect("mainmenu.html");
		} else {
			response.sendError(451, "Either User does not exist or password incorrect.");
		}

	}

}
