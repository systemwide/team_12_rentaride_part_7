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
        String               updateRentalSql = "update rental set customer = ?, pickupDate = ?, return = ?, late = ?, charges = ?, vehicleID = ?, reservation = ? where id = ?";
        PreparedStatement    stmt = null;
        int                  inscnt;
        long                 rentalId;

        /*
        if( club.getFounderId() == -1 )
            throw new ClubsException( "ClubManager.save: Attempting to save a Club without a founder" );
            */
                 
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
            	throw new RARException( "RentalManager.save: can't save a Rental: PickupTime undefined" );

            if( rental.getReturnTime() != null )
                stmt.setDate( 3, rental.getReturnTime()  );
            else
            	throw new RARException( "RentalManager.save: can't save a Rental: ReturnTime undefined" );
            
            if( rental.getLate() != false )
                stmt.setString( 4, rental.getLate()  );
            else
            	throw new RARException( "RentalManager.save: can't save a Rental: late undefined" );
            
            if( rental.getCharges() != 0 )
                stmt.setString( 5, rental.getCharges()  );
            else
            	throw new RARException( "RentalManager.save: can't save a Rental: Charges undefined" );
           
            if( rental.getVehicle()!= null )
                stmt.setString( 6, rental.getVehicle()  );
            else
            	throw new RARException( "RentalManager.save: can't save a Rental: VehicleId undefined" );
            
            if( rental.getReservation()!= null )
                stmt.setString( 7, rental.getReservation()  );
            else
            	throw new RARException( "RentalManager.save: can't save a Rental: ReservationId undefined" );

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
        String       selectClubSql = "select id, name, address, established from club";
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
                    condition.append( "vehicleID = '" + modelRental.getLate() + "'" );
                
                    if( modelRental.getCharges() != 0 )
                    if(condition.length() > 0 )
        				condition.append(" and ");
                    condition.append( "charges = '" + modelRental.getCharges() + "'" );
                    
                if( modelRental.getVehicle() != null )
                	if(condition.length() > 0 )
    					condition.append(" and ");
                    condition.append( "vehicleID = '" + modelRental.getVehicle() + "'" );
                    
                if( modelRental.getReservation() != null )
                  	if(condition.length() > 0 )
        				condition.append(" and ");
                    condition.append( "reservationID = '" + modelRental.getReservation() + "'" );

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

    public List<Comment> restoreRentalComment( Rental rental ) throws RARException
    {
    	String selectRentalSql = "select com.commendDate, com.comment, com.rental, rental.customer, rental.pickupTime, rental.returnTime, rental.condition " +
                " from Rentals rental, Comment com where 1 = 1 ";
       Statement    stmt = null;
       StringBuffer query = new StringBuffer( 100 );
       StringBuffer condition = new StringBuffer( 100 );
       List<Comment>   comment = new ArrayList<Comment>();
   	
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
               Date comDate;
               int userId;
               int rentalId;
               Comment nextComment = null;
               
               ResultSet rs = stmt.getResultSet();
               
               // retrieve the retrieved clubs
               while( rs.next() ) {
                   
                   id = rs.getLong( 1 );
                   text = rs.getString( 2 );
                   comDate = rs.getDate( 3 );
                   userId = rs.getInt( 4 );
                   rentalId = rs.getInt( 5 );
                   
                   nextComment = objectLayer.createComment(); // create a proxy comment object
                   // and now set its retrieved attributes
                   nextComment.setId( id );
                   nextComment.setText(text);
                   nextComment.setDate(comDate);
                   
                   
                   // set this to null for the "lazy" association traversal
                   //nextReservation.setPersonFounder( null );
                   
                   comment.add( nextComment );
               }
               
               return comment;
           }
       }
       catch( Exception e ) {      // just in case...
           throw new RARException( "RentalManager.restore: Could not restore persistent Rental objects; Root cause: " + e );
       }

       throw new RARException( "RentalManager.restore: Could not restore persistent Rental objects" );	}
    
    public List<Reservation> restoreRentalReservation( Rental rental ) throws RARException
    {
    	String selectRentalSql = "select com.commendDate, com.comment, com.rental, rental.customer, rental.pickupTime, rental.returnTime, rental.condition " +
                " from Rentals rental, Comment com where 1 = 1 ";
       Statement    stmt = null;
       StringBuffer query = new StringBuffer( 100 );
       StringBuffer condition = new StringBuffer( 100 );
       List<Reservation>   res = new ArrayList<Reservation>();
   	
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
               int typeId;
               String location;
               Reservation nextReservation = null;
               
               ResultSet rs = stmt.getResultSet();
               
               // retrieve the retrieved clubs
               while( rs.next() ) {
                   
                   id = rs.getLong( 1 );
                   pickupT = rs.getDate( 2 );
                   length = rs.getInt( 3 );
                   
                   nextReservation = objectLayer.createReservation(); // create a proxy reservation object
                   // and now set its retrieved attributes
                   nextReservation.setId( id );
                   nextReservation.setPickupTime(pickupT);
                   nextReservation.setLength(length);
                   // set this to null for the "lazy" association traversal
                   //nextReservation.setPersonFounder( null );
                   
                   res.add( nextReservation );
               }
               
               return res;
           }
       }
       catch( Exception e ) {      // just in case...
           throw new RARException( "RentalManager.restore: Could not restore persistent Rental objects; Root cause: " + e );
       }

       throw new RARException( "RentalManager.restore: Could not restore persistent Rental objects" );		
    }
    
    public List<Vehicle> restoreVehicleRental( Rental rental ) throws RARException
    {
    	String selectRentalSql = "select rental.address, rental.capacity, rental.name, v.reservationTag, v.lastService, v.make, v.milage, v.model, v.rentalLocation, v.status, v.vehicleType, v.vehicleYear, v.vehicleCondition " +
                "from vehicle v, rental rental where v.vehicleID = rental.rentalID";
       Statement    stmt = null;
       StringBuffer query = new StringBuffer( 100 );
       StringBuffer condition = new StringBuffer( 100 );
       List<Vehicle> vehicle = new ArrayList<Vehicle>();   //THIS IS PROBABLY NOT RIGHT!!!
   	
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
               
               // retrieve the retrieved clubs
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
                   
                   
                   nextVehicle = objectLayer.createVehicle(); // create a proxy club object
                   // and now set its retrieved attributes
                   nextVehicle.setId( id );
                   nextVehicle.setMake(make);
                   nextVehicle.setModel(model);
                   nextVehicle.setYear(year);
                   nextVehicle.setMileage(mileage);
                   nextVehicle.setRegistrationTag(tag);
                   nextVehicle.setLastServiced(lastServiced);
                   nextVehicle.setStatus(status);
                   nextVehicle.setCondition(condition);
                   nextVehicle.setRentalLocation(locationId);
                   nextVehicle.setVehicleType(typeId);
                   
                   // set this to null for the "lazy" association traversal
                   //nextReservation.setPersonFounder( null );
                   
                   vehicle.add( nextVehicle );
               }
               
               return vehicle;
           }
       }
       catch( Exception e ) {      // just in case...
           throw new RARException( "RentalManager.restore: Could not restore persistent Rental objects; Root cause: " + e );
       }

       throw new RARException( "RentalManager.restore: Could not restore persistent Rental objects" );	
  	
    	
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