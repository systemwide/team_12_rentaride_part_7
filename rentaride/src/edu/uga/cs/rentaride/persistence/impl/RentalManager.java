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


class RentalManager
{
    private ObjectLayer objectLayer = null;
    private Connection   conn = null;
    
    public RentalManager( Connection conn, ObjectLayer objectLayer )
    {
        this.conn = conn;
        this.objectLayer = objectLayer;
    }
    
    public void store( Rental rental ) 
            throws RARException
    {
        String               insertRentalSql = "insert into rental ( customer, pickupDate, return, late, charges, vehicleID, reservationID ) values ( ?, ?, ?, ?, ?, ?, ? )";
        String               updateRentalSql = "update rental set customer = ?, pickupDate = ?, return = ?, late = ?, charges = ?, vehicleID = ?, reservationID = ? where id = ?";
        PreparedStatement    stmt = null;
        int                  inscnt;
        long                 rentalId;
                 
        try {

            if( !rental.isPersistent() )
                stmt = (PreparedStatement) conn.prepareStatement( insertRentalSql );
            else
                stmt = (PreparedStatement) conn.prepareStatement( updateRentalSql );

            if( rental.getCustomer() != null ) // name is unique unique and non null
                stmt.setString( 1, rental.getCustomer() );
            else 
                throw new RARException( "RentalManager.save: can't save a Rental: Customer undefined" );

            if( rental.getPickupTime() != null )
                stmt.setDate( 2, rental.getPickupTime()  );
            else
            	stmt.setNull(2, java.sql.Types.DATE);

            if( rental.getReturnTime() != null )
                stmt.setDate( 3, rental.getReturnTime()  );
            else
            	stmt.setNull(3, java.sql.Types.DATE);
            
            if( rental.getLate() != false )
                stmt.setString( 4, rental.getLate()  );
            else
            	throw new RARException( "RentalManager.save: can't save a Rental: late undefined" );
            
            if( rental.getCharges() != 0 )
                stmt.setString( 5, rental.getCharges()  );
            else
            	throw new RARException( "RentalManager.save: can't save a Rental: charges not set or not persistent" );
           
            if( rental.getVehicle()!= null )
                stmt.setString( 6, rental.getVehicle().getId()  );
            else
            	throw new RARException( "RentalManager.save: can't save a Rental: vehicleId undefined" );
            
            if( rental.getReservation()!= null )
                stmt.setString( 7, rental.getReservation().getId()  );
            else
            	throw new RARException( "RentalManager.save: can't save a Rental: reservationId undefined" );

            if( rental.isPersistent() )
                stmt.setLong( 8, rental.getId() );

            inscnt = stmt.executeUpdate();

            if( !rental.isPersistent() ) {
                if( inscnt >= 1 ) {
                    String sql = "select last_insert_id()";
                    if( stmt.execute( sql ) ) { // statement returned a result

                        // retrieve the result
                        ResultSet r = stmt.getResultSet();

                        // we will use only the first row!
                        //
                        while( r.next() ) {

                            // retrieve the last insert auto_increment value
                        	rentalId = r.getLong( 1 );
                            if( rentalId > 0 )
                                rental.setId( rentalId ); // set this person's db id (proxy object)
                        }
                    }
                }
                else
                    throw new RARException( "RentalManager.save: failed to save a Rental" );
            }
            else {
                if( inscnt < 1 )
                    throw new RARException( "RentalManager.save: failed to save a Rental" ); 
            }
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new RARException( "RentalManager.save: failed to save a Rental: " + e );
        }
    }

    public List<Rental> restore( Rental modelRental ) 
            throws RARException
    {
        String       selectClubSql = "select id, customer, pickupDate, return, late, charges, vehicleID, reservationID from Rental";
        Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );
        List<Rental>   Rental = new ArrayList<Rental>();

        condition.setLength( 0 );
        
        // form the query based on the given Rental object instance
        query.append( selectClubSql );
        
        if( modelRental != null ) {
            if( modelRental.getId() >= 0 ) // id is unique, so it is sufficient to get a rental
                query.append( " where id = " + modelRental.getId() );
            else {
            	if( modelRental.getCustomer() != null ) // customer name is unique, so it is sufficient to get a rental
                    query.append( " where customer = '" + modelRental.getCustomer() + "'" );
            	
            	if( modelRental.getPickupTime() != null )
            		if(condition.length() > 0 )
    					condition.append(" and ");
                    condition.append( "pickup time = '" + modelRental.getPickupTime() + "'" );

                if( modelRental.getReturnTime() != null )
                	if(condition.length() > 0 )
    					condition.append(" and ");
                    condition.append( "return time = '" + modelRental.getReturnTime()+ "'" );  
       
                if( modelRental.getLate() != false )
                  	if(condition.length() > 0 )
                  		condition.append(" and ");
                    condition.append( "late = '" + modelRental.getLate() + "'" );
                
                    if( modelRental.getCharges() != 0 )
                    if(condition.length() > 0 )
        				condition.append(" and ");
                    condition.append( "charges = '" + modelRental.getCharges() + "'" );
                    
                if( modelRental.getVehicle() != null )
                	if(condition.length() > 0 )
    					condition.append(" and ");
                    condition.append( "vehicleID = '" + modelRental.getVehicle().getId() + "'" );
                    
                if( modelRental.getReservation() != null )
                  	if(condition.length() > 0 )
        				condition.append(" and ");
                    condition.append( "reservationID = '" + modelRental.getReservation().getId() + "'" );

            }
        }
        
        try {

            stmt = conn.createStatement();

            // retrieve the persistent Rental objects
            //
            if( stmt.execute( query.toString() ) ) { // statement returned a result
                
                long   id;
                Date pickupT;
                Date returnT;
                String isLate;
                int charges;
                String vehicleId;
                String customer;
                String reservationId;
                Rental nextRental = null;
                
                ResultSet rs = stmt.getResultSet();
                
                // retrieve the retrieved Rentals
                while( rs.next() ) {
                    
                    id = rs.getLong( 1 );
                    pickupT = rs.getDate( 2 );
                    returnT = rs.getDate( 3 );
                    isLate = rs.getString( 4 );
                    charges = rs.getInt( 5 );
                    vehicleId = rs.getString( 6 );
                    customer = rs.getString( 7 );
                    reservationId = rs.getString( 8 );
                    
                    nextRental = objectLayer.createRental(); // create a proxy Rental object
                    // and now set its retrieved attributes
                    nextRental.setId( id );
                    nextRental.setPickupTime(pickupT);
                    nextRental.setReturnTime(returnT);
                    nextRental.setCharges(charges);
                    nextRental.setVehicle(null); //LAZY??
                    nextRental.setReservation(null); //LAZY??
                    
                    // set this to null for the "lazy" association traversal
                    //nextRental.setPersonFounder( null );
                    
                    Rental.add( nextRental );
                }
                
                return Rental;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new RARException( "RentalManager.restore: Could not restore persistent Rental objects; Root cause: " + e );
        }

        throw new RARException( "RentalManager.restore: Could not restore persistent Rental objects" );
    }

    public Comment restoreRentalComment( Rental rental ) throws RARException
    {
    	String selectRentalSql = "select com.text, com.date, com.userID, com.rentalID, rental.customer, rental.pickupTime, rental.returnTime, rental.condition " +
                " from Rentals rental, Comment com where 1 = 1 ";
       Statement    stmt = null;
       StringBuffer query = new StringBuffer( 100 );
       StringBuffer condition = new StringBuffer( 100 );
   	
       condition.setLength( 0 );
       
       // form the query based on the given Rental object instance
       query.append( selectRentalSql );
       
       if( rental != null ) {
           if( rental.getId() >= 0 ) // id is unique, so it is sufficient to get a rental
               query.append( " where id = " + rental.getId() );
           else {
           	if( rental.getCustomer()!= null ) // customer name is unique, so it is sufficient to get a rental
                condition.append( " where customer name = '" + rental.getCustomer() + "'" );
           	
           	if( rental.getPickupTime() != null )
           		if(condition.length() > 0 )
   					condition.append(" and ");
                condition.append( "address = '" + rental.getPickupTime() + "'" );

            if( rental.getReturnTime() != null )
            	if(condition.length() > 0 )
   		    		condition.append(" and ");
            	condition.append( "return time = '" + rental.getReturnTime()+ "'" );  
 
            if( rental.getVehicle().getCondition() != null )
               	if(condition.length() > 0 )
               		condition.append(" and ");
               	condition.append( "capacity = '" + rental.getVehicle().getCondition() + "'" );
           }
       }
       
       try {

           stmt = conn.createStatement();

           // retrieve the persistent comment objects
           //
           if( stmt.execute( query.toString() ) ) { // statement returned a result
               
               long   id;
               String text;
               Date date;
               int userId;
               int rentalId;
               Comment nextComment = null;
               
               ResultSet rs = stmt.getResultSet();
               
               // retrieve the retrieved clubs
               while( rs.next() ) {
                   
                   id = rs.getLong( 1 );
                   text = rs.getString( 2 );
                   date = rs.getDate( 3 );
                   userId = rs.getInt( 4 );
                   rentalId = rs.getInt( 5 );
                   
                   nextComment = objectLayer.createComment(text, date, null); // create a proxy comment object
                   // and now set its retrieved attributes
                   nextComment.setId( id );
                   
                   
                   // set this to null for the "lazy" association traversal
                   //nextReservation.setPersonFounder( null );
               }
               
               return nextComment;
           }
           else
        	   return null;
       }
       catch( Exception e ) {      // just in case...
           throw new RARException( "RentalManager.restore: Could not restore persistent Rental objects; Root cause: " + e );
       }

    }
    
    public Reservation restoreRentalReservation( Rental rental ) throws RARException
    {
    	String selectRentalSql = "select r.id, r.pickupDate, r.length, r.cancelled, r.typeId, r.customer, r.location, rental.customer, rental.pickupTime, rental.returnTime, rental.condition " +
                " from Rentals rental, Reservation r com where 1 = 1 ";
       Statement    stmt = null;
       StringBuffer query = new StringBuffer( 100 );
       StringBuffer condition = new StringBuffer( 100 );
   	
condition.setLength( 0 );
       
       // form the query based on the given Rental object instance
       query.append( selectRentalSql );
       
       if( rental != null ) {
           if( rental.getId() >= 0 ) // id is unique, so it is sufficient to get a rental
               query.append( " where id = " + rental.getId() );
           else {
           	if( rental.getCustomer()!= null ) // customer name is unique, so it is sufficient to get a rental
                condition.append( " where customer name = '" + rental.getCustomer() + "'" );
           	
           	if( rental.getPickupTime() != null )
           		if(condition.length() > 0 )
   					condition.append(" and ");
                condition.append( "address = '" + rental.getPickupTime() + "'" );

            if( rental.getReturnTime() != null )
            	if(condition.length() > 0 )
   		    		condition.append(" and ");
            	condition.append( "return time = '" + rental.getReturnTime()+ "'" );  
 
            if( rental.getVehicle().getCondition() != null )
               	if(condition.length() > 0 )
               		condition.append(" and ");
               	condition.append( "capacity = '" + rental.getVehicle().getCondition() + "'" );
           }
       }
       
       try {

           stmt = conn.createStatement();

           // retrieve the persistent Reservation objects
           //
           if( stmt.execute( query.toString() ) ) { // statement returned a result
               
        	   long   id;
               Date pickupT;
               int length;
               String cancelled;
               String customer;
               long typeId;
               long location;
               Reservation nextReservation = null;
               
               ResultSet rs = stmt.getResultSet();
               
               // retrieve the retrieved clubs
               while( rs.next() ) {
                   
                   id = rs.getLong( 1 );
                   pickupT = rs.getDate( 2 );
                   length = rs.getInt( 3 );
                   cancelled = rs.getString(4);
                   customer = rs.getString(5);
                   typeId = rs.getLong( 6 );
                   location = rs.getLong(7);
                   
                   nextReservation = objectLayer.createReservation(pickupT, length, null, null, null); // create a proxy reservation object
                   // and now set its retrieved attributes
                   nextReservation.setId( id );
                   // set this to null for the "lazy" association traversal
                   //nextReservation.setPersonFounder( null );
                   
               }
               
               return nextReservation;
           }
           else
        	   return null;
       }
       catch( Exception e ) {      // just in case...
           throw new RARException( "RentalManager.restore: Could not restore persistent Rental objects; Root cause: " + e );
       }

       		
    }
    
    public Vehicle restoreVehicleRental( Rental rental ) throws RARException
    {
    	String selectRentalSql = "select rental.address, rental.capacity, rental.name, v.reservationTag, v.lastService, v.make, v.milage, v.model, v.rentalLocation, v.status, v.vehicleType, v.vehicleYear, v.vehicleCondition " +
                "from vehicle v, rental rental where v.vehicleID = rental.rentalID";
       Statement    stmt = null;
       StringBuffer query = new StringBuffer( 100 );
       StringBuffer condition = new StringBuffer( 100 );
   	
       condition.setLength( 0 );
       
       // form the query based on the given Rental object instance
       query.append( selectRentalSql );
       
       if( rental != null ) {
           if( rental.getId() >= 0 ) // id is unique, so it is sufficient to get a rental
               query.append( " where id = " + rental.getId() );
           else {
           	if( rental.getCustomer()!= null ) // customer name is unique, so it is sufficient to get a rental
                condition.append( " where customer name = '" + rental.getCustomer() + "'" );
           	
           	if( rental.getPickupTime() != null )
           		if(condition.length() > 0 )
   					condition.append(" and ");
                condition.append( "address = '" + rental.getPickupTime() + "'" );

            if( rental.getReturnTime() != null )
            	if(condition.length() > 0 )
   		    		condition.append(" and ");
            	condition.append( "return time = '" + rental.getReturnTime()+ "'" );  
 
            if( rental.getVehicle().getCondition() != null )
               	if(condition.length() > 0 )
               		condition.append(" and ");
               	condition.append( "capacity = '" + rental.getVehicle().getCondition() + "'" );
           }
       }
       
       try {

           stmt = conn.createStatement();

           // retrieve the persistent Reservation objects
           //
           if( stmt.execute( query.toString() ) ) { // statement returned a result
               
               long   id;
               String make;
               String model;
               int year;
               int mileage;
               String tag;
               Date lastServiced;
               String status;
               String vcondition;
               int locationId;
               int typeId;
               Vehicle nextVehicle = null;
               
               ResultSet rs = stmt.getResultSet();
               
               // retrieve the retrieved vehicles
               while( rs.next() ) {
                   
                   id = rs.getLong( 1 );
                   make = rs.getString( 2 );
                   model = rs.getString( 3 );
                   year = rs.getInt( 4 );
                   mileage = rs.getInt( 5 );
                   tag = rs.getString( 6 );
                   lastServiced = rs.getDate( 7 );
                   status = rs.getString(8);
                   vcondition = rs.getString( 9 );
                   locationId = rs.getInt( 10);
                   typeId = rs.getInt( 11 );
                   
                   
                   nextVehicle = objectLayer.createVehicle(make, model, year, tag, mileage, lastServiced, null, null, null, null); // create a proxy vehicle object
                   // and now set its retrieved attributes
                   nextVehicle.setId( id );
                   
                   // set this to null for the "lazy" association traversal
                   //nextReservation.setPersonFounder( null );
                   
               }
               
               return nextVehicle;
           }
           else
        	   return null;
       }
       catch( Exception e ) {      // just in case...
           throw new RARException( "RentalManager.restore: Could not restore persistent Rental objects; Root cause: " + e );
       }
        	
    	
    }
    
    public void delete( Rental rental ) 
            throws RARException
    {
        String               deleteRentalSql = "delete from rental where id = ?";              
        PreparedStatement    stmt = null;
        int                  inscnt;
             
        if( !rental.isPersistent() ) // is the Club object persistent?  If not, nothing to actually delete
            return;
        
        try {
            stmt = (PreparedStatement) conn.prepareStatement( deleteRentalSql );         
            stmt.setLong( 1, rental.getId() );
            inscnt = stmt.executeUpdate();          
            if( inscnt == 1 ) {
                return;
            }
            else
                throw new RARException( "RentalManager.delete: failed to delete a Rental" );
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new RARException( "RentalManager.delete: failed to delete a Rental: " + e );        }
    }
}