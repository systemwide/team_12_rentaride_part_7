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
import edu.uga.cs.rentaride.entity.VehicleType;
import edu.uga.cs.rentaride.entity.impl.*;
import edu.uga.cs.rentaride.object.impl.*;

import java.sql.SQLException;



class VehicleTypeManager
{
	private Connection conn = null;
	private ObjectLayerImpl object = null;
	
	public VehicleTypeManager( Connection conn, ObjectLayerImpl object)
	{
		this.conn = conn;
		this.object = object;
	}
	
	public void save(VehicleType vehicleType) throws RARException
	{
		String vehicleTypeInsert = 		"INSERT INTO VehicleType (name) VALUES (?)";
		String vehicleTypeUpdate = 		"UPDATE VehicleType SET name = ? where id = ?";
		PreparedStatement stmt = 		null;
		int inscnt;
		long vehicleTypeID;
		
		try
		{
			if(!vehicleType.isPersistent())
				stmt = (PreparedStatement) conn.prepareStatement( vehicleTypeInsert );
			else
				stmt = (PreparedStatement) conn.prepareStatement( vehicleTypeUpdate );
		}// end try
		catch(SQLException e) {
			e.printStackTrace();
			throw new RARException("VehicleTypeManager.save failed to save a vehicle type" + e);
		}//end catch
		
		
	}// end save method
}// end class VehicleTypeManager