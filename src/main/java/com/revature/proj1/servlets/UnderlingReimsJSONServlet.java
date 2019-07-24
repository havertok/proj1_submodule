package com.revature.proj1.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.proj1.utils.CompanyDBUtilities;
import com.revature.service.ChangeReimStatusService;

/**
 * Servlet implementation class UnderlingReimsJSONServlet
 */
@MultipartConfig 
@WebServlet(description = "Special Reim Servlet that needs username as a param in request", urlPatterns = {
		"/underlingreims" })
public class UnderlingReimsJSONServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UnderlingReimsJSONServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	// This is used by a manager to approve/reject a reimbursement
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//System.out.println("doPost UnderlingReimsJSONServlet");
		ChangeReimStatusService cs = new ChangeReimStatusService();
		cs.changeStatus(Integer.parseInt(request.getParameter("reimId")), 
				request.getParameter("toStatus"), request.getParameter("manName"));
		response.setStatus(202); //accepted
	}

}
