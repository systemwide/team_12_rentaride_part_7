package edu.uga.cs.rentaride.presentation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.impl.CustomerImpl;
import edu.uga.cs.rentaride.object.ObjectLayer;
import edu.uga.cs.rentaride.object.impl.ObjectLayerImpl;
import edu.uga.cs.rentaride.persistence.impl.*;


/**
 * Servlet implementation class signup_servlet
 */
@WebServlet("/signup_servlet")
public class signup_servlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public signup_servlet()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	private boolean registeredSuccessfully(Connection con, String firstname, String lastname, String username,
			String email, String licenseNo, String licenseState, String pass, String passRepeat,
			HttpServletRequest request, HttpServletResponse response)
	{
		// use logic layer methods to insert into database
		// if no error return true;
		// else return false;
		String address = "543 Second Street, Athens, GA 30605";
		String createDate = "0000-00-00 00:00:00";
		String memberUntil = "0000-00-00";
		String ccNumb = "placeholder";
		String ccExp = "0000-00-00";
		String status = "active";
		long tempID = -1;
		CustomerImpl cust = new CustomerImpl( tempID, firstname, lastname, username, email, pass, licenseNo, licenseState, address, createDate, memberUntil, ccNumb, ccExp, status); 
		String query = "INSERT INTO Customer (firstName, lastName, userName, email, password, licState, licNumber, address, createDate, memberUntil, ccNumb, ccExp, status) "
				+ "VALUES ('" + firstname + "','" + lastname + "','" + username + "','" + email + "', '" + pass + "', '"
				+ licenseState + "', '" + licenseNo + "','" + address + "', '" + createDate + "', '" + memberUntil + "', '"+ ccNumb + "', '" + ccExp + "', '"+ status +"');";
		Statement stmt = null;
		ObjectLayer obj = new ObjectLayerImpl();
		CustomerManager cm = new CustomerManager(con, obj);
		try {
			cm.store(cust);
		} catch (RARException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try
		{
			stmt = con.createStatement();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		try
		{
			stmt.executeUpdate(query);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		System.out.println("Inserted " + firstname + " Into Customer Table");
		return true;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		System.out.println("Connecting to database...");
		Connection con = DatabaseAccess.connect(); // change database access class to team12 database
		System.out.println("Connected to database successfully.\n");
		// getServletConfig().getServletContext().getRequestDispatcher("/admin_login.html").forward(request,
		// response);
		PrintWriter out = response.getWriter();
		out.println("<html><head>");
		out.println("<title>Signup</title>");
		out.println("</head>");
		out.println("<body>");
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String licenseNo = request.getParameter("licenseNo");
		String licenseState = request.getParameter("licenseState");
		String pass = request.getParameter("psw");
		String passRepeat = request.getParameter("psw-repeat");

		if (!pass.equalsIgnoreCase(passRepeat)) // user did not correctly enter passwords
		{
			getServletConfig().getServletContext().getRequestDispatcher("/invalid_signup.html").forward(request,
					response);
		}

		if (registeredSuccessfully(con, firstname, lastname, username, email, licenseNo, licenseState, pass, passRepeat,
				request, response))
		{
			HttpSession session = request.getSession();
			session.setAttribute("name", firstname);
			getServletConfig().getServletContext().getRequestDispatcher("/user_browse.html").forward(request, response);
		}
		out.println("</body></html>");
		try
		{
			con.close();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}