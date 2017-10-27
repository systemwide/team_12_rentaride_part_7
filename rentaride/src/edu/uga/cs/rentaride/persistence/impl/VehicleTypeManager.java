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

public class VehicleTypeManager {

	private ObjectLayer objectLayer = null;
    private Connection   conn = null;
    
    public VehicleTypeManager( Connection conn, ObjectLayer objectLayer )
    {
        this.conn = conn;
        this.objectLayer = objectLayer;
    }
	
	
	public void storeVehicleType( VehicleType vehicle ) throws RARException
	{
		String               insertRentalSql = "insert into vehicle type ( name ) values ( ?)";
        String               updateRentalSql = "update vehicle type set name = ? where id = ?";
        PreparedStatement    stmt = null;
        int                  inscnt;
        long                 vehicleId;
                 
        try {

            if( !vehicle.isPersistent() )
                stmt = (PreparedStatement) conn.prepareStatement( insertRentalSql );
            else
                stmt = (PreparedStatement) conn.prepareStatement( updateRentalSql );

            if( vehicle.getName() != null ) // name is unique and non null
                stmt.setString( 1, vehicle.getName() );
            else 
                throw new RARException( "RentalManager.save: can't save a Rental: name undefined" );

            if( vehicle.isPersistent() )
                stmt.setLong( 2, vehicle.getId() );

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
                            	vehicle.setId( vehicleId ); // set this vehicle's db id (proxy object)
                        }
                    }
                }
                else
                    throw new RARException( "VehicleTypeManager.save: failed to save a Vehicle Type" );
            }
            else {
                if( inscnt < 1 )
                    throw new RARException( "VehicleTypeManager.save: failed to save a Vehicle Type" ); 
            }
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new RARException( "VehicleTypeManager.save: failed to save a Vehicle Type: " + e );
        }
    }
	
	public List<VehicleType> restoreVehicleType( VehicleType modelVehicleType ) throws RARException
	{
		String       selectVehicleSql = "select id and name from vehicle type";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		StringBuffer condition = new StringBuffer( 100 );
		List<VehicleType> vehicles = new ArrayList<VehicleType>();

		condition.setLength( 0 );

		// form the query based on the given Club object instance
		query.append( selectVehicleSql );

		if( modelVehicleType != null ) {
			if( modelVehicleType.getId() >= 0 ) // id is unique, so it is sufficient to get a person
				query.append( " where id = " + modelVehicleType.getId() );
			else if( modelVehicleType.getName() != null ) // userName is unique, so it is sufficient to get a person
				query.append( " where name = '" + modelVehicleType.getName() + "'" );
			}

		try {

			stmt = conn.createStatement();

			// retrieve the persistent Vehicle type objects
			//
			if( stmt.execute( query.toString() ) ) { // statement returned a result

				long   id;
				String name;
				
				VehicleType   nextVehicle = null;

				ResultSet rs = stmt.getResultSet();

				// retrieve the retrieved customers
				while( rs.next() ) {

					id = rs.getLong( 1 );
					name = rs.getString( 2 );

					nextVehicle = objectLayer.createVehicleType(); // create a proxy vehicle type object
					// and now set its retrieved attributes
					nextVehicle.setId( id );
					nextVehicle.setName(name);
					

					// set this to null for the "lazy" association traversal
					//nextCustomer.setPersonFounder( null );

					vehicles.add( nextVehicle );
				}

				return vehicles;
			}
		}
		catch( Exception e ) {      // just in case...
			throw new RARException( "VehicleTypeManager.restore: Could not restore persistent Vehicle Type objects; Root cause: " + e );
		}

		throw new RARException( "VehicleTypeManager.restore: Could not restore persistent Vehicle Type objects" );	
	}
	
	public void deleteVehicleType( VehicleType vehicleType ) throws RARException
	{
		String               deleteVehicleTypeSql = "delete from Vehicle Type where id = ?";              
	    PreparedStatement    stmt = null;
	    int                  inscnt;
	             
	    if( !vehicleType.isPersistent() ) // is the Vehicle Type object persistent?  If not, nothing to actually delete
	    	return;
	        
	    try {
	    	stmt = (PreparedStatement) conn.prepareStatement( deleteVehicleTypeSql );         
	    	stmt.setLong( 1, vehicleType.getId() );
	    	inscnt = stmt.executeUpdate();          
	    	if( inscnt == 1 ) {
	    		return;
	        }
	    	else	
	    		throw new RARException( "VehicleTypeManager.delete: failed to delete a Vehicle Type" );
	        }	
	        catch( SQLException e ) 
	    	{
	        	e.printStackTrace();
	            throw new RARException( "VehicleTypeManager.delete: failed to delete a Vehicle Type: " + e );       
	    	}
	 }
	
	public List<Reservation> restoreReservationVehicleType( VehicleType vehicleType ) throws RARException
	{
		String       selectVehicleTypeSql = "select r.id, r.customer, r.pickupTime, r.rental, r.length, r.locationID, r.typeID, " + 
				   "  vt.typeName, vt.id from RentalLocation r, VehicleType vt where r.reservationID = vt.typeId";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		StringBuffer condition = new StringBuffer( 100 );
		List<Reservation> res = new ArrayList<Reservation>();

		condition.setLength( 0 );

		// form the query based on the given Club object instance
		query.append( selectVehicleTypeSql );

		if( vehicleType != null ) {
			if( vehicleType.getId() >= 0 ) // id is unique, so it is sufficient to get a person
				query.append( " where id = " + vehicleType.getId() );
			else if( vehicleType.getName() != null ) // userName is unique, so it is sufficient to get a person
				query.append( " where name = '" + vehicleType.getName() + "'" );
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
	               
	               // retrieve the retrieved reservations
	               while( rs.next() ) {
	                   
	                   id = rs.getLong( 1 );
	                   pickupT = rs.getDate( 2 );
	                   length = rs.getInt( 3 );
	                   cancelled = rs.getString(4);
	                   customer = rs.getString(5);
	                   typeId = rs.getLong(6);
	                   location = rs.getLong(7);
	                   
	                   nextReservation = objectLayer.createReservation(); // create a proxy reservation object
	                   // and now set its retrieved attributes
	                   nextReservation.setId( id );
	                   nextReservation.setCustomer(null); //LAZY??
	                   nextReservation.setPickupTime(pickupT);
	                   nextReservation.setLength(length);
	                   nextReservation.setRentalLocation(null); //LAZY??
	                   nextReservation.setVehicleType(null); //LAZY??
	                   nextReservation.setRental(null);
	                   // set this to null for the "lazy" association traversal
	                   //nextReservation.setPersonFounder( null );
	                   
	                   res.add( nextReservation );
	               }
	               
	               return res;
	           }
		
		}catch( Exception e ) {      // just in case...
        throw new RARException( "VehicleTypeManager.restore: Could not restore persistent Vehicle Type objects; Root cause: " + e );
    }

    throw new RARException( "VehicleTypeManager.restore: Could not restore persistent Vehicle Type objects" );		
 }
	
	public List<Vehicle> restoreVehicleVehicleType( VehicleType vehicleType ) throws RARException
	{
		String       selectVehicleTypeSql = "select v.make, v.model, v.year, v.mileage, v.tag, v.lastService, v.status, " +
				   " v.condition,v.locationID, v.vehicleType, v.vehicleID" +
				   "  vt.typeName, vt.vehicleTypeId from Vehicle v, VehicleType vt where v.vehicleID = vt.vehicleTypeId";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		StringBuffer condition = new StringBuffer( 100 );
		List<Vehicle> vehicles = new ArrayList<Vehicle>();

		condition.setLength( 0 );

		// form the query based on the given Club object instance
		query.append( selectVehicleTypeSql );

		if( vehicleType != null ) {
			if( vehicleType.getId() >= 0 ) // id is unique, so it is sufficient to get a person
				query.append( " where id = " + vehicleType.getId() );
			else if( vehicleType.getName() != null ) // userName is unique, so it is sufficient to get a person
				query.append( " where name = '" + vehicleType.getName() + "'" );
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
                    nextVehcle.setStatus(null);
                    nextVehcle.setCondition(null);
                    nextVehcle.setRentalLocation(null);
                    nextVehcle.setVehicleType(null);
                    
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
	
	public List<HourlyPrice> restoreVehicleTypeHourlyPrice( VehicleType vehicleType ) throws RARException
	{
		String       selectVehicleTypeSql = "select hp.maxHours and hp.price" +
				   "  vt.typeName, vt.vehicleTypeId from HourlyPrice hp, VehicleType vt where hp.vehicleID = vt.vehicleTypeId";
		Statement    stmt = null;
		StringBuffer query = new StringBuffer( 100 );
		StringBuffer condition = new StringBuffer( 100 );
		List<HourlyPrice> hp = new ArrayList<HourlyPrice>();

		condition.setLength( 0 );

		// form the query based on the given Club object instance
		query.append( selectVehicleTypeSql );

		if( vehicleType != null ) {
			if( vehicleType.getId() >= 0 ) // id is unique, so it is sufficient to get a person
				query.append( " where id = " + vehicleType.getId() );
			else if( vehicleType.getName() != null ) // userName is unique, so it is sufficient to get a person
				query.append( " where name = '" + vehicleType.getName() + "'" );
			}

		try {

			stmt = conn.createStatement();

         // retrieve the persistent Hourly Price objects
         //
         if( stmt.execute( query.toString() ) ) { // statement returned a result
             
             long   id;
             int maxHours;
             int price;
             HourlyPrice   nextHourlyPrice = null;
             
             ResultSet rs = stmt.getResultSet();
             
             // retrieve the retrieved customers
             while( rs.next() ) {
                 
                 id = rs.getLong( 1 );
                 maxHours = rs.getInt( 2 );
                 price = rs.getInt(3);
                 
                 nextHourlyPrice = objectLayer.createHourlyPrice(); // create a proxy hourly price object
                 // and now set its retrieved attributes
                 nextHourlyPrice.setId( id );
                 nextHourlyPrice.setMaxHours(maxHours);
                 nextHourlyPrice.setPrice(price);
                 // set this to null for the "lazy" association traversal
                 //nextCustomer.setPersonFounder( null );
                 
                 hp.add( nextHourlyPrice );
             }
             
             return hp;
         }
     }
     catch( Exception e ) {      // just in case...
         throw new RARException( "VehicleTypeManager.restore: Could not restore persistent Vehicle Type objects; Root cause: " + e );
     }

     throw new RARException( "VehicleTypeManager.restore: Could not restore persistent Vehicle Type objects" );
 
 }
	
}
