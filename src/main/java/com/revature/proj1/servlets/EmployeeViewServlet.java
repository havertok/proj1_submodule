package com.revature.proj1.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.proj1.beans.Employee;
import com.revature.proj1.utils.CompanyDBUtilities;


/**
 * Servlet implementation class EmployeeViewServlet
 */
@WebServlet("/employeeview")
public class EmployeeViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeeViewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	//Employee reims GET will return a list of all reimbursements, underling list will change the status of those reims
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("did get EmpViewServ");
		String username = request.getReader().readLine();
		Employee emp = CompanyDBUtilities.getEmployeeByName(username);
		
		response.getWriter().write((new ObjectMapper()).writeValueAsString(emp.getMyReimbursements()));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Did Post EmpViewServ POST");
		String username = request.getReader().readLine();
		System.out.println("username:"+username);
		Employee emp = CompanyDBUtilities.getEmployeeByName(username);
		
		response.getWriter().write((new ObjectMapper()).writeValueAsString(emp.getMyReimbursements()));
		
	}

}
