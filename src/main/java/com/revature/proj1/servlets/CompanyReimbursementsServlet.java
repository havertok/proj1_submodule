package com.revature.proj1.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.proj1.utils.CompanyDBUtilities;

/**
 * Servlet implementation class CompanyReimbursementsServlet
 */
@WebServlet("/listReimsByStatus")
public class CompanyReimbursementsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CompanyReimbursementsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//defaults to approved reims
		response.getWriter().write((new ObjectMapper()).writeValueAsString(CompanyDBUtilities.grabApprovedReimbursements()));
	}

	/**
	 * Sends either: 1 to get all approved Reims, 0 for all pending reims, and a manager name, for all reims with
	 * an approving manager of that name (we shouldn't be able to call a man that doesn't exist)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String determinant = request.getReader().readLine();
		//System.out.println("doPost CompanyReimServlet: "+determinant);
		if(determinant == null) {
			response.sendRedirect("/404");
		}
		switch (determinant) {
		case "1":
			response.getWriter().write((new ObjectMapper()).writeValueAsString(CompanyDBUtilities.grabApprovedReimbursements()));
			break;
		case "0":
			response.getWriter().write((new ObjectMapper()).writeValueAsString(CompanyDBUtilities.getPendingReimbursements()));
			break;
		default:
			response.getWriter().write((new ObjectMapper()).writeValueAsString(CompanyDBUtilities.grabApprovedReimsByName(determinant)));
			break;
		}
	}

}
