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

public class VehicleManager {

	private ObjectLayer objectLayer = null;
    private Connection   conn = null;
    
    public VehicleManager( Connection conn, ObjectLayer objectLayer )
    {
        this.conn = conn;
        this.objectLayer = objectLayer;
    }
    
    public void storeVehicle( Vehicle vehicle ) throws RARException
    {
    	String insertVehicleSql = "insert into Vehicle ( make, model, year, mileage, tag, lastService, status, " + 
				  " condition, locationId, typeId) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
        String updateVehicleSql = "update Vehicle set make = ?, model = ?, year = ?, mileage = ?, " + 
				  " tag = ?, lastServiced = ?, status = ?, condition = ?, locationId = ?, typeId = ? where id = ?";
        PreparedStatement    stmt = null;
        int                  inscnt;
        long                 vehicleId;      
              
        try {

            if( !vehicle.isPersistent() )
                stmt = (PreparedStatement) conn.prepareStatement( insertVehicleSql );
            else
                stmt = (PreparedStatement) conn.prepareStatement( updateVehicleSql );

            if( vehicle.getMake() != null ) // name is unique unique and non null
                stmt.setString( 1, vehicle.getMake() );
            else 
                throw new RARException( "VehicleManager.save: can't save a Vehicle: make undefined" );
            
            if( vehicle.getModel()  != null ) // name is unique unique and non null
                stmt.setString( 2, vehicle.getModel());
            else 
                throw new RARException( "VehicleManager.save: can't save a Vehicle: model undefined" );

            if( vehicle.getYear() != 0 )
                stmt.setInt( 3, vehicle.getYear());
            else
            	throw new RARException( "VehicleManager.save: can't save a Vehicle: year undefined" );
        
            if( vehicle.getMileage()!= 0 )
                stmt.setString( 4, vehicle.getMileage() );
            else
                throw new RARException( "VehicleManager.save: can't save a Vehicle: mileage undefined" );
            
            if( vehicle.getRegistrationTag() != null )
                stmt.setString( 5, vehicle.getRegistrationTag() );
            else
            	throw new RARException( "VehicleManager.save: can't save a Vehicle: tag undefined" );
            
            if( vehicle.getLastServiced() != null )
                stmt.setDate( 6, vehicle.getLastServiced() );
            else
            	throw new RARException( "VehicleManager.save: can't save a Vehicle: last serviced undefined" );

            if( vehicle.getStatus() != null )
                stmt.setString( 7, vehicle.getStatus() );
            else
            	throw new RARException( "VehicleManager.save: can't save a Vehicle: status undefined" );

            if( vehicle.getCondition() != null )
                stmt.setString( 8, vehicle.getCondition());
            else
            	throw new RARException( "VehicleManager.save: can't save a Vehicle: condition undefined" );

            if( vehicle.getRentalLocation()!= null )
                stmt.setString( 9, vehicle.getRentalLocation().getId() );
            else
            	throw new RARException( "VehicleManager.save: can't save a Vehicle: rental location id undefined" );

            if( vehicle.getVehicleType()!= null )
                stmt.setString( 10, vehicle.getVehicleType().getId() );
            else
            	throw new RARException( "VehicleManager.save: can't save a Vehicle: last serviced undefined" );
            
            if( vehicle.isPersistent() )
                stmt.setLong( 11, vehicle.getId() );

            inscnt = stmt.executeUpdate();

            if( !vehicle.isPersistent() ) {
                if( inscnt >= 1 ) {
                    String sql = "select last_insert_id()";
                    if( stmt.execute( sql ) ) { // statement returned a result

                        // retrieve the result
                        ResultSet r = stmt.getResultSet();

                        // we will use only the first row!
                        //
                        while( r.next() ) {

                            // retrieve the last insert auto_increment value
                        	vehicleId = r.getLong( 1 );
                            if( vehicleId > 0 )
                            	vehicle.setId( vehicleId ); // set this Vehicle's db id (proxy object)
                        }
                    }
                }
                else
                    throw new RARException( "VehicleManager.save: failed to save a Vehicle" );
            }
            else {
                if( inscnt < 1 )
                    throw new RARException( "VehicleManager.save: failed to save a Vehicle" ); 
            }
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new RARException( "VehicleManager.save: failed to save a Vehicle: " + e );
        }
    }
	
    public List<Vehicle> restoreVehicle( Vehicle modelVehicle ) throws RARException
    {
        String       selectVehicleSql = "select v.make, v.model, v.year, v.mileage, v.tag, v.lastService, v.status, " + 
        							 " v.condition,v.locationID, v.vehicleType, v.vehicleID from Vehicle v where 1=1" ;
        Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );
        List<Vehicle> vehicles = new ArrayList<Vehicle>();

        condition.setLength( 0 );
        
        // form the query based on the given Club object instance
        query.append( selectVehicleSql );
        
        if( modelVehicle != null ) {
            if( modelVehicle.getId() >= 0 ) // id is unique, so it is sufficient to get a person
                query.append( " where id = " + modelVehicle.getId() );
            else if( modelVehicle.getRegistrationTag() != null ) // userName is unique, so it is sufficient to get a person
                query.append( " where registrationTag = '" + modelVehicle.getRegistrationTag() + "'" );
            else
            {   
                if( modelVehicle.getMake() != null )
                	if(condition.length() > 0 )
    					condition.append(" and ");
                    condition.append( " make = '" + modelVehicle.getMake() + "'" );   
                
                if( modelVehicle.getYear()!= 0 )
                  	if(condition.length() > 0 )
        				condition.append(" and ");
                    query.append( "vehicleYear = '" + modelVehicle.getYear() + "'" );    
                
                if( modelVehicle.getModel() != null )
                	if(condition.length() > 0 )
    					condition.append(" and ");
                    query.append( "model = '" + modelVehicle.getModel() + "'" );  
                
               if( modelVehicle.getMileage() != 0 )
                   	if(condition.length() > 0 )
        				condition.append(" and ");
                    query.append( "mileage = '" + modelVehicle.getMileage() + "'" );        
                        
               if( modelVehicle.getLastServiced() != null )
                  	if(condition.length() > 0 )
                  		condition.append(" and ");
                    condition.append( "lastService = '" + modelVehicle.getLastServiced() + "'" );    
                
               if( modelVehicle.getStatus() != null )
                	if(condition.length() > 0 )
    					condition.append(" and ");
                    query.append( "status = '" + modelVehicle.getStatus() + "'" );   
               
               if( modelVehicle.getCondition() != null )
                  	if(condition.length() > 0 )
       					condition.append(" and ");
                    query.append( "vehicleCondition = '" + modelVehicle.getCondition() + "'" );     
                    
               if( modelVehicle.getRentalLocation() != null )
                  	if(condition.length() > 0 )
                  		condition.append(" and ");
                    query.append( "rentalLocation = '" + modelVehicle.getRentalLocation() + "'" );     
                    
                if( modelVehicle.getVehicleType() != null )
                	if(condition.length() > 0 )
    					condition.append(" and ");
                    query.append( "vehicleType = '" + modelVehicle.getVehicleType() + "'" ); 
            }
        }
        
        try {

            stmt = conn.createStatement();

            // retrieve the persistent Club objects
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
                int rentalLoc;
                int vehicleID;
                Vehicle   nextVehcle = null;
                
                ResultSet rs = stmt.getResultSet();
                
                // retrieve the retrieved customers
                while( rs.next() ) {
                    
                    id = rs.getLong( 1 );
                    make = rs.getString( 2 );
                    model = rs.getString(3);
                    year = rs.getInt( 4 );
                    mileage = rs.getInt(5);
                    tag = rs.getString( 6 );
                    lastServiced = rs.getDate(7);
                    status = rs.getString( 8);
                    vcondition = rs.getString(9);
                    rentalLoc = rs.getInt(10);
                    vehicleID = rs.getInt(11);
                    
                    nextVehcle = objectLayer.createVehicle(); // create a proxy vehicle object
                    // and now set its retrieved attributes
                    nextVehcle.setId( id );
                    nextVehcle.setMake(make);
                    nextVehcle.setModel( model );
                    nextVehcle.setYear(year);
                    nextVehcle.setMileage(mileage);
                    nextVehcle.setRegistrationTag(tag);
                    nextVehcle.setLastServiced(lastServiced);
                    nextVehcle.setStatus(status);
                    nextVehcle.setCondition(condition);
                    nextVehcle.setRentalLocation(rentalLoc);
                    nextVehcle.setVehicleType(vehicleId);
                    
                    // set this to null for the "lazy" association traversal
                    //nextCustomer.setPersonFounder( null );
                    
                    vehicles.add( nextVehcle );
                }
                
                return vehicles;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new RARException( "VehiclesManager.restore: Could not restore persistent Vehicle objects; Root cause: " + e );
        }

        throw new RARException( "VehiclesManager.restore: Could not restore persistent Vehicle objects" );
    
    }
	
    public void deleteVehicle( Vehicle vehicle ) throws RARException
    {
		String               deleteVehicleSql = "delete from Vehicle where id = ?";              
	    PreparedStatement    stmt = null;
	    int                  inscnt;
	             
	    if( !vehicle.isPersistent() ) // is the Comment object persistent?  If not, nothing to actually delete
	    	return;
	        
	    try {
	    	stmt = (PreparedStatement) conn.prepareStatement( deleteVehicleSql );         
	    	stmt.setLong( 1, vehicle.getId() );
	    	inscnt = stmt.executeUpdate();          
	    	if( inscnt == 1 ) {
	    		return;
	        }
	    	else	
	    		throw new RARException( "VehicleManager.delete: failed to delete a Vehicle" );
	        }	
	        catch( SQLException e ) 
	    	{
	        	e.printStackTrace();
	            throw new RARException( "VehicleManager.delete: failed to delete a Vehicle: " + e );       
	    	}
	 }

    public RentalLocation restoreVehicleRentalLocation( Vehicle vehicle ) throws RARException
    {
    	String       selectVehicleSql = "select rentalL.address, rentalL.capacity, rentalL.rentalLocation, v.tag, v.lastService, v.make, v.mileage, v.model, v.locationID, " + 
				 " v.status, v.vehicleType, v.year, v.condition, v.vehicleID, from Vehicle v, RentalLocation rentalL where v.vehicleId = rentalL.renatalID " ;
    	Statement    stmt = null;
    	StringBuffer query = new StringBuffer( 100 );
    	StringBuffer condition = new StringBuffer( 100 );
    	

    	condition.setLength( 0 );

    	// form the query based on the given Club object instance
    	query.append( selectVehicleSql );

    	if( vehicle != null ) {
    		if( vehicle.getId() >= 0 ) // id is unique, so it is sufficient to get a person
    			query.append( " where id = " + vehicle.getId() );
    		else if( vehicle.getRegistrationTag() != null ) // userName is unique, so it is sufficient to get a person
    			query.append( " where registrationTag = '" + vehicle.getRegistrationTag() + "'" );
    		else {   
    			if( vehicle.getMake() != null )
    				if(condition.length() > 0 )
    					condition.append(" and ");
    				condition.append( " make = '" + vehicle.getMake() + "'" );   
            
    			if( vehicle.getYear()!= 0 )
    				if(condition.length() > 0 )
    					condition.append(" and ");
                	condition.append( "vehicleYear = '" + vehicle.getYear() + "'" );    
            
            if( vehicle.getModel() != null )
            	if(condition.length() > 0 )
					condition.append(" and ");
                condition.append( "model = '" + vehicle.getModel() + "'" );  
            
           if( vehicle.getMileage() != 0 )
               	if(condition.length() > 0 )
    				condition.append(" and ");
                condition.append( "mileage = '" + vehicle.getMileage() + "'" );        
                    
           if( vehicle.getLastServiced() != null )
              	if(condition.length() > 0 )
              		condition.append(" and ");
                condition.append( "lastService = '" + vehicle.getLastServiced() + "'" );    
            
           if( vehicle.getStatus() != null )
            	if(condition.length() > 0 )
					condition.append(" and ");
                condition.append( "status = '" + vehicle.getStatus() + "'" );   
           
           if( vehicle.getCondition() != null )
              	if(condition.length() > 0 )
   					condition.append(" and ");
                condition.append( "vehicleCondition = '" + vehicle.getCondition() + "'" );     
                
           if( vehicle.getRentalLocation() != null )
              	if(condition.length() > 0 )
              		condition.append(" and ");
                condition.append( "rentalLocation = '" + vehicle.getRentalLocation() + "'" );     
                
            if( vehicle.getVehicleType() != null )
            	if(condition.length() > 0 )
					condition.append(" and ");
                condition.append( "vehicleType = '" + vehicle.getVehicleType() + "'" ); 
        }
    }
    	try {

            stmt = conn.createStatement();

            // retrieve the persistent Rental Location objects
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
            throw new RARException( "VehicleManager.restore: Could not restore persistent Vehicle objects; Root cause: " + e );
        }

        throw new RARException( "VehicleManager.restore: Could not restore persistent Vehicle objects" );
    }
    
    public VehicleType restoreVehicleVehicleType( Vehicle vehicle ) throws RARException
    {
    	String       selectVehicleTypeSql = "select vt.typeName, v.make, v.model, v.year, v.mileage, v.tag, v.lastService v.locationID, " + 
				 " v.status, v.vehicleType, v.condition, v.vehicleID, from Vehicle v, VehicleType vt where v.vehicleId = vt.vehicleTypeId " ;   	
    	Statement    stmt = null;
    	StringBuffer query = new StringBuffer( 100 );
    	StringBuffer condition = new StringBuffer( 100 );
   	

    	condition.setLength( 0 );

    	// form the query based on the given Club object instance
    	query.append( selectVehicleTypeSql );

    	if( vehicle != null ) {
   		if( vehicle.getId() >= 0 ) // id is unique, so it is sufficient to get a person
   			query.append( " where id = " + vehicle.getId() );
   		else if( vehicle.getRegistrationTag() != null ) // userName is unique, so it is sufficient to get a person
   			query.append( " where registrationTag = '" + vehicle.getRegistrationTag() + "'" );
   		else {   if( vehicle.getMake() != null )
           	if(condition.length() > 0 )
					condition.append(" and ");
               condition.append( " make = '" + vehicle.getMake() + "'" );   
           
           if( vehicle.getYear()!= 0 )
             	if(condition.length() > 0 )
   				condition.append(" and ");
               condition.append( "vehicleYear = '" + vehicle.getYear() + "'" );    
           
           if( vehicle.getModel() != null )
           	if(condition.length() > 0 )
					condition.append(" and ");
               condition.append( "model = '" + vehicle.getModel() + "'" );  
           
          if( vehicle.getMileage() != 0 )
              	if(condition.length() > 0 )
   				condition.append(" and ");
               condition.append( "mileage = '" + vehicle.getMileage() + "'" );        
                   
          if( vehicle.getLastServiced() != null )
             	if(condition.length() > 0 )
             		condition.append(" and ");
               condition.append( "lastService = '" + vehicle.getLastServiced() + "'" );    
           
          if( vehicle.getStatus() != null )
           	if(condition.length() > 0 )
					condition.append(" and ");
               query.append( "status = '" + vehicle.getStatus() + "'" );   
          
          if( vehicle.getCondition() != null )
             	if(condition.length() > 0 )
  					condition.append(" and ");
               condition.append( "vehicleCondition = '" + vehicle.getCondition() + "'" );     
               
          if( vehicle.getRentalLocation() != null )
             	if(condition.length() > 0 )
             		condition.append(" and ");
               condition.append( "rentalLocation = '" + vehicle.getRentalLocation() + "'" );     
               
           if( vehicle.getVehicleType() != null )
           	if(condition.length() > 0 )
					condition.append(" and ");
               condition.append( "vehicleType = '" + vehicle.getVehicleType() + "'" ); 
       }
   }
   	try {

           stmt = conn.createStatement();

           // retrieve the persistent Rental Location objects
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
           throw new RARException( "VehicleManager.restore: Could not restore persistent Vehicle objects; Root cause: " + e );
       }

       throw new RARException( "VehicleManager.restore: Could not restore persistent Vehicle objects" );
    	
    }
    
    public List<Rental> restoreVehicleRental( Vehicle vehicle ) throws RARException
    {
    	String       selectVRentalSql = "select rental.customer, rental.pickup, rental.return v.make, v.model, v.year, v.mileage, v.tag, v.lastService v.locationID, " + 
				 " v.status, v.vehicleType, v.condition, v.vehicleID, from Vehicle v, Rental rental where v.vehicleId = vt.vehicleTypeId " ;   	
    	Statement    stmt = null;
    	StringBuffer query = new StringBuffer( 100 );
    	StringBuffer condition = new StringBuffer( 100 );
    	List<Rental> rental = new ArrayList<Rental>();

  	condition.setLength( 0 );

  	// form the query based on the given Club object instance
  	query.append( selectVehicleTypeSql );

  	if( vehicle != null ) {
  		if( vehicle.getId() >= 0 ) // id is unique, so it is sufficient to get a person
  			query.append( " where id = " + vehicle.getId() );
  		else if( vehicle.getRegistrationTag() != null ) // userName is unique, so it is sufficient to get a person
  			query.append( " where registrationTag = '" + vehicle.getRegistrationTag() + "'" );
  		else {   if( vehicle.getMake() != null )
          	if(condition.length() > 0 )
					condition.append(" and ");
              condition.append( " make = '" + vehicle.getMake() + "'" );   
          
          if( vehicle.getYear()!= 0 )
            	if(condition.length() > 0 )
  				condition.append(" and ");
              condition.append( "vehicleYear = '" + vehicle.getYear() + "'" );    
          
          if( vehicle.getModel() != null )
          	if(condition.length() > 0 )
					condition.append(" and ");
              condition.append( "model = '" + vehicle.getModel() + "'" );  
          
         if( vehicle.getMileage() != 0 )
             	if(condition.length() > 0 )
  				condition.append(" and ");
              condition.append( "mileage = '" + vehicle.getMileage() + "'" );        
                  
         if( vehicle.getLastServiced() != null )
            	if(condition.length() > 0 )
            		condition.append(" and ");
              condition.append( "lastService = '" + vehicle.getLastServiced() + "'" );    
          
         if( vehicle.getStatus() != null )
          	if(condition.length() > 0 )
					condition.append(" and ");
              query.append( "status = '" + vehicle.getStatus() + "'" );   
         
         if( vehicle.getCondition() != null )
            	if(condition.length() > 0 )
 					condition.append(" and ");
              condition.append( "vehicleCondition = '" + vehicle.getCondition() + "'" );     
              
         if( vehicle.getRentalLocation() != null )
            	if(condition.length() > 0 )
            		condition.append(" and ");
              condition.append( "rentalLocation = '" + vehicle.getRentalLocation() + "'" );     
              
          if( vehicle.getVehicleType() != null )
          	if(condition.length() > 0 )
					condition.append(" and ");
              condition.append( "vehicleType = '" + vehicle.getVehicleType() + "'" ); 
      }
  }
  	try {

          stmt = conn.createStatement();

          // retrieve the persistent Rental Location objects
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
          throw new RARException( "VehicleManager.restore: Could not restore persistent Vehicle objects; Root cause: " + e );
      }

      throw new RARException( "VehicleManager.restore: Could not restore persistent Vehicle objects" );
  }
}