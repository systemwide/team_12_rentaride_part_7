package edu.uga.cs.rentaride.persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;
import edu.uga.cs.rentaride.entity.impl.*; 
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
	
	
}