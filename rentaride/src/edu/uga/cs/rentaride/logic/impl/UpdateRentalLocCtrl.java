package edu.uga.cs.rentaride.logic.impl;

import java.util.Date;
import java.util.List;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.RentalLocation;
import edu.uga.cs.rentaride.entity.impl.RentalLocationImpl;
import edu.uga.cs.rentaride.object.ObjectLayer;

public class UpdateRentalLocCtrl {
	
	private ObjectLayer objectLayer = null;
	
	public UpdateRentalLocCtrl(ObjectLayer objectModel){
		this.objectLayer = objectModel;
	}//FindAllLocationsCtrl

	public void updateRentalLocation(long id, String name, String address, int capacity) throws RARException{
		RentalLocation newRentalLoc = new RentalLocationImpl(id, name, address, capacity);
		objectLayer.storeRentalLocation(newRentalLoc);
	}//findAllLocations

} //CreateRentalLocCtrl