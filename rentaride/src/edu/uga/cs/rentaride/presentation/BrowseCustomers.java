package edu.uga.cs.rentaride.presentation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.Customer;
import edu.uga.cs.rentaride.entity.RentalLocation;
import edu.uga.cs.rentaride.entity.VehicleCondition;
import edu.uga.cs.rentaride.entity.VehicleStatus;
import edu.uga.cs.rentaride.entity.VehicleType;
import edu.uga.cs.rentaride.entity.impl.RentalLocationImpl;
import edu.uga.cs.rentaride.entity.impl.VehicleTypeImpl;
import edu.uga.cs.rentaride.logic.LogicLayer;
import edu.uga.cs.rentaride.object.ObjectLayer;
import edu.uga.cs.rentaride.object.impl.ObjectLayerImpl;
import edu.uga.cs.rentaride.persistence.impl.RentalLocationManager;
import edu.uga.cs.rentaride.persistence.impl.VehicleTypeManager;
import edu.uga.cs.rentaride.session.Session;
import edu.uga.cs.rentaride.session.SessionManager;

@WebServlet("/BrowseCustomers")
public class BrowseCustomers extends HttpServlet {

	/**
	 *  Because Eclipse told me to do it.
	 */
	private static final long serialVersionUID = 1L;

	static  String            templateDirectory = "WEB-INF/templates";
	static  String            templateName = "BrowseCustomers.ftl";

	private Configuration     cfg;
	
	public void init()
	{
		cfg = new Configuration();
		cfg.setServletContextForTemplateLoading(getServletContext(), templateDirectory);
	}
	
	
	public void doGet( HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
	{
		
		BufferedWriter toClient = null;
		
		List<Customer> clist = null;
		List<List<Object>> customers = null;
		List<Object> cust = null;
		Customer c = null;
		
		Session					session;
		LogicLayer				logicLayer;
		Connection 				dbconn;
		Template					resTemplate;
		String 					ssid;
		
		
		resTemplate = cfg.getTemplate(templateName);
		
		toClient = new BufferedWriter(
                new OutputStreamWriter( res.getOutputStream(), resTemplate.getEncoding() )
                );

        res.setContentType("text/html; charset=" + resTemplate.getEncoding());
        
        HttpSession httpSession = req.getSession();
		
        ssid = (String)httpSession.getAttribute("ssid");
        session = SessionManager.getSessionById(ssid);
        logicLayer = session.getLogicLayer();
        if(ssid == null || session == null || logicLayer == null)
        {
        		RARError.error(cfg, toClient, "Session Expired - Try logging in again");
        }
        
        Map<String, Object> root = new HashMap<String, Object>();
        
        clist = logicLayer.browseCustomers();
        
        customers = new LinkedList<List<Object>>();
        
        root.put("customers", customers);
        
        for(int i = 0;i < clist.size();i++)
        {
        		c = (Customer) clist.get(i);
        		cust = new LinkedList<Object>();
        		
        		cust.add( c.getFirstName());
        		cust.add(c.getLastName());
        		cust.add(c.getUserName());
        		//cust.add(c.getPassword());
        		cust.add(c.getEmail());
        		cust.add(c.getAddress());
        		cust.add(c.getCreatedDate());
        		cust.add(c.getMemberUntil());
        		cust.add(c.getLicenseState());
        		cust.add(c.getLicenseNumber());
        		//cust.add(c.getCreditCardNumber());
        		cust.add(c.getCreditCardExpiration());
        		cust.add(c.getUserStatus());
        		//cust.add(c.getReservations());
        		
        		customers.add(cust);
        }// end for
        
        try {
            resTemplate.process( root, toClient );
            toClient.flush();
        } 
        catch (TemplateException e) {
            throw new ServletException( "Error while processing FreeMarker template", e);
        }

        toClient.close();
        
	}// end doGet

}// end class
