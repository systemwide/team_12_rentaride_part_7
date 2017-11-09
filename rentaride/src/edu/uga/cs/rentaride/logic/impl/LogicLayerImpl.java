package edu.uga.cs.rentaride.logic.impl;

import java.sql.Connection;
import java.util.List;

import edu.uga.cs.rentaride.logic.LogicLayer;
import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.RentalLocation;
import edu.uga.cs.rentaride.object.ObjectLayer;
import edu.uga.cs.rentaride.object.impl.ObjectLayerImpl;
import edu.uga.cs.rentaride.persistence.PersistenceLayer;
import edu.uga.cs.rentaride.persistence.impl.PersistenceLayerImpl;
import edu.uga.cs.rentaride.session.Session;
import edu.uga.cs.rentaride.session.SessionManager;

public class LogicLayerImpl implements LogicLayer{
	private ObjectLayer objectLayer = null;

	public LogicLayerImpl(Connection conn){
		objectLayer = new ObjectLayerImpl();
		PersistenceLayer persistenceLayer = new PersistenceLayerImpl(conn, objectLayer);
		objectLayer.setPersistence(persistenceLayer);
		System.out.println("LogicLayerImpl.LogicLayerImpl(conn): initialized");
	}//LogicLayerImpl

	public LogicLayerImpl(ObjectLayer objectLayer){
		this.objectLayer = objectLayer;
		System.out.println("LogicLayerImpl.LogicLayerImpl(objectLayer):initialized");
	}//LogicLayerImol

	public List<RentalLocation> findAllLocations() throws RARException{
		FindAllLocationsCtrl ctrlFindAllLocations = new FindAllLocationsCtrl(objectLayer);
		return ctrlFindAllLocations.findAllLocations();
	}//findAllLocations
}//LogicLayerImpl