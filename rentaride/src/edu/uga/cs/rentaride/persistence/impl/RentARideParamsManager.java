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

public class RentARideParamsManager {
	
	private ObjectLayer objectLayer = null;
    private Connection   conn = null;
    
    public RentARideParamsManager( Connection conn, ObjectLayer objectLayer )
    {
        this.conn = conn;
        this.objectLayer = objectLayer;
    }

    public void storeRentARideConfig( RentARideParams rentARideConfig ) throws RARException
    {
        String               insertRentalSql = "insert into rental ( id, membershipPrice, lateFee) values ( ?, ?, ?)";
        String               updateRentalSql = "update rental set id = ?, membershipPrice = ?, lateFee = ? where id = ?";
        PreparedStatement    stmt = null;
        int                  inscnt;
        long                 RARPId;
                 
        try {

            if( !rentARideConfig.isPersistent() )
                stmt = (PreparedStatement) conn.prepareStatement( insertRentalSql );
            else
                stmt = (PreparedStatement) conn.prepareStatement( updateRentalSql );

            if( rentARideConfig.getId() >=0 ) // id is unique unique and non null
                stmt.setString( 1, rentARideConfig.getId() );
            else 
                throw new RARException( "RentARideParamsManager.save: can't save a RentARideParams: id undefined" );

            if( rentARideConfig.getMembershipPrice() > 0  )
                stmt.setDate( 2, rentARideConfig.getMembershipPrice() );
            else
            	throw new RARException( "RentARideParamsManager.save: can't save a RentARideParams: membership price not set or not persistent" );

            if( rentARideConfig.getLateFee() >= 0 )
                stmt.setDate( 3, rentARideConfig.getLateFee() );
            else
            	throw new RARException( "RentARideParamsManager.save: can't save a RentARideParams: late fee not set or is not persistent" );
            
            
            if( rentARideConfig.isPersistent() )
                stmt.setLong( 4, rentARideConfig.getId() );

            inscnt = stmt.executeUpdate();

            if( !rentARideConfig.isPersistent() ) {
                if( inscnt >= 1 ) {
                    String sql = "select last_insert_id()";
                    if( stmt.execute( sql ) ) { // statement returned a result

                        // retrieve the result
                        ResultSet r = stmt.getResultSet();

                        // we will use only the first row!
                        //
                        while( r.next() ) {

                            // retrieve the last insert auto_increment value
                        	RARPId = r.getLong( 1 );
                            if( RARPId > 0 )
                            	rentARideConfig.setId( RARPId ); // set this RentARideParams's db id (proxy object)
                        }
                    }
                }
                else
                    throw new RARException( "RentARideParamsManager.save: failed to save a RentARideParams" );
            }
            else {
                if( inscnt < 1 )
                    throw new RARException( "RentARideParamsManager.save: failed to save a RentARideParams" ); 
            }
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new RARException( "RentARideParamsManager.save: failed to save a RentARideParams: " + e );
        }
    }

    public RentARideParams restoreRentARideConfig() throws RARException
    {
        String       selectRARPSql = "select from RentARideParams";
        Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );

        condition.setLength( 0 );
        
        // form the query based on the given RentARideParams object instance
        query.append( selectRARPSql );
        
        try {

        }
        catch( Exception e ) {      // just in case...
            throw new RARException( "RentalManager.restore: Could not restore persistent Rental objects; Root cause: " + e );
        }

        throw new RARException( "RentalManager.restore: Could not restore persistent Rental objects" );
    }
	
}