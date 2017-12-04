package edu.uga.cs.rentaride.presentation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.uga.cs.rentaride.entity.Reservation;
import edu.uga.cs.rentaride.logic.LogicLayer;
import edu.uga.cs.rentaride.session.Session;
import edu.uga.cs.rentaride.session.SessionManager;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


@WebServlet("/BrowseReservations")
public class BrowseReservations 
extends HttpServlet  {

	private static final long serialVersionUID = 1L;

	static  String            templateDir = "WEB-INF/templates";
	static  String            resultTemplateName = "BrowseReservations.ftl";

	private Configuration     cfg;

	public void init() 
	{
		
		cfg = new Configuration();
		cfg.setServletContextForTemplateLoading( getServletContext(), "WEB-INF/templates" );
	}

	public void doGet(HttpServletRequest  req, HttpServletResponse res) throws ServletException, IOException {

		Template            resultTemplate = null;
		BufferedWriter      toClient = null;
		LogicLayer	        logicLayer = null;
		List<Reservation> 	resList = null; 
		List<List<Object>> 	reservations = null;
		List<Object>			reservation = null; 
		Reservation			r  = null;
		HttpSession	     	httpSession;
		Session            	session;
		String             	ssid;

		
		try {
			resultTemplate = cfg.getTemplate( resultTemplateName );
		} 
		catch( IOException e ) {
			throw new ServletException( 
					"Can't load template in: " + templateDir + ": " + e.toString());
		}

		
		toClient = new BufferedWriter(
				new OutputStreamWriter( res.getOutputStream(), resultTemplate.getEncoding() )
				);

		res.setContentType("text/html; charset=" + resultTemplate.getEncoding());

		httpSession = req.getSession();
		if( httpSession == null ) {      
			RARError.error( cfg, toClient, "Not Logged IN!!" );
			return;
		}

		ssid = (String) httpSession.getAttribute( "ssid" );
		if( ssid == null ) {       
			RARError.error( cfg, toClient, "Not Logged IN!!" );
			return;
		}

		session = SessionManager.getSessionById( ssid );
		if( session == null ) {
			RARError.error( cfg, toClient, "Session expired or illegal; please log in" );
			return; 
		}

		logicLayer = session.getLogicLayer();
		if( logicLayer == null ) {
			RARError.error( cfg, toClient, "Session expired or illegal; please log in" );
			return; 
		}

		
		Map<String,Object> root = new HashMap<String,Object>();

		try {

			System.out.println("BrowseReservations: Attempting to grab all reservations...");

			
			if (session.getUser().getClass().getName() == "edu.uga.cs.rentaride.entity.impl.CustomerImpl") { 
				
				int id = (int) session.getUser().getId();
				resList = logicLayer.findAllReservations(id);
				
			}
			else if (session.getUser().getClass().getName() == "edu.uga.cs.rentaride.entity.impl.AdministratorImpl") {
				
				resList = logicLayer.findAllReservations();
			}

			System.out.println("BrowseReservations rv.toString: " + resList.toString());

			
			reservations = new LinkedList<List<Object>>();
			root.put( "reservations", reservations );

			for( int i = 0; i < resList.size(); i++ ) {
				r = (Reservation) resList.get( i );
				reservation = new LinkedList<Object>();
				reservation.add( r.getId() );
				reservation.add( r.getCustomer().getId() );
				reservation.add( r.getRentalLocation().getId() );
				reservation.add( r.getVehicleType().getId() );
				reservation.add( r.getPickupTime() );
				reservation.add( r.getLength() );		

				reservations.add( reservation );
			}

			System.out.println("BrowseReservations: Exited for loop");
			System.out.println("BrowseReservations: reservations.toString() " + reservations.toString());
		} 
		catch( Exception e) {
			RARError.error( cfg, toClient, e );
			System.out.println("BrowseReservations Servlet: Failed to get all reservations");
			return;
		}

		
		try {
			resultTemplate.process( root, toClient );
			toClient.flush();
		} 
		catch (TemplateException e) {
			throw new ServletException( "Error while processing FreeMarker template", e);
		}

		toClient.close();

	}
}