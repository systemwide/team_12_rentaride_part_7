package edu.uga.cs.rentaride.presentation;

import java.io.*;
import java.util.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.Comment;
import edu.uga.cs.rentaride.entity.RentalLocation;
import edu.uga.cs.rentaride.logic.LogicLayer;
import edu.uga.cs.rentaride.logic.impl.LogicLayerImpl;
//import edu.uga.cs.rentaride.persistence.impl.DbUtils;
import edu.uga.cs.rentaride.session.Session;
import edu.uga.cs.rentaride.session.SessionManager;
//import edu.uga.cs.rentaride.persistence.impl.DbUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import javax.servlet.annotation.WebServlet;
import java.sql.Connection;

//locName, locAddress, locCoord, 
@WebServlet("/AddRentalLoc")
public class SimpleServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) {
		PrintWriter out = null;
		List<RentalLocation>           rvlocation = null;
		LogicLayer     logicLayer = null;
		Connection dbConn = null;
		RentalLocation rentalLoc = null;
		List<Comment>           rvcomment = null;
		Comment myComment = null;
		
		res.setContentType("text/html");
		try{
			out = res.getWriter();
		}catch(IOException ioe){
			System.out.println("HelloServlet: "+ ioe);
		}//try-catch

		
		//try {
			dbConn = DatabaseAccess.connect();
		//} catch(RARException e) {
		//	out.println("RARException:: Unable to connect to database!" + e);
		//}//try-catch
		
		logicLayer = new LogicLayerImpl(dbConn);
		
		res.setContentType("text/html");
		/*try {
			rvcomment = logicLayer.findAllComments();
		} 
		catch( Exception e ) {
			out.println("couldn't process the call to logic.findAllComments! " + e.toString());
			return;
		}
		
		
		
		try{
			out = res.getWriter();
		}catch(IOException ioe){
			System.out.println("HelloServlet: "+ ioe);
		}//try-catch
		*/
		try {
            rvlocation = logicLayer.findAllLocations();
        } 
        catch( Exception e ) {
        	out.println("Error in finding all locations: " +e.toString());
            return;
        }
		
		out.println("<html><body>");
		for	(int i=0; i<rvlocation.size();++i) {
            rentalLoc = (RentalLocation) rvlocation.get( i );
            out.println("Name: "+ rentalLoc.getName());
		}
		/*for( int i = 0; i < rvcomment.size(); i++ ) {
			myComment = (Comment) rvcomment.get( i );
			//comment = new LinkedList<Object>();
			out.println("\nID = "+ myComment.getId() ) ;
			if(myComment.getCustomer()!=null)
				out.println("\nCustomerusername = "+ myComment.getCustomer().getUserName() );
			else
				out.println("customer is null :(");
			out.println("\nText = "+ myComment.getText() );
			if(myComment.getRental()!=null) {
				//out.println("Rental is not null!");
				if(myComment.getRental().getVehicle()!=null)
					out.println("\nVehicleID = "+ myComment.getRental().getVehicle().getId());
			}
			out.println("\nDate = " + myComment.getDate());
			if((myComment.getRental()!=null)&&(myComment.getRental().getReservation()!=null)) {
				out.println("\nReservationID = " + myComment.getRental().getReservation().getId());
			}
			//comments.add( comment );
		}*/
		out.println("</body></html>");

	}

	public void doPost(HttpServletRequest req, HttpServletResponse res){
		PrintWriter out = null;
		String title = "After save!";
		//Vector toresult = null;
		//Vector fromresult = null;
		//String fromquery = null;
		//String toquery = null;
		LogicLayer     logicLayer = null;
		String	       loc_name = null;
		String	       loc_addr = null;
		String         loc_cap_str = null;
		int           loc_cap;
		long	       loc_id = 0;
		Session        session;
		String         ssid;
		HttpSession    httpSession;
		Connection dbConn = null;
        String update_button;
        String delete_button;
        List<RentalLocation>           rvlocation = null;
        RentalLocation rentalLoc = null;
        String locInput = null;
        String addrInput = null;
        String capInput = null;



		res.setContentType("text/html");
		try{
			out = res.getWriter();
		}catch(IOException ioe){
			System.out.println("HelloServlet: "+ ioe);
		}//try-catch

		/*httpSession = req.getSession();
        if( httpSession == null ) {       // assume not logged in!
           new RARException("Session expired or illegal; please log in" );
           return;
        }

        ssid = (String) httpSession.getAttribute( "ssid" );
        if( ssid == null ) {       // not logged in!
            new RARException("Session expired or illegal; please log in" );
            return;
        }

        session = SessionManager.getSessionById( ssid );
        if( session == null ) {
            new RARException("Session expired or illegal; please log in" );
            return; 
        }

        logicLayer = session.getLogicLayer();
        if( logicLayer == null ) {
        	out.println("Session expired or illegal; please log in");
            new RARException("Session expired or illegal; please log in" );
            return; 
        }
		try {
			dbConn = DatabaseAccess.connect();
		} catch(RARException e) {
			out.println("RARException:: Unable to connect to database!" + e);
		}//try-catch
		if(dbConn != null) {
			out.println("Somehow connected?? idk maybe?");
		}
		logicLayer = new LogicLayerImpl(dbConn);

		/*loc_name = req.getParameter( "locName" );
		loc_addr = req.getParameter( "locAddress" );
		loc_cap_str = req.getParameter( "locCapacity" );
		if( loc_name == null || loc_addr == null ) {
			out.println("Unspecified location name or location address");
			new RARException("Unspecified location name or location address" );
			return;
		}

		if( loc_cap_str == null ) {
			out.println("Unspecified capacity");
			new RARException("Unspecified capacity" );
			return;
		}

		try {
			loc_cap = Integer.parseInt( loc_cap_str );
		}
		catch( Exception e ) {
			out.println("Capacity should be a number");
			new RARException("Capacity should be a number" );
			return;
		}

		if( loc_cap < 0 ) {
			new RARException("Capacity must be a non-negative number" );
			return;
		}
		out.println("<p>before creating rentalLocation</p>");
		try {
			loc_id = logicLayer.createRentalLocation( loc_name, loc_addr, loc_cap );
		} 
		catch ( Exception e ) {
			out.println("error from logic layer! Exception: " + e.toString());
			new RARException( e );
			return;
		}
		
        update_button = req.getParameter("updateLoc");
        delete_button = req.getParameter("deleteLoc");

        if(update_button!=null) {
        	locInput = "editLocName" + update_button;
        	addrInput = "editLocAddress"+update_button;
        	capInput = "editLocCap"+update_button;
        }
        else if(delete_button!=null) {
        	locInput = "editLocName" + delete_button;
        	addrInput = "editLocAddress"+delete_button;
        	capInput = "editLocCap"+delete_button;
        }
        loc_name = req.getParameter( locInput );
        loc_addr = req.getParameter( addrInput );
        loc_cap_str = req.getParameter( capInput );
        
        if( loc_name == null || loc_addr == null ) {
            out.println("RARException: Unspecified location name or location address" );
            return;
        }

        if( loc_cap_str == null ) {
        	out.println("RARException: Unspecified capacity" );
            return;
        }

        try {
            loc_cap = Integer.parseInt( loc_cap_str );
        }
        catch( Exception e ) {
        	out.println("RARException: Capacity should be a number" );
            return;
        }

        if( loc_cap < 0 ) {
        	out.println("RARException: Capacity must be a non-negative number" );
            return;
        }

        try {
        	if(update_button!=null) {
        		out.println("update_button = " + update_button);
        		long update_val = Long.parseLong(update_button);
        		logicLayer.updateRentalLocation(update_val, loc_name, loc_addr, loc_cap );
        	}//if
        	else if(delete_button!=null) {
        		long delete_val = Long.parseLong(delete_button);
        		logicLayer.deleteRentalLocation(delete_val, loc_name, loc_addr, loc_cap);
        	}//else if
        } catch ( Exception e ) {
        	out.println("RARException after update/delete: "+ e );
            return;
        } 

		/*out.println("<html><head><title>" + title+ "</title></head>");
		out.println("<body>");

		out.println("<h2>" + title + "</h2>");

		out.println("<p> The location id returned was :" + loc_id);

		out.println("</body>");
		out.println("</html>");*/
		
		//try {
			dbConn = DatabaseAccess.connect();
		//} catch(RARException e) {
			
		//}//try-catch
		logicLayer = new LogicLayerImpl(dbConn);
		

        // Get the form parameters
        ////locName, locAddress, locCoord, 
        loc_name = req.getParameter( "locName" );
        loc_addr = req.getParameter( "locAddress" );
        loc_cap_str = req.getParameter( "locCapacity" );

        if( loc_name == null || loc_addr == null ) {
        	out.println("Unspecified location name or location address!");
            new RARException("Unspecified location name or location address" );
            return;
        }

        if( loc_cap_str == null ) {
        	out.println("Unspecified capacity!");
            new RARException("Unspecified capacity" );
            return;
        }

        try {
            loc_cap = Integer.parseInt( loc_cap_str );
        }
        catch( Exception e ) {
        	out.println("Capacity should be a number!");
            new RARException("Capacity should be a number" );
            return;
        }

        if( loc_cap < 0 ) {
        	out.println("Capacity much be a non-negative number");
            new RARException("Capacity must be a non-negative number" );
            return;
        }

        try {
            loc_id = logicLayer.createRentalLocation( loc_name, loc_addr, loc_cap );
        } 
        catch ( Exception e ) {
        	out.println("Exception caught freom logic.createRentalLocation "+ e.toString());
            new RARException( e );
            return;
        }
        
		try {
            rvlocation = logicLayer.findAllLocations();
        } 
        catch( Exception e ) {
        	out.println("Error in finding all locations: " +e.toString());
            return;
        }
		
		out.println("<html><body>");
		for	(int i=0; i<rvlocation.size();++i) {
            rentalLoc = (RentalLocation) rvlocation.get( i );
            out.println("Name: "+ rentalLoc.getName());
		}
		out.println("</body></html>");


		out.close();
	}//doPost
}//simple servlet