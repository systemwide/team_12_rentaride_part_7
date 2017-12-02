package edu.uga.cs.rentaride.presentation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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


@WebServlet("/CreateVehicle")

public class CreateVehicle extends HttpServlet{

	/**
	 * Because eclipse told me to...
	 */
	private static final long serialVersionUID = 1L;

	

	static String templateDirectory = "WEB-INF/templates";
	static String templateName = "CreateVehicleSuccess";
	
	
	private Configuration cfg;
	
	public void init()
	{
		cfg = new Configuration();
		cfg.setServletContextForTemplateLoading(getServletContext(), templateDirectory);
	}
	
	public void doGet( HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
	{
		BufferedWriter toClient;

		// vehicle attributes
		long 					id = 0;
		String 					make;
		int 						year;
		String 					model;
		int 						mileage;
		String 					registrationTag;
		Date 					lastServiced = null;
		VehicleStatus 			status = null;
		VehicleCondition 		condition = null;
		RentalLocation 			rentalLocation = null;
		VehicleType 				vehicleType = null;
		
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
        
        // data from form on admin_vehicle.html
        
        make = req.getParameter("make");
        	year = Integer.getInteger(req.getParameter("year"));
        	model = req.getParameter("model");
        	mileage = Integer.getInteger(req.getParameter("mileage"));
        	registrationTag = req.getParameter("registrationTag");

        	// get a date from the form---yay  	
        	String lastServicedAsString = req.getParameter("lastServiced");
        	DateFormat format = new SimpleDateFormat("mm-dd-yyyy");
        	try {
				lastServiced = format.parse(lastServicedAsString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	String stat = req.getParameter("status");
        	if(stat == "INLOCATION")
        	{
        		status = VehicleStatus.INLOCATION;
        	}
        	if(stat == "INRENTAL")
        	{
        		status = VehicleStatus.INRENTAL;
        	}
       
        	String cond = req.getParameter("condition");
        	if(cond == "GOOD")
        	{
        		condition = VehicleCondition.GOOD;
        	}
        	if(cond == "NEEDSMAINTENANCE")
        	{
        		condition = VehicleCondition.NEEDSMAINTENANCE;
        	}
        	
        	RentalLocation modelLoc = new RentalLocationImpl();
        	try {
				modelLoc.setName(req.getParameter("rentalLocation"));
			} catch (RARException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	try {
				rentalLocation = RentalLocationManager.restoreRentalLocation(modelLoc).get(0);
			} catch (RARException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	VehicleType modelType = new VehicleTypeImpl();
        	try {
				modelType.setName(req.getParameter("type"));
			} catch (RARException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	try {
				vehicleType = VehicleTypeManager.restoreVehicleType(modelType).get(0);
			} catch (RARException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	// create the vehicle
        	try {
				id = logicLayer.createVehicle(id, make, model, year, mileage, registrationTag, lastServiced, 
						status, condition, rentalLocation, vehicleType);
			} catch (RARException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	}// end doPost
	
	
}// end class
