package edu.uga.cs.rentaride.persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.*;
import edu.uga.cs.rentaride.object.ObjectLayer;

public class CustomerManager{

private ObjectLayer objectLayer = null;
    private Connection  conn = null;
    
    public CustomerManager( Connection conn, ObjectLayer objectLayer )
    {
        this.conn = conn;
        this.objectLayer = objectLayer;
    }
    
    public void store( Customer customer ) throws RARException
    {
    	String               insertCustomerSql = "insert into customer ( firstName, lastname, username, password, email, address, createDate, memberUntil, licState, licNumber, ccNumb, ccDate, Customerid ) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
        String               updateCustomerSql = "update club set firstName = ?, lastname = ?, username = ?, password = ?, email = ?, address = ?, createDate = ?, memberUntil = ?, licState = ?, licNumber = ?, ccNub = ?, ccDate = ?, Customerid = ? where id = ?";
        PreparedStatement    stmt = null;
        int                  inscnt;
        long                 customerId;      
            
        /*
        if( club.getFounderId() == -1 )
            throw new ClubsException( "ClubManager.save: Attempting to save a Club without a founder" );
            */
                 
        try {

            if( !customer.isPersistent() )
                stmt = (PreparedStatement) conn.prepareStatement( insertCustomerSql );
            else
                stmt = (PreparedStatement) conn.prepareStatement( updateCustomerSql );

            if( customer.getFirstName() != null ) // name is unique unique and non null
                stmt.setString( 1, customer.getFirstName() );
            else 
                throw new RARException( "CustomerManager.save: can't save a Customer: name undefined" );
            
            if( customer.getLastName() != null ) // name is unique unique and non null
                stmt.setString( 2, customer.getLastName() );
            else 
                throw new RARException( "CustomerManager.save: can't save a Customer: name undefined" );

            if( customer.getUserName() != null )
                stmt.setString( 3, customer.getUserName());
            else
            	throw new RARException( "CustomerManager.save: can't save a Customer: username undefined" );
        
            if( customer.getPassword() != null )
                stmt.setString( 4, customer.getPassword() );
            else
                throw new RARException( "CustomerManager.save: can't save a Customer: password undefined" );
            
            if( customer.getEmail() != null )
                stmt.setString( 5, customer.getEmail() );
            else
            	throw new RARException( "CustomerManager.save: can't save a Customer: email undefined" );
            
            if( customer.getAddress() != null )
                stmt.setString( 6, customer.getAddress() );
            else
                stmt.setNull( 6, java.sql.Types.VARCHAR );

            if( customer.getCreatedDate() != null ) {
                java.util.Date jDate = customer.getCreatedDate();
                java.sql.Date sDate = new java.sql.Date( jDate.getTime() );
                stmt.setDate( 7,  sDate );
            }
            else
                stmt.setNull(7, java.sql.Types.DATE);
            
            if( customer.getMemberUntil() != null ){
            java.util.Date jDate = customer.getMemberUntil();
            java.sql.Date sDate = new java.sql.Date( jDate.getTime() );
            stmt.setDate( 8,  sDate );
            }
            else
            	stmt.setNull(8, java.sql.Types.DATE);
            
            if( customer.getLicenseState() != null )
                stmt.setString( 9, customer.getLicenseState() );
            else
                stmt.setNull( 9, java.sql.Types.VARCHAR );
           
            if( customer.getLicenseNumber() != null )
                stmt.setString( 10, customer.getLicenseNumber() );
            else
                stmt.setNull( 10, java.sql.Types.VARCHAR );
            
            if( customer.getCreditCardNumber() != null )
                stmt.setString( 11, customer.getCreditCardNumber() );
            else
                stmt.setNull( 11, java.sql.Types.VARCHAR );
            
            if( customer.getCreditCardExpiration() != null ){
                java.util.Date jDate = customer.getCreditCardExpiration();
                java.sql.Date sDate = new java.sql.Date( jDate.getTime() );
                stmt.setDate( 12,  sDate );
            }
            else
            	stmt.setNull(12, java.sql.Types.DATE);
            
            /*
            if( customer.getPersonFounder() != null && customer.getPersonFounder().isPersistent() )
                stmt.setLong( 14, customer.getPersonFounder().getId() );
            else 
                throw new RARException( "ClubManager.save: can't save a Club: founder is not set or not persistent" );
            */
            
            if( customer.isPersistent() )
                stmt.setLong( 13, customer.getId() );

            inscnt = stmt.executeUpdate();

            if( !customer.isPersistent() ) {
                if( inscnt >= 1 ) {
                    String sql = "select last_insert_id()";
                    if( stmt.execute( sql ) ) { // statement returned a result

                        // retrieve the result
                        ResultSet r = stmt.getResultSet();

                        // we will use only the first row!
                        //
                        while( r.next() ) {

                            // retrieve the last insert auto_increment value
                            customerId = r.getLong( 1 );
                            if( customerId > 0 )
                            	customer.setId( customerId ); // set this customer's db id (proxy object)
                        }
                    }
                }
                else
                    throw new RARException( "CustomerManager.save: failed to save a Customer" );
            }
            else {
                if( inscnt < 1 )
                    throw new RARException( "CustomerManager.save: failed to save a Customer" ); 
            }
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new RARException( "CustomerManager.save: failed to save a Customer: " + e );
        }
    }

	public List<Customer> restoreCustomer( Customer modelCustomer ) throws RARException
	{
		String       selectCustomerSql = "select id, firstName, lastname, username, password, email, address, createDate, memberUntil, licState, licNumber, ccNum, ccDate, from club";
        Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );
        List<Customer>   customers = new ArrayList<Customer>();

        condition.setLength( 0 );
        
        // form the query based on the given Customer object instance
        query.append( selectCustomerSql );
        
        if( modelCustomer != null ) {
            if( modelCustomer.getId() >= 0 ) // id is unique, so it is sufficient to get a customer
                query.append( " where id = " + modelCustomer.getId() );
            else if( modelCustomer.getUserName() != null ) // userName is unique, so it is sufficient to get a customer
                query.append( " where name = '" + modelCustomer.getUserName() + "'" );
            else {

            	if( modelCustomer.getPassword() != null )
    				condition.append( " password = '" + modelCustomer.getPassword() + "'" );

    			if( modelCustomer.getEmail() != null){
    				if(condition.length() > 0 )
    					condition.append(" and ");
    				condition.append( "email = '" + modelCustomer.getEmail() + "'" );
    			}
    			
    			if( modelCustomer.getFirstName() != null){
    				if(condition.length() > 0 )
    					condition.append(" and ");
    				condition.append( "firstname = '" + modelCustomer.getFirstName() + "'" );
    			
    			}

    			if( modelCustomer.getLastName() != null){
    				if(condition.length() == 0 )
    					condition.append(" and ");
    				condition.append( "lastname = '" + modelCustomer.getLastName() + "'" );
    			}

    			if( modelCustomer.getAddress() != null){
    				if(condition.length() == 0 )
    					condition.append(" and ");
    				condition.append( "address = '" + modelCustomer.getAddress() + "'" );
    			}
    			
    			if( modelCustomer.getLicenseState() != null){
    				if(condition.length() > 0 )
    					condition.append(" and ");
    				condition.append( "license state = '" + modelCustomer.getLicenseState() + "'" );
    			}
    			
    			if( modelCustomer.getLicenseNumber() != null){
    				if(condition.length() > 0 )
    					condition.append(" and ");
    				condition.append( "license number = '" + modelCustomer.getLicenseNumber() + "'" );
    			}

    			if( modelCustomer.getCreditCardNumber() != null){
    				if(condition.length() > 0 )
    					condition.append(" and ");
    				condition.append( "credit card number = '" + modelCustomer.getCreditCardNumber() + "'" );
    			}

    			if( modelCustomer.getCreditCardExpiration() != null){
    				if(condition.length() > 0 )
    					condition.append(" and ");
    				condition.append( "credit card expiration = '" + modelCustomer.getCreditCardNumber() + "'" );
    			}
    			
    			if( modelCustomer.getCreatedDate() != null){
    				if(condition.length() > 0 )
    					condition.append(" and ");
    				condition.append( "created date = '" + modelCustomer.getCreatedDate() + "'" );
    			}	
    			
    			if( modelCustomer.getMemberUntil() != null){
    				if(condition.length() > 0 )
    					condition.append(" and ");
    				condition.append( "member until = '" + modelCustomer.getMemberUntil() + "'" );
    			}
				
            }
        }
        
        try {

            stmt = conn.createStatement();

            // retrieve the persistent Club objects
            //
            if( stmt.execute( query.toString() ) ) { // statement returned a result
                
                long   id;
                String firstname;
                String lastname;
                String username;
                String password;
                String email;
                String address;
                Date createdDate;
                String licState;
                String licNum;
                Date ccDate;
                String ccNum;
                Date memberUntil;
                Customer   nextCustomer = null;
                
                ResultSet rs = stmt.getResultSet();
                
                // retrieve the retrieved customers
                while( rs.next() ) {
                    
                    id = rs.getLong( 1 );
                    firstname = rs.getString( 2 );
                    lastname = rs.getString(3);
                    username = rs.getString( 4 );
                    password = rs.getString(5);
                    email = rs.getString( 6 );
                    address = rs.getString(7);
                    createdDate = rs.getDate( 8);
                    licState = rs.getString(9);
                    licNum = rs.getString(10);
                    ccDate = rs.getDate(11);
                    ccNum = rs.getString( 12 );
                    memberUntil = rs.getDate(13);
                    
                    nextCustomer = objectLayer.createCustomer(); // create a proxy customer object
                    // and now set its retrieved attributes
                    nextCustomer.setId( id );
                    nextCustomer.setUserName( username );
                    nextCustomer.setAddress( address );
                    nextCustomer.setCreateDate(createdDate);
                    nextCustomer.setFirstName(firstname);
                    nextCustomer.setLastName(lastname);
                    nextCustomer.setEmail(email);
                    nextCustomer.setPassword(password);
                    nextCustomer.setLicenseState(licState);
                    nextCustomer.setLicenseNumber(licNum);
                    nextCustomer.setCreditCardExpiration(ccDate);
                    nextCustomer.setCreditCardNumber(ccNum);
                    nextCustomer.setMemberUntil(memberUntil);
                    
                    // set this to null for the "lazy" association traversal
                    //nextCustomer.setPersonFounder( null );
                    
                    customers.add( nextCustomer );
                }
                
                return customers;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new RARException( "CustomerManager.restore: Could not restore persistent Customer objects; Root cause: " + e );
        }

        throw new RARException( "CustomerManager.restore: Could not restore persistent Customer objects" );
    }


    public void deleteCustomer( Customer customer ) throws RARException{
    	
    	//empty
    	
    }
    
    
    public List<Reservation> restoreCustomerReservation( Customer customer ) throws RARException
    {
    	String selectCustomerSql = "select r.id, r.name, r.address, r.established, r.id, " +
                "c.username, c.userpass, c.email, c.firstname, c.lastname, c.address, " +
                "c.creditcardnumber from Reservation r, Customer c where c.founderid = c.id";
    	Statement    stmt = null;
    	StringBuffer query = new StringBuffer( 100 );
    	StringBuffer condition = new StringBuffer( 100 );
    	List<Reservation> res = new ArrayList<Reservation>();

    	condition.setLength( 0 );

    	// form the query based on the given Customer object instance
    	query.append( selectCustomerSql );

    	if( customer != null ) {
    		if( customer.getId() >= 0 ) // id is unique, so it is sufficient to get a customer
    			query.append( " and c.id = " + customer.getId() );
    		else if( customer.getUserName() != null ) // userName is unique, so it is sufficient to get a customer
    			query.append( " and c.username = '" + customer.getUserName() + "'" );
    		else {
    			if( customer.getPassword() != null )
    				condition.append( " c.password = '" + customer.getPassword() + "'" );

    			if( customer.getEmail() != null && condition.length() == 0 )
    				condition.append( " c.email = '" + customer.getEmail() + "'" );
    			else
    				condition.append( " AND c.email = '" + customer.getEmail() + "'" );

    			if( customer.getFirstName() != null && condition.length() == 0 )
    				condition.append( " c.firstname = '" + customer.getFirstName() + "'" );
    			else
    				condition.append( " AND c.firstname = '" + customer.getFirstName() + "'" );

    			if( customer.getLastName() != null && condition.length() == 0 )
    				condition.append( " c.lastname = '" + customer.getLastName() + "'" );
    			else
    				condition.append( " AND c.lastname = '" + customer.getLastName() + "'" );

    			if( customer.getAddress() != null && condition.length() == 0 )
    				condition.append( " c.address = '" + customer.getAddress() + "'" );
    			else
    				condition.append( " AND c.address = '" + customer.getAddress() + "'" );
    			
    			if( customer.getUserStatus() != null )
                    condition.append( " c.userstatus = '" + customer.getUserStatus() + "'" );
                else
                    condition.append( " AND c.userstatus = '" + customer.getUserStatus() + "'" );
    			
    			if( customer.getCreatedDate() != null && condition.length() == 0 )
    				condition.append( " c.createdDate = '" + customer.getCreatedDate() + "'" );
    			else
    				condition.append( " AND c.createdDate = '" + customer.getCreatedDate() + "'" );
    			
    			//DO WE EVEN NEED THESE?
    			
    			if( customer.getLicenseState() != null && condition.length() == 0 )
    				condition.append( " c.licensestate = '" + customer.getLicenseState() + "'" );
    			else
    				condition.append( " AND c.licensestate = '" + customer.getLicenseState() + "'" );
    			
    			if( customer.getLicenseNumber() != null && condition.length() == 0 )
    				condition.append( " c.licensenumber = '" + customer.getLicenseNumber() + "'" );
    			else
    				condition.append( " AND c.licensenumber = '" + customer.getLicenseNumber() + "'" );

    			if( customer.getCreditCardNumber() != null && condition.length() == 0 )
    				condition.append( " c.creditcardnumber = '" + customer.getCreditCardNumber() + "'" );
    			else
    				condition.append( " AND c.creditcardnumber = '" + customer.getCreditCardNumber() + "'" );

    			if( customer.getCreditCardExpiration() != null && condition.length() == 0 )
    				condition.append( " c.creditcardexpiration = '" + customer.getCreditCardExpiration() + "'" );
    			else
    				condition.append( " AND c.creditcardexpiration = '" + customer.getCreditCardNumber() + "'" );
    			
    			if( customer.getMemberUntil() != null && condition.length() == 0 )
    				condition.append( " c.memberuntil = '" + customer.getMemberUntil() + "'" );
    			else
    				condition.append( " AND c.memberuntil = '" + customer.getMemberUntil() + "'" );
    			
    			//END OF DO WE NEED THESE
    			
    			if( condition.length() > 0 ) {
    				query.append( condition );
    			}
    		}
    	}

    	try {

    		stmt = conn.createStatement();

    		// retrieve the persistent Club objects
    		//
    		if( stmt.execute( query.toString() ) ) { // statement returned a result
         
    			long   id;
                Date pickup;
                int length;
                Reservation nextReservation = null;
                
                ResultSet rs = stmt.getResultSet();
                
                // retrieve the retrieved customers
                while( rs.next() ) {
                    
                    id = rs.getLong( 1 );
                    pickup = rs.getDate( 2 );
                    length = rs.getInt(3);
                    
                    nextReservation = objectLayer.createReservation(); // create a proxy customer object
                    // and now set its retrieved attributes
                    nextReservation.setId( id );
                    nextReservation.setPickupTime(pickup);
                    nextReservation.setLength(length);
    				// set this to null for the "lazy" association traversal
    				//nextReservation.setPersonFounder( null );
   
                    res.add( nextReservation );
                }
    			return res;
    		
    		}
    		else
    			return res;
    	}
    	catch( Exception e ) {      // just in case...
    		throw new RARException( "CustomerManager.restoreReservation: Could not restore persistent Customer objects; Root cause: " + e );
    	}
    }
    
    	 
}