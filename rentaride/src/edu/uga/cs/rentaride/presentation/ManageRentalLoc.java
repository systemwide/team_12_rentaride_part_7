package edu.uga.cs.rentaride.presentation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
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
import edu.uga.cs.rentaride.entity.RentalLocation;
import edu.uga.cs.rentaride.logic.LogicLayer;
import edu.uga.cs.rentaride.logic.impl.LogicLayerImpl;
import edu.uga.cs.rentaride.session.Session;
import edu.uga.cs.rentaride.session.SessionManager;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import javax.servlet.annotation.WebServlet;

//locName, locAddress, locCoord, 
@WebServlet("/ManageRentalLoc")
public class ManageRentalLoc extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    static  String         templateDir = "WEB-INF/templates";
    static  String         resultTemplateName = "CreateLocationLocList-Result.ftl";

    private Configuration  cfg; 

    public void init() 
    {
        // Prepare the FreeMarker configuration;
        // - Load templates from the WEB-INF/templates directory of the Web app.
        //
        cfg = new Configuration();
        cfg.setServletContextForTemplateLoading(
                getServletContext(), 
                "WEB-INF/templates"
                );
    }

    public void doPost( HttpServletRequest req, HttpServletResponse res )
            throws ServletException, IOException
    {
        Template       resultTemplate = null;
        BufferedWriter toClient = null;
        String	       loc_name = null;
        String	       loc_addr = null;
        String         loc_cap_str;
        String update_button;
        String delete_button;
        int           loc_cap;
        long	       loc_id = 0;
        LogicLayer     logicLayer = null;
        List<RentalLocation>           rvlocation = null;
        List<List<Object>>     locations = null;
        List<Object>           location = null;
        RentalLocation         rentalLoc  = null;
        HttpSession    httpSession;
        Session        session;
        String         ssid;
        Connection dbConn = null;
        String locInput = null;
        String addrInput = null;
        String capInput = null;


        // Load templates from the WEB-INF/templates directory of the Web app.
        //
        try {
            resultTemplate = cfg.getTemplate( resultTemplateName );
        } 
        catch (IOException e) {
            throw new ServletException( 
                    "Can't load template in: " + templateDir + ": " + e.toString());
        }

        // Prepare the HTTP response:
        // - Use the charset of template for the output
        // - Use text/html MIME-type
        //
        toClient = new BufferedWriter(
                new OutputStreamWriter( res.getOutputStream(), resultTemplate.getEncoding() )
                );

        res.setContentType("text/html; charset=" + resultTemplate.getEncoding());

/*        httpSession = req.getSession();
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
            new RARException("Session expired or illegal; please log in" );
            return; 
        }*/

        //try {
			dbConn = DatabaseAccess.connect();
		//} catch(RARException e) {
			
		//}//try-catch
		logicLayer = new LogicLayerImpl(dbConn);
		
        
        // Get the form parameters
        ////locName, locAddress, locCoord, 
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
            new RARException("Unspecified location name or location address" );
            return;
        }

        if( loc_cap_str == null ) {
            new RARException("Unspecified capacity" );
            return;
        }

        try {
            loc_cap = Integer.parseInt( loc_cap_str );
        }
        catch( Exception e ) {
            new RARException("Capacity should be a number" );
            return;
        }

        if( loc_cap < 0 ) {
            new RARException("Capacity must be a non-negative number" );
            return;
        }

        try {
        	if(update_button!=null) {
        		long update_val = Long.parseLong(update_button);
        		logicLayer.updateRentalLocation(update_val, loc_name, loc_addr, loc_cap );
        	}//if
        	else if(delete_button!=null) {
        		long delete_val = Long.parseLong(delete_button);
        		logicLayer.deleteRentalLocation(delete_val, loc_name, loc_addr, loc_cap);
        	}//else if
        } 
        catch ( Exception e ) {
            new RARException( e );
            return;
        }
/*
        // Setup the data-model
        //
        Map<String,Object> root = new HashMap<String,Object>();

        // Build the data-model
        //
        root.put( "club_name", loc_name );
        root.put( "club_id", new Long( loc_id ) );

        // Merge the data-model and the template
        //
        try {
            resultTemplate.process( root, toClient );
            toClient.flush();
        } 
        catch (TemplateException e) {
            throw new ServletException( "Error while processing FreeMarker template", e);
        }
*/
        Map<String,Object> root = new HashMap<String,Object>();

        // Build the data-model
        //
        locations = new LinkedList<List<Object>>();

        try {
            rvlocation = logicLayer.findAllLocations();
        } 
        catch( Exception e ) {
	    //            ClubsError.error( cfg, toClient, e );
            return;
        }

        root.put( "locations", locations );
	
        for( int i = 0; i < rvlocation.size(); i++ ) {
            rentalLoc = (RentalLocation) rvlocation.get( i );
            location = new LinkedList<Object>();
            location.add( new Long( rentalLoc.getId() ) );
            location.add( rentalLoc.getName() );
            location.add( rentalLoc.getAddress() );
	    location.add( rentalLoc.getCapacity());
            locations.add( location );
        }

        // Merge the data-model and the template
        //
        try {
            resultTemplate.process( root, toClient );
            toClient.flush();
        } 
        catch (TemplateException e) {
            throw new ServletException( "Error while processing FreeMarker template", e);
        }

        toClient.close();
  }
}//AddRentalLoc
