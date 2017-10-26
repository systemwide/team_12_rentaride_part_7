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
import edu.uga.cs.rentaride.object.ObjectLayer;
import edu.uga.cs.rentaride.entity.*;

public class ReservationManager {
	
	private ObjectLayer objectLayer = null;
    private Connection   conn = null;
    
    public ReservationManager( Connection conn, ObjectLayer objectLayer )
    {
        this.conn = conn;
        this.objectLayer = objectLayer;
    }
	
    public List<Reservation> restoreReservation( Reservation modelReservation ) throws RARException
    {
    	
    	
    	
    	
    	
    }
    
    
    public void storeReservation( Reservation reservation ) throws RARException
    {
    	String               insertReservationSql = "insert into Reservations ( pickupTime, length, cancelled, customer, typeId, locationId, vehicleType, reservationID ) values ( ?, ?, ?, ?, ?, ? )";
        String               updateReservationSql = "update Reservation set pickupTime = ?, length = ?, cancelled = ?, customer = ?, typeId = ?, locationId = ?, vehicleType = ?, reservationID = ? where id = ?";java.sql.PreparedStatement    stmt = null;
        int                  inscnt;
        long                 reservationId;

        
        if( reservation.getId() == -1 )
            throw new RARException( "ReservationManager.save: Attempting to save a Reservation  without an id" );
                 
        try {

            if( !reservation.isPersistent() )
                stmt = (PreparedStatement) conn.prepareStatement(insertReservationSql);
            else
           	 stmt = (PreparedStatement) conn.prepareStatement( updateReservationSql );

            if( reservation.getPickupTime() != null ) // name is unique unique and non null
                stmt.setString( 1, reservation.getPickupTime().toString() );
            else 
                throw new RARException( "ReservationManager.save: can't save a Reservation: pickup time undefined" );

            if( reservation.getLength() != 0 )
                stmt.setInt( 2, reservation.getLength());
            else
           	 throw new RARException( "ReservationManager.save: can't save a Reservation: length undefined" );

            if( reservation.getCustomer()!= null ) {
           	 stmt.setString(3, reservation.getCustomer().toString());
            }
            else
           	 throw new RARException( "ReservationManager.save: can't save a Reservation: customer not set" );

            if( reservation.getVehicleType() != null ) {
              	 stmt.setString(4, reservation.getVehicleType().getId());
               }
               else
              	 throw new RARException( "ReservationManager.save: can't save a Reservation: vehicle type not set" );

            if( reservation.getRentalLocation() != null ) {
              	 stmt.setString(5, reservation.getRentalLocation().getId());
               }
               else
              	 throw new RARException( "ReservationManager.save: can't save a Reservation: rental location id not set" );

            if( reservation.getRental() != null ) {
              	 stmt.setString(6, reservation.getRental().getReservation());
               }
               else
              	 throw new RARException( "ReservationManager.save: can't save a Reservation: reservation not set" );

            
            if( reservation.isPersistent() )
                stmt.setLong( 4, reservation.getId() );

            inscnt = stmt.executeUpdate();

            if( !reservation.isPersistent() ) {
                if( inscnt >= 1 ) {
                    String sql = "select last_insert_id()";
                    if( stmt.execute( sql ) ) { // statement returned a result

                        // retrieve the result
                        ResultSet r = stmt.getResultSet();

                        // we will use only the first row!
                        //
                        while( r.next() ) {

                            // retrieve the last insert auto_increment value
                        	reservationId = r.getLong( 1 );
                            if( reservationId > 0 )
                            	reservation.setId( reservationId ); // set this reservation's db id (proxy object)
                        }
                    }
                }
                else
                    throw new RARException( "ReservationManager.save: failed to save a Reservation" );
            }
            else {
                if( inscnt < 1 )
                    throw new RARException( "ReservationManager.save: failed to save a Reservation" ); 
            }
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new RARException( "ReservationManager.save: failed to save a Reservation: " + e );
        }
   }
    
    
    public Customer restoreCustomerReservation( Reservation reservation ) throws RARException
    {
    	String selectReservationSql = "select cus.firstname, cus.lastname, cus.username, cus.password, cus.memberUntil"
    			+ ", cus.createdDate, cus.licNumber, cus,licState, cus.ccDate, cus.ccNum, r.pickupTime, r.length, r.cancelled, r.customer, r.typeId, r.locationId, r.vehicleType, r.reservationID " +
                " from Reservation r, Customer cus where ";
       Statement    stmt = null;
       StringBuffer query = new StringBuffer( 100 );
       StringBuffer condition = new StringBuffer( 100 );
       Customer customers;
   	
       condition.setLength( 0 );
       
       // form the query based on the given reservation object instance
       query.append( selectReservationSql );
       
       if( reservation != null ) {
           if( reservation.getId() >= 0 ) // id is unique, so it is sufficient to get a reservation
               query.append( " where id = " + reservation.getId() );
           else {
           	if( reservation.getCustomer()!= null ) // customer name is unique, so it is sufficient to get a rental
                condition.append( " where customer name = '" + reservation.getCustomer() + "'" );
           	
           	if( reservation.getPickupTime() != null )
           		if(condition.length() > 0 )
   					condition.append(" and ");
                condition.append( "pickup time = '" + reservation.getPickupTime() + "'" );

            if( reservation.getLength() != 0 )
            	if(condition.length() > 0 )
   		    		condition.append(" and ");
            	condition.append( "length = '" + reservation.getLength()+ "'" );  
 
            if( reservation.getVehicleType() != null )
               	if(condition.length() > 0 )
               		condition.append(" and ");
               	condition.append( "vehicle type = '" + reservation.getVehicleType() + "'" );
           
            if( reservation.getRentalLocation() != null )
               	if(condition.length() > 0 )
       	    		condition.append(" and ");
               	condition.append( "location id = '" + reservation.getRentalLocation().getId()+ "'" );  
     
            if( reservation.getVehicleType() != null )
               	if(condition.length() > 0 )
               		condition.append(" and ");
                condition.append( "vehicle type id = '" + reservation.getVehicleType().getId() + "'" );
            
                
           }
       }
    	
       try {

    	   stmt = conn.createStatement();

           // retrieve the persistent Reservation objects
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
               Customer nextCustomer = null;
               
               ResultSet rs = stmt.getResultSet();
               while( rs.next() ){ // statement returned a result
               
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
               
               nextCustomer = objectLayer.createCustomer();
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
       throw new RARException( "ReservationManager.restore: Could not restore persistent Reservation objects; Root cause: " + e );
   }

   throw new RARException( "ReservationManager.restore: Could not restore persistent Reservation objects" );
       }
    
    
    public RentalLocation restoreReservationRentalLocation( Reservation reservation ) throws RARException
    {
    	String selectReservationSql = "select rl.address, rl.capacity, rl.locationName, "
    			+ "r.pickupTime, r.length, r.cancelled, r.customer, r.typeId, r.locationId, r.vehicleType, r.reservationID " +
                " from Reservation r, rentalLocation rl where r.id = rl.id";
       Statement    stmt = null;
       StringBuffer query = new StringBuffer( 100 );
       StringBuffer condition = new StringBuffer( 100 );
       RentalLocation rentalL;
   	
condition.setLength( 0 );
       
       // form the query based on the given reservation object instance
       query.append( selectReservationSql );
       
       if( reservation != null ) {
           if( reservation.getId() >= 0 ) // id is unique, so it is sufficient to get a reservation
               query.append( " where id = " + reservation.getId() );
           else {
           	if( reservation.getCustomer()!= null ) // customer name is unique, so it is sufficient to get a rental
                condition.append( " where customer name = '" + reservation.getCustomer() + "'" );
           	
           	if( reservation.getPickupTime() != null )
           		if(condition.length() > 0 )
   					condition.append(" and ");
                condition.append( "pickup time = '" + reservation.getPickupTime() + "'" );

            if( reservation.getLength() != 0 )
            	if(condition.length() > 0 )
   		    		condition.append(" and ");
            	condition.append( "length = '" + reservation.getLength()+ "'" );  
 
            if( reservation.getVehicleType() != null )
               	if(condition.length() > 0 )
               		condition.append(" and ");
               	condition.append( "vehicle type = '" + reservation.getVehicleType() + "'" );
           
            if( reservation.getRentalLocation() != null )
               	if(condition.length() > 0 )
       	    		condition.append(" and ");
               	condition.append( "location id = '" + reservation.getRentalLocation() + "'" );  
                
           }
       }
    	
       try {

    	   stmt = conn.createStatement();

           // retrieve the persistent Reservation objects
           //
           if( stmt.execute( query.toString() ) ) { // statement returned a result
               
               long   id;
               String address;
               int capacity;
               String name;
               
               RentalLocation nextRentalL = null;
               
               ResultSet rs = stmt.getResultSet();
               while( rs.next() ){ // statement returned a result
               
        	   id = rs.getLong( 1 );
               address = rs.getString( 2 );
               capacity = rs.getInt(3);
               name = rs.getString( 4 );
               
               nextRentalL = objectLayer.createRentalLocation();
               // and now set its retrieved attributes
               nextRentalL.setId( id );
               nextRentalL.setAddress(address);
               nextRentalL.setCapacity(capacity);
               nextRentalL.setName(name);
               
               // set this to null for the "lazy" association traversal
               //nextCustomer.setPersonFounder( null );
               
               rentalL.add( nextRentalL );
           }
           
           return rentalL;
         }
       }
       catch( Exception e ) {      // just in case...
    	   throw new RARException( "ReservationManager.restore: Could not restore Reservation Customer objects; Root cause: " + e );
       }

       throw new RARException( "ReservationManager.restore: Could not restore persistent Reservation objects" );	
    } 
    
    public VehicleType restoreReservationVehicleType( Reservation reservation ) throws RARException
    {
    	String selectReservationSql = "select vt.name, r.typeId, r.locationId, r.vehicleType, r.reservationID " +
                " from Reservation r, vehicleType vt where r.id = vt.id";
       Statement    stmt = null;
       StringBuffer query = new StringBuffer( 100 );
       StringBuffer condition = new StringBuffer( 100 );
       VehicleType vehicleType;
   	
condition.setLength( 0 );
       
       // form the query based on the given reservation object instance
       query.append( selectReservationSql );
       
       if( reservation != null ) {
           if( reservation.getId() >= 0 ) // id is unique, so it is sufficient to get a reservation
               query.append( " where id = " + reservation.getId() );
           else {
           	if( reservation.getCustomer()!= null ) // customer name is unique, so it is sufficient to get a rental
                condition.append( " where customer name = '" + reservation.getCustomer() + "'" );
           	
           	if( reservation.getPickupTime() != null )
           		if(condition.length() > 0 )
   					condition.append(" and ");
                condition.append( "pickup time = '" + reservation.getPickupTime() + "'" );

            if( reservation.getLength() != 0 )
            	if(condition.length() > 0 )
   		    		condition.append(" and ");
            	condition.append( "length = '" + reservation.getLength()+ "'" );  
 
            if( reservation.getVehicleType() != null )
               	if(condition.length() > 0 )
               		condition.append(" and ");
               	condition.append( "vehicle type = '" + reservation.getVehicleType() + "'" );
           
            if( reservation.getRentalLocation() != null )
               	if(condition.length() > 0 )
       	    		condition.append(" and ");
               	condition.append( "location id = '" + reservation.getRentalLocation() + "'" );  
                
           }
       }
    	
       try {

    	   stmt = conn.createStatement();

           // retrieve the persistent Reservation objects
           //
           if( stmt.execute( query.toString() ) ) { // statement returned a result
               
               long   id;
               String name;
               
               VehicleType nextvType = null;
               
               ResultSet rs = stmt.getResultSet();
               while( rs.next() ){ // statement returned a result
               
        	   id = rs.getLong( 1 );
               name = rs.getString( 2 );
               
               nextvType = objectLayer.createVehicleType();
               // and now set its retrieved attributes
               nextvType.setId( id );
               nextvType.setName(name);
               
               // set this to null for the "lazy" association traversal
               //nextCustomer.setPersonFounder( null );
               
               vehicleType.add( nextvType );
           }
           
           return vehicleType;
         }
       }
       catch( Exception e ) {      // just in case...
    	   throw new RARException( "ReservationManager.restore: Could not restore Reservation Customer objects; Root cause: " + e );
       }

       throw new RARException( "ReservationManager.restore: Could not restore persistent Reservation objects" );	
    }
    
    
    public Rental restoreRentalReservation( Reservation reservation ) throws RARException
    {
    	String selectReservationSql = "select vt.name, r.typeId, r.locationId, r.vehicleType, r.reservationID " +
                " from Reservation r, vehicleType vt where r.id = vt.id";
       Statement    stmt = null;
       StringBuffer query = new StringBuffer( 100 );
       StringBuffer condition = new StringBuffer( 100 );
       Rental rental;
   	
condition.setLength( 0 );
       
       // form the query based on the given reservation object instance
       query.append( selectReservationSql );
       
       if( reservation != null ) {
           if( reservation.getId() >= 0 ) // id is unique, so it is sufficient to get a reservation
               query.append( " where id = " + reservation.getId() );
           else {
           	if( reservation.getCustomer()!= null ) // customer name is unique, so it is sufficient to get a rental
                condition.append( " where customer name = '" + reservation.getCustomer() + "'" );
           	
           	if( reservation.getPickupTime() != null )
           		if(condition.length() > 0 )
   					condition.append(" and ");
                condition.append( "pickup time = '" + reservation.getPickupTime() + "'" );

            if( reservation.getLength() != 0 )
            	if(condition.length() > 0 )
   		    		condition.append(" and ");
            	condition.append( "length = '" + reservation.getLength()+ "'" );  
 
            if( reservation.getVehicleType() != null )
               	if(condition.length() > 0 )
               		condition.append(" and ");
               	condition.append( "vehicle type = '" + reservation.getVehicleType() + "'" );
           
            if( reservation.getRentalLocation() != null )
               	if(condition.length() > 0 )
       	    		condition.append(" and ");
               	condition.append( "location id = '" + reservation.getRentalLocation() + "'" );  
                
           }
       }
    	
       try {

               stmt = conn.createStatement();

               // retrieve the persistent Rental objects
               //
               if( stmt.execute( query.toString() ) ) { // statement returned a result
                   
                   long   id;
                   String customer;
                   Date pickupT;
                   Date returnT;
                   String isLate;
                   int charges;
                   String vehicleId;
                   String reservationId;
                   Rental nextRental = null;
                   
                   ResultSet rs = stmt.getResultSet();
                   
                   // retrieve the retrieved Rentals
                   while( rs.next() ) {
                       
                       id = rs.getLong( 1 );
                       customer = rs.getString( 2 );
                       pickupT = rs.getDate( 3 );
                       returnT = rs.getDate( 4 );
                       isLate = rs.getString( 5 );
                       charges = rs.getInt( 6 );
                       vehicleId = rs.getString( 7 );
                       reservationId = rs.getString( 8 );
                       
                       nextRental = objectLayer.createRental(); // create a proxy Rental object
                       // and now set its retrieved attributes
                       nextRental.setId( id );
                       nextRental.setCharges(charges);
                       nextRental.setPickupTime(pickupT);
                       nextRental.setReturnTime(returnT);
                       // set this to null for the "lazy" association traversal
                       //nextRental.setPersonFounder( null );
               
               rental.add( nextRental );
           }
           
           return rental;
         }
       }
       catch( Exception e ) {      // just in case...
    	   throw new RARException( "ReservationManager.restore: Could not restore Reservation Customer objects; Root cause: " + e );
       }

       throw new RARException( "ReservationManager.restore: Could not restore persistent Reservation objects" );	
    }
    
    
    public void deleteReservation( Reservation reservation ) throws RARException
    {
		String               deleteReservationSql = "delete from Reservation where id = ?";              
	    PreparedStatement    stmt = null;
	    int                  inscnt;
	             
	    if( !reservation.isPersistent() ) // is the Reservation object persistent?  If not, nothing to actually delete
	    	return;
	        
	    try {
	    	stmt = (PreparedStatement) conn.prepareStatement( deleteReservationSql );         
	    	stmt.setLong( 1, reservation.getId() );
	    	inscnt = stmt.executeUpdate();          
	    	if( inscnt == 1 ) {
	    		return;
	        }
	    	else	
	    		throw new RARException( "ReservationManager.delete: failed to delete a Reservation" );
	        }	
	        catch( SQLException e ) 
	    	{
	        	e.printStackTrace();
	            throw new RARException( "ReservationManager.delete: failed to delete a Reservation: " + e );       
	    	}
	 }	
}