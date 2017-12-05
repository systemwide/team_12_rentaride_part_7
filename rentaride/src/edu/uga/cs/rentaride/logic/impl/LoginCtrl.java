package edu.uga.cs.rentaride.logic.impl;

import java.util.List;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.Administrator;
import edu.uga.cs.rentaride.entity.Customer;
import edu.uga.cs.rentaride.object.ObjectLayer;
import edu.uga.cs.rentaride.session.Session;
import edu.uga.cs.rentaride.session.SessionManager;

public class LoginCtrl {
	private ObjectLayer objectLayer = null;
    
    public LoginCtrl(ObjectLayer objectLayer) {
        this.objectLayer = objectLayer;
    }
    
    public String login(Session session, String userName, String password) throws RARException {
    	String ssid = null;
       	
        Administrator modelAdministrator = objectLayer.createAdministrator();
        modelAdministrator.setUserName(userName);
        modelAdministrator.setPassword(password);
        List<Administrator> adminsList = objectLayer.findAdministrator(modelAdministrator);
        
        Customer modelCustomer = objectLayer.createCustomer();
        modelCustomer.setUserName(userName);
        modelCustomer.setPassword(password); 
        List<Customer> customerList = objectLayer.findCustomer(modelCustomer);
        
        if (!adminsList.isEmpty()) {
        	Administrator admin = adminsList.get(0);
            session.setUser(admin);
            ssid = SessionManager.storeSession(session);
        } else if (!customerList.isEmpty()) {
        	Customer customer = customerList.get(0);
            session.setUser(customer);
            ssid = SessionManager.storeSession(session);
        } else 
            throw new RARException( "You have entered an invalid User Name or Password" );
        
        return ssid;
    }
    
    public void logout(Session session) throws RARException {
    	
    	return;
    }
}
