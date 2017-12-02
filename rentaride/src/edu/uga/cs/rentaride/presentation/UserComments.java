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
import edu.uga.cs.rentaride.entity.Comment;
import edu.uga.cs.rentaride.logic.LogicLayer;
import edu.uga.cs.rentaride.logic.impl.LogicLayerImpl;
import edu.uga.cs.rentaride.session.Session;
import edu.uga.cs.rentaride.session.SessionManager;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import javax.servlet.annotation.WebServlet;

@WebServlet("/UserComments")
public class UserComments 
extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static  String         templateDir = "WEB-INF/templates";
	static  String         resultTemplateName = "UserComments-Results.ftl";

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

	public void doGet( HttpServletRequest req, HttpServletResponse res )
			throws ServletException, IOException
	{
		Template               resultTemplate = null;
		BufferedWriter         toClient = null;
		LogicLayer             logicLayer = null;
		List<Comment>           rvcomment = null;
		List<List<Object>>     comments = null;
		List<Object>           comment = null;
		Comment         myComment  = null;
		HttpSession            httpSession;
		Session                session;
		String                 ssid;
		Connection dbConn = null;


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

		httpSession = req.getSession();
		/*if( httpSession == null ) {       // not logged in!
	    //            ClubsError.error( cfg, toClient, "Session expired or illegal; please log in" );
            return;
        }

        ssid = (String) httpSession.getAttribute( "ssid" );
        if( ssid == null ) {       // assume not logged in!
	    //            ClubsError.error( cfg, toClient, "Session expired or illegal; please log in" );
            return;
        }

        session = SessionManager.getSessionById( ssid );
        if( session == null ) {
	    //            ClubsError.error( cfg, toClient, "Session expired or illegal; please log in" );
            return; 
        }

        logicLayer = session.getLogicLayer();
        if( logicLayer == null ) {
	    //            ClubsError.error( cfg, toClient, "Session expired or illegal; please log in" );
            return; 
        }*/

		//try {
		dbConn = DatabaseAccess.connect();
		//} catch(RARException e) {

		//}//try-catch
		logicLayer = new LogicLayerImpl(dbConn);


		// Get the parameters
		//
		// No parameters here


		// Setup the data-model
		//
		Map<String,Object> root = new HashMap<String,Object>();

		// Build the data-model
		//
		comments = new LinkedList<List<Object>>();

		try {
			rvcomment = logicLayer.findAllComments();
		} 
		catch( Exception e ) {
			//            ClubsError.error( cfg, toClient, e );
			return;
		}

		root.put( "comments", comments );

		for( int i = 0; i < rvcomment.size(); i++ ) {
			myComment = (Comment) rvcomment.get( i );
			comment = new LinkedList<Object>();
			comment.add( new Long( myComment.getId() ) );
			comment.add( myComment.getCustomer().getUserName() );
			comment.add( myComment.getText() );
			comment.add( myComment.getRental().getVehicle().getId());
			comment.add(myComment.getDate());
			comment.add(myComment.getRental().getReservation().getId());
			comments.add( comment );
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


} //UserComments