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

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.RentalLocation;
import edu.uga.cs.rentaride.logic.LogicLayer;
import edu.uga.cs.rentaride.session.Session;
import edu.uga.cs.rentaride.session.SessionManager;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@WebServlet("/BrowseRentalLocations")

public class BrowseRentalLocations 
extends HttpServlet 

{
	private static final long serialVersionUID = 1L;

	static  String            templateDir = "WEB-INF/templates";
	static  String            resultTemplateName = "BrowseRentalLocations.ftl";

	private Configuration     cfg;

	public void init() 
	{
		// Prepare the FreeMarker configuration;
		// - Load templates from the WEB-INF/templates directory of the Web app.
		//
		cfg = new Configuration();
		cfg.setServletContextForTemplateLoading( getServletContext(), "WEB-INF/templates" );
	}

	public void doGet( HttpServletRequest  req, HttpServletResponse res )
			throws ServletException, IOException
	{
		Template                resultTemplate = null;
		BufferedWriter          toClient = null;
		LogicLayer              logicLayer = null;
		List<RentalLocation>    locs = null;
		List<List<Object>>      rentalLocations = null;
		List<Object>            rentalLocation = null;
		RentalLocation   	    rl  = null;
		HttpSession             httpSession;
		Session                 session;
		String                  ssid;


		// Load templates from the WEB-INF/templates directory of the Web app.
		//
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
			RARError.error( cfg, toClient, "Session expired or illegal; please log in" );
			return;
		}

		ssid = (String) httpSession.getAttribute( "ssid" );
		if( ssid == null ) {      
			RARError.error( cfg, toClient, "Session expired or illegal; please log in" );
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
				locs = logicLayer.findAllLocations();
				rentalLocations = new LinkedList<List<Object>>();
				root.put( "rentalLocations", rentalLocations );
				
				for( int i = 0; i <locs.size(); i++ ) {
	                rl = (RentalLocation) locs.get( i );
	                rentalLocation = new LinkedList<Object>();
	                rentalLocation.add( rl.getName() );
	                rentalLocation.add( rl.getAddress() );
	                rentalLocation.add( rl.getCapacity() );
	                rentalLocations.add( rentalLocation );
	            }// end for
			} catch (RARException e) {

				e.printStackTrace();
			}// end try/catch
			
			try {
	            resultTemplate.process( root, toClient );
	            toClient.flush();
	        } 
	        catch (TemplateException e) {
	            throw new ServletException( "Error while processing FreeMarker template", e);
	        }

	        toClient.close();


		
	}//
}// end class
