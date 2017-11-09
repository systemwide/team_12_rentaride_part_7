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

public class RentalLocationManager {

	private ObjectLayer objectLayer = null;
    private Connection   conn = null;
    
    public RentalLocationManager( Connection conn, ObjectLayer objectLayer )
    {
        this.conn = conn;
        this.objectLayer = objectLayer;
    }
	
    public void store( RentalLocation rentalL ) 
            throws RARException
    {
    	 String               insertrentalLSql = "insert into rental location ( name, address, capacity ) values ( ?, ?, ? )";
         String               updaterentalLSql = "update rental location set name = ?, address = ?, capacity = ?, ? where id = ?";
         java.sql.PreparedStatement    stmt = null;
         int                  inscnt;
         long                 rentalLId;

         
         if( rentalL.getId() == -1 )
             throw new RARException( "RentalLocationManager.save: Attempting to save a Rental Location without a founder" );
                  
         try {

             if( !rentalL.isPersistent() )
                 stmt = (PreparedStatement) conn.prepareStatement(insertrentalLSql);
             else
            	 stmt = (PreparedStatement) conn.prepareStatement( updaterentalLSql );

             if( rentalL.getName() != null ) // name is unique unique and non null
                 stmt.setString( 1, rentalL.getName() );
             else 
                 throw new RARException( "RentalLocationManager.save: can't save a Rental Location: name undefined" );

             if( rentalL.getAddress() != null )
                 stmt.setString( 2, rentalL.getAddress() );
             else
            	 throw new RARException( "RentalLocationManager.save: can't save a Rental Location: address undefined" );

             if( rentalL.getCapacity() != 0 ) {
            	 stmt.setInt(3, rentalL.getCapacity());
             }
             else
            	 throw new RARException( "RentalLocationManager.save: can't save a Rental Location: capacity not set" );

             if( rentalL.isPersistent() )
                 stmt.setLong( 4, rentalL.getId() );

             inscnt = stmt.executeUpdate();

             if( !rentalL.isPersistent() ) {
                 if( inscnt >= 1 ) {
                     String sql = "select last_insert_id()";
                     if( stmt.execute( sql ) ) { // statement returned a result

                         // retrieve the result
                         ResultSet r = stmt.getResultSet();

                         // we will use only the first row!
                         //
                         while( r.next() ) {

                             // retrieve the last insert auto_increment value
                             rentalLId = r.getLong( 1 );
                             if( rentalLId > 0 )
                                 rentalL.setId( rentalLId ); // set this person's db id (proxy object)
                         }
                     }
                 }
                 else
                     throw new RARException( "RentalLocationManager.save: failed to save a Rental Location" );
             }
             else {
                 if( inscnt < 1 )
                     throw new RARException( "RentalLocationManager.save: failed to save a Rental Location" ); 
             }
         }
         catch( SQLException e ) {
             e.printStackTrace();
             throw new RARException( "RentalLocationManager.save: failed to save a Rental Location: " + e );
         }
     }
    
    public List<RentalLocation> restoreRentalLocation( RentalLocation modelRentalL ) 
            throws RARException
    {
        String       selectRentalLSql = "select name, address, capacity from club";
        Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );
        List<RentalLocation>   rentalL = new ArrayList<RentalLocation>();

        condition.setLength( 0 );
        
        // form the query based on the given Club object instance
        query.append( selectRentalLSql );
        
        if( modelRentalL != null ) {
            if( modelRentalL.getId() >= 0 ) // id is unique, so it is sufficient to get a person
                query.append( " where id = " + modelRentalL.getId() );
            else {
            	if( modelRentalL.getName() != null ) // userName is unique, so it is sufficient to get a person
                    condition.append( " where location name = '" + modelRentalL.getName() + "'" );
            	
                if( modelRentalL.getAddress() != null )
                	if( condition.length() > 0 )
                        condition.append( " and" );
                    condition.append( " where address = '" + modelRentalL.getAddress() + "'" );   

                if( modelRentalL.getCapacity() != 0 ) {
                    if( condition.length() > 0 )
                        condition.append( " and" );
                    condition.append( " where Capacity = '" + modelRentalL.getCapacity() + "'" );
                }

            }
        }
        
        try {

            stmt = conn.createStatement();

            // retrieve the persistent Club objects
            //
            if( stmt.execute( query.toString() ) ) { // statement returned a result
                
                long   id;
                String name;
                String address;
                int capacity;
                RentalLocation nextRentalL = null;
                
                ResultSet rs = stmt.getResultSet();
                
                // retrieve the retrieved clubs
                while( rs.next() ) {
                    
                    id = rs.getLong( 1 );
                    name = rs.getString( 2 );
                    address = rs.getString( 3 );
                    capacity = rs.getInt( 4 );
                    
                    nextRentalL = objectLayer.createRentalLocation(); // create a proxy rental location object
                    // and now set its retrieved attributes
                    nextRentalL.setId( id );
                    nextRentalL.setName( name );
                    nextRentalL.setAddress( address );
                    nextRentalL.setCapacity( capacity );
                    // set this to null for the "lazy" association traversal
                    //nextRentalL.setPersonFounder( null );
                    
                    rentalL.add( nextRentalL );
                }
                
                return rentalL;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new RARException( "RentalLocationManager.restore: Could not restore persistent Rental Location objects; Root cause: " + e );
        }

        throw new RARException( "RentalLocationManager.restore: Could not restore persistent Rental Location objects" );
    }
    
    
    public List<Reservation> restoreReservationRentalLocation( RentalLocation rentalL ) throws RARException
    {
    	String       selectRentalLocationSql = "select rentalL.rentalID, rentalL.name, rentalL.address, rentalL.capacity " +
                " from RentalLocations rl ";
        Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );
        List<Reservation>   res = new ArrayList<Reservation>();
    	
condition.setLength( 0 );
        
        // form the query based on the given Rental object instance
        query.append( selectRentalLocationSql );
        
        if( rentalL != null ) {
            if( rentalL.getId() >= 0 ) // id is unique, so it is sufficient to get a rental
                query.append( " where id = " + rentalL.getId() );
            else {
            	if( rentalL.getName() != null ) // customer name is unique, so it is sufficient to get a rental
                    condition.append( " where location name = '" + rentalL.getName() + "'" );
            	
            	if( rentalL.getAddress() != null )
            		if(condition.length() > 0 )
    					condition.append(" and ");
                    condition.append( "address = '" + rentalL.getAddress() + "'" );

                if( rentalL.getCapacity() != 0 )
                	if(condition.length() > 0 )
    					condition.append(" and ");
                    condition.append( "capacity = '" + rentalL.getCapacity()+ "'" );  
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
                    
                    nextReservation = objectLayer.createReservation(); // create a proxy club object
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
    
    public List<Vehicle> restoreVehicleRentalLocation( RentalLocation rentalLocation ) throws RARException
    {
    	 String selectRentalLocationSql = "select rentaL.address, rentaL.capacity, rentaL.name, v.reservationTag, v.lastService, v.make, v.milage, v.model, v.rentalLocation, v.status, v.vehicleType, v.vehicleYear, v.vehicleCondition " +
                 "from vehicle v, rentalLocation rentaL where v.vehicleID = rentaL.rentalID";
        Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );
        List<Vehicle>   vehicle = new ArrayList<Vehicle>();
    	
        condition.setLength( 0 );
        
        // form the query based on the given Rental object instance
        query.append( selectRentalLocationSql );
        
        if( rentalLocation != null ) {
            if( rentalLocation.getId() >= 0 ) // id is unique, so it is sufficient to get a rental
                query.append( " where id = " + rentalLocation.getId() );
            else {
            	if( rentalLocation.getName() != null ) // customer name is unique, so it is sufficient to get a rental
                    condition.append( " where location name = '" + rentalLocation.getName() + "'" );
            	
            	if( rentalLocation.getAddress() != null )
            		if(condition.length() > 0 )
    					condition.append(" and ");
                    condition.append( "address = '" + rentalLocation.getAddress() + "'" );

                if( rentalLocation.getCapacity() != 0 )
                	if(condition.length() > 0 )
    					condition.append(" and ");
                    condition.append( "capacity = '" + rentalLocation.getCapacity()+ "'" );  
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
                VehicleStatus status;
                String cond;
                VehicleCondition vcondition;
                long locationId;
                long typeId;
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
                    
                    String stat = rs.getString(8);
                    if ( stat == "INLOCATION" ) {
                    		status = VehicleStatus.INLOCATION;
                    }
                    else if ( stat == "INRENTAL" )
                    {
                    		status = VehicleStatus.INRENTAL;
                    } 
                    else {
                    		throw new RARException("Not a valid status!!");
                    }
                    
                    cond = rs.getString( 9 );
                    
                    if(cond == "GOOD") {
                    		vcondition = VehicleCondition.GOOD;
                    } else if (stat == "NEEDSMAINTANCE") {
                    		vcondition = VehicleCondition.NEEDSMAINTENANCE;
                    } else {
                    		throw new RARException("Not a valid condition!!");
                    }
                    
                    locationId = rs.getLong( 10);
                    typeId = rs.getLong( 11 );
                    
                    //RentalLocation loc = 
                    
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
                    nextVehicle.setCondition(vcondition);
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
    
    public void delete( RentalLocation rentalL ) 
            throws RARException
    {
        String               deleteRentalLSql = "delete from Rental Location where id = ?";              
        PreparedStatement    stmt = null;
        int                  inscnt;
             
        if( !rentalL.isPersistent() ) // is the Club object persistent?  If not, nothing to actually delete
            return;
        
        try {
            stmt = (PreparedStatement) conn.prepareStatement( deleteRentalLSql );         
            stmt.setLong( 1, rentalL.getId() );
            inscnt = stmt.executeUpdate();          
            if( inscnt == 1 ) {
                return;
            }
            else
                throw new RARException( "RentalLocationManager.delete: failed to delete a Rental Location" );
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new RARException( "RentalLocationManager.delete: failed to delete a Rental Location: " + e ); 
            }
    
    }
}

    
