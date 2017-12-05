package edu.uga.cs.rentaride.presentation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.uga.cs.rentaride.entity.Customer;
import edu.uga.cs.rentaride.logic.LogicLayer;
import edu.uga.cs.rentaride.session.Session;
import edu.uga.cs.rentaride.session.SessionManager;
import edu.uga.cs.rentaride.presentation.RARError;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@WebServlet("/Login")
public class Login extends HttpServlet {
    
	private static final long serialVersionUID = 1L;
    
    static  String  templateDir = "WEB-INF/templates";
    //static  
    String  resultTemplateName = "";

    private Configuration cfg; 

    public void init() {
        // Prepare the FreeMarker configuration;
        // - Load templates from the WEB-INF/templates directory of the Web app.
        cfg = new Configuration();
        cfg.setServletContextForTemplateLoading(getServletContext(), "WEB-INF/templates");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Template       resultTemplate = null;
        HttpSession    httpSession = null;
        BufferedWriter toClient = null;
        String         username = null;
        String         password = null;
        String         ssid = null;
        Session        session = null;
        LogicLayer     logicLayer = null;

        httpSession = req.getSession();
        ssid = (String) httpSession.getAttribute("ssid");
        if( ssid != null ) {
            System.out.println("Already have ssid: " + ssid);
            session = SessionManager.getSessionById(ssid);
            
        } else
            System.out.println("NULL VALUE FOR SSID !!");

        if( session == null ) {
            try {
                
            	session = SessionManager.createSession();
                System.out.println("Session's SSID: " + session.getId());
            }
            catch ( Exception e ) {
                RARError.error( cfg, toClient, e );
                return;
            }
        }
        
        logicLayer = session.getLogicLayer();

        // Get the parameters
        //
        username = req.getParameter( "username" );
        password = req.getParameter( "password" );

        System.out.println("Login Servlet has acquired the following:");
        System.out.println("username: " + username);
        System.out.println("password: " + password);
        
        if( username == null || password == null ) {
            RARError.error( cfg, toClient, "You have failed to enter username or password" );
            return;
        }

        try {          
            ssid = logicLayer.login( session, username, password );
            System.out.println( "Obtained ssid: " + ssid );
            httpSession.setAttribute( "ssid", ssid );
            System.out.println( "Connection: " + session.getConnection() );
        } 
        catch (Exception e) {
            RARError.error(cfg, toClient, e);
            return;
        }

     
        System.out.println("Select appropriate login page - Admin or Customer.");
        try {
        	
        	if (session.getUser().getClass().getName() == "edu.uga.cs.rentaride.entity.impl.AdministratorImpl") resultTemplateName = "admin_login.ftl";
        	
        	if (session.getUser().getClass().getName() == "edu.uga.cs.rentaride.entity.impl.CustomerImpl") resultTemplateName = "user_login.ftl";
        	
        	System.out.println("Loading template: " + resultTemplateName);
        	
            resultTemplate = cfg.getTemplate(resultTemplateName);
        } catch (IOException e) {
            throw new ServletException("Login.doPost: Can't load template in: " + templateDir + ": " + e.toString());
}
        // Setup the data-model
        //
        Map<String, String> root = new HashMap<String, String>();

        // Build the data-model
        //
        root.put( "username", username );

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
}

