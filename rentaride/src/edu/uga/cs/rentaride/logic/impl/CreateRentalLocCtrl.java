package edu.uga.cs.rentaride.logic.impl;

import java.util.Date;
import java.util.List;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.RentalLocation;
import edu.uga.cs.rentaride.object.ObjectLayer;

public class CreateRentalLocCtrl {
	
	private ObjectLayer objectLayer = null;
	
	public CreateRentalLocCtrl(ObjectLayer objectModel){
		this.objectLayer = objectModel;
	}//FindAllLocationsCtrl

	public long createRentalLocation(String name, String address, int capacity) throws RARException{
		RentalLocation newRentalLoc;
		newRentalLoc = objectLayer.createRentalLocation(name, address, capacity);

		return newRentalLoc.getId();
	}//findAllLocations

} //CreateRentalLocCtrl