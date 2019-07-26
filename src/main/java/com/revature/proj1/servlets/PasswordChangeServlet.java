package com.revature.proj1.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.proj1.beans.Employee;
import com.revature.proj1.utils.CompanyDBUtilities;

@WebServlet("/password")
public class PasswordChangeServlet extends HttpServlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if(session != null) {
			try {
				String username = session.getAttribute("username").toString();
				String password = req.getParameter("passToChange");
				Employee e = CompanyDBUtilities.getEmployeeByName(username);
				e.setPassword(password);
				CompanyDBUtilities.addEmployeeDB(e);
				resp.sendRedirect("login.html");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			resp.sendError(404);
		}
	}
	
}
