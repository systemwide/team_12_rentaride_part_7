package edu.uga.cs.rentaride.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class signup_servlet
 */
@WebServlet("/signup_servlet")
public class signup_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public signup_servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		//System.out.println("Connecting to database...");
		//Connection con = DatabaseAccess.connect(); //change database access class to team12 database
		//System.out.println("Connected to database successfully.\n");
		//getServletConfig().getServletContext().getRequestDispatcher("/admin_login.html").forward(request, response);
		PrintWriter out = response.getWriter();
		out.println("<html><head>");
		out.println("<title>Signup</title>");
		out.println("</head>");
		out.println("<body>");
		String email = request.getParameter("email");
		String licenseNo = request.getParameter("licenseNo");
		String licenseState = request.getParameter("licenseState");
		String pass = request.getParameter("psw");
		String passRepeat = request.getParameter("psw-repeat");
		
		if(!pass.equalsIgnoreCase(passRepeat)) //user did not correctly enter passwords 
		{
			getServletConfig().getServletContext().getRequestDispatcher("/invalid_signup.html").forward(request, response);
		}
		
		if(registeredSuccessfully(email, licenseNo, licenseState, pass, passRepeat))
		{
			HttpSession session=request.getSession();  
			session.setAttribute("email",email); 
			getServletConfig().getServletContext().getRequestDispatcher("/user_browse.html").forward(request, response);
		}
		out.println("</body></html>");
	}

	private boolean registeredSuccessfully(String email, String licenseNo, String licenseState, String pass, String passRepeat)
	{
		//use logic layer methods to insert into database
		//if no error return true;
		//else return false;
		return true; //change to false later
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}