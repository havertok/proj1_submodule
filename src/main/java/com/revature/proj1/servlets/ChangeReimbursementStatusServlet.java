package com.revature.proj1.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.proj1.beans.Reimbursement;
import com.revature.proj1.utils.CompanyDBUtilities;
import com.revature.service.generateNewReimbursement;

/**
 * Servlet implementation class ChangeReimbursementStatusServlet
 */
@WebServlet(name = "addnewreim", urlPatterns = { "/addnewreim" })
public class ChangeReimbursementStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeReimbursementStatusServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doPost ChangeReimStatusServlet \n Amount:"+ request.getParameter("amount")+" notes: "+request.getParameter("notes"));
		generateNewReimbursement gnr = new generateNewReimbursement();
		Reimbursement newReim = null;
		String username;
		HttpSession session = request.getSession(false);
		if(session != null) {
			try {
				username = session.getAttribute("username").toString();
				newReim = gnr.generateReimbursement(Double.parseDouble(request.getParameter("amount")), 
						request.getParameter("notes"), username);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println(newReim);
		CompanyDBUtilities.addUpdateReim(newReim);
		request.getRequestDispatcher("mainmenu.html").forward(request , response);
	}

}
