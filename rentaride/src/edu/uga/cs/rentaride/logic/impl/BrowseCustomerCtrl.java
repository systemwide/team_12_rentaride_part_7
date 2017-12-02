package edu.uga.cs.rentaride.logic.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.Customer;
import edu.uga.cs.rentaride.entity.User;
import edu.uga.cs.rentaride.object.ObjectLayer;

public class BrowseCustomerCtrl {
	private ObjectLayer objectLayer = null;
	
	public BrowseCustomerCtrl(ObjectLayer obj)
	{
		this.objectLayer = obj;
		
	}
	
	public List<Customer> ViewCustomerInfo(String username)
            throws RARException
    {
        List<Customer> 	    customers  = null;
        Iterator<Customer> 	custIter = null;
        Customer     	    customer = null;

        customers = new LinkedList<Customer>();
        
        // retrieve all Customer objects
        //
        custIter = (Iterator<Customer>) objectLayer.findCustomer( null );
        while( custIter.hasNext() ) {
            customer = custIter.next();
            System.out.println( customer);
            customers.add( customer );
        }

        return customers;
    }
	
}// end class
