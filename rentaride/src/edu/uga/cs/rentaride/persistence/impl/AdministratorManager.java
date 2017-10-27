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

public class AdministratorManager {
	private ObjectLayer objectLayer = null;
	private Connection conn = null;
	
	public AdministratorManager(Connection conn, ObjectLayer objectLayer){
		this.conn = conn;
		this.objectLayer = objectLayer;
	}
	
	public void store(Administrator admin)
		throws RARException
	{
		String insertAdminSql = "insert into Administrator ( firstname, lastname, username, password, email, address, created, userStatus) values ( ?, ?, ?, ?, ?, ?, ?, ? )";
		String updateAdminSql = "update Administrator set firstname = ?, lastname = ?, username = ?, password = ?, email = ?, address = ?, created = ?, userStatus = ? where id = ?";
		PreparedStatement stmt = null;
		int inscnt;
		long adminId;
		
		try {

            if( !admin.isPersistent() )
                stmt = (PreparedStatement) conn.prepareStatement( insertAdminSql );
            else
                stmt = (PreparedStatement) conn.prepareStatement( updateAdminSql );
		
            if( admin.getFirstName() != null ) // name is unique unique and non null
                stmt.setString( 1, admin.getFirstName() );
            else 
                throw new RARException( "AdministratorManager.save: can't save an Administrator: first name undefined" );

            if( admin.getLastName() != null ) // name is unique unique and non null
                stmt.setString( 2, admin.getLastName() );
            else 
                throw new RARException( "AdministratorManager.save: can't save an Administrator: last name undefined" );
            
            if( admin.getUserName()!= null ) // name is unique unique and non null
                stmt.setString( 3, admin.getUserName() );
            else 
                throw new RARException( "AdministratorManager.save: can't save an Administrator: username undefined" );
            
            //password
            if( admin.getPassword() != null )
                stmt.setString( 4, admin.getPassword() );
            else
            	throw new RARException( "AdministratorManager.save: can't save an Administrator: password undefined" );
            
            //email
            if( admin.getEmail() != null )
                stmt.setString( 5, admin.getEmail() );
            else
            	throw new RARException( "AdministratorManager.save: can't save an Administrator: email undefined" );
            
            //address - 5
            if( admin.getAddress() != null )
                stmt.setString( 6, admin.getAddress() );
            else
                stmt.setNull( 6, java.sql.Types.VARCHAR );
            
            //created
            if( admin.getCreatedDate() != null ) {
                java.util.Date jDate = admin.getCreatedDate();
                java.sql.Date sDate = new java.sql.Date( jDate.getTime() );
                stmt.setDate( 7,  sDate );
            }
            else
                stmt.setNull(7, java.sql.Types.DATE);
            
            if( admin.getUserStatus() != null )
                stmt.setString( 8, admin.getUserStatus() );
            else
                stmt.setNull( 8, java.sql.Types.VARCHAR );
            
            if( admin.isPersistent() )
                stmt.setLong( 9, admin.getId() );

            inscnt = stmt.executeUpdate();

            if( !admin.isPersistent() ) {
                if( inscnt >= 1 ) {
                    String sql = "select last_insert_id()";
                    if( stmt.execute( sql ) ) { // statement returned a result

                        // retrieve the result
                        ResultSet r = stmt.getResultSet();

                        // we will use only the first row!
                        //
                        while( r.next() ) {

                            // retrieve the last insert auto_increment value
                            adminId = r.getLong( 1 );
                            if( adminId > 0 )
                                admin.setId( adminId ); // set this Administrator's db id (proxy object)
                        }
                    }
                }
                else
                    throw new RARException( "AdministratorManager.save: failed to save an Administrator" );
            }
            else {
                if( inscnt < 1 )
                    throw new RARException( "AdministratorManager.save: failed to save an Administrator" ); 
            }
		
		}catch( SQLException e ) {
            e.printStackTrace();
            throw new RARException( "AdministratorManager.save: failed to save an Administrator: " + e );
        }
		
	}
	public List<Administrator> restore( Administrator modelAdmin ) 
            throws RARException
    {
        String       selectAdministratorSql = "select id, firstname, lastname, username, password, email, address, created, userStatus from Administrator";
        Statement    stmt = null;
        StringBuffer query = new StringBuffer( 100 );
        StringBuffer condition = new StringBuffer( 100 );
        List<Administrator>  administrator = new ArrayList<Administrator>();

        condition.setLength( 0 );
        
        // form the query based on the given Club object instance
        query.append( selectAdministratorSql );
        
        if( modelAdmin != null ) {
            if( modelAdmin.getId() >= 0 ) // id is unique, so it is sufficient to get a person
                query.append( " where id = " + modelAdmin.getId() );
            else if( modelAdmin.getUserName() != null ) // userName is unique, so it is sufficient to get a person
                query.append( " where username = '" + modelAdmin.getUserName() + "'" );
            else {

            	if( modelAdmin.getFirstName() != null )
                    condition.append( " where first name = '" + modelAdmin.getFirstName() + "'" ); 
            	
            	if( modelAdmin.getLastName() != null )
            		if(condition.length() > 0 )
    					condition.append(" and ");
                    condition.append( " where last name = '" + modelAdmin.getLastName() + "'" ); 
            	
            	if( modelAdmin.getPassword() != null )
            		if(condition.length() > 0 )
    					condition.append(" and ");
                    condition.append( " where password = '" + modelAdmin.getPassword() + "'" ); 
            	
            	if( modelAdmin.getEmail() != null )
            		if(condition.length() > 0 )
    					condition.append(" and ");
                    condition.append( " where email = '" + modelAdmin.getEmail() + "'" ); 
            	
                if( modelAdmin.getAddress() != null )
                	if(condition.length() > 0 )
    					condition.append(" and ");
                    condition.append( " where address = '" + modelAdmin.getAddress() + "'" );   

                if( modelAdmin.getCreatedDate() != null ) {
                    if( condition.length() > 0 )
                        condition.append( " and" );
                    else
                        condition.append( " where" );
                    condition.append( " created = '" + modelAdmin.getCreatedDate() + "'" );
                }
                
                if( modelAdmin.getUserStatus() != null )
                	if(condition.length() > 0 )
    					condition.append(" and ");
                    condition.append( " where user status = '" + modelAdmin.getUserStatus() + "'" ); 

            }
        }
        
        try {

            stmt = conn.createStatement();

            // retrieve the persistent Club objects
            //
            if( stmt.execute( query.toString() ) ) { // statement returned a result
                
                long   id;
                String firstName;
                String lastName;
                String userName;
                String password;
                String email;
                String address;
                Date   created;
                String userStatus;
                Administrator   nextAdmin = null;
                
                ResultSet rs = stmt.getResultSet();
                
                // retrieve the retrieved clubs
                while( rs.next() ) {
                    
                    id = rs.getLong( 1 );
                    firstName = rs.getString( 2 );
                    lastName = rs.getString(3);
                    userName = rs.getString(4);
                    password = rs.getString(5);
                    email = rs.getString( 6 );
                    address = rs.getString(7);
                    created = rs.getDate( 8 );
                    userStatus = rs.getString(9);
                    
                    nextAdmin = objectLayer.createAdministrator(); // create a proxy Administrator object
                    // and now set its retrieved attributes
                    nextAdmin.setId( id );
                    nextAdmin.setFirstName( firstName );
                    nextAdmin.setLastName(lastName);
                    nextAdmin.setEmail(email);
                    nextAdmin.setAddress( address );
                    nextAdmin.setCreateDate( created );
                    nextAdmin.setUserStatus(null); // set this to null for the "lazy" association traversal
                   // nextAdmin.setPersonFounder( null );
                    
                    administrator.add( nextAdmin );
                }
                
                return administrator;
            }
        }
        catch( Exception e ) {      // just in case...
            throw new RARException( "AdministratorManager.restore: Could not restore persistent Administrator objects; Root cause: " + e );
        }

        throw new RARException( "AdministratorManager.restore: Could not restore persistent Administrator objects" );
    }

	public void deleteAdministrator( Administrator administrator ) throws RARException
	{
        String               deleteAdministratorSql = "delete from hourly price where id = ?";              
        PreparedStatement    stmt = null;
        int                  inscnt;
             
        if( !administrator.isPersistent() ) // is the Administrator object persistent?  If not, nothing to actually delete
            return;
        
        try {
            stmt = (PreparedStatement) conn.prepareStatement( deleteAdministratorSql );         
            stmt.setLong( 1, administrator.getId() );
            inscnt = stmt.executeUpdate();          
            if( inscnt == 1 ) {
                return;
            }
            else
                throw new RARException( "AdministratorManager.delete: failed to delete a Administrator" );
        }
        catch( SQLException e ) {
            e.printStackTrace();
            throw new RARException( "AdministratorManager.delete: failed to delete a Administrator: " + e );        }
    }
}