package edu.uga.cs.rentaride.logic.impl;

import java.util.List;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.RentalLocation;
import edu.uga.cs.rentaride.object.ObjectLayer;

public class FindAllLocationsCtrl{
    private ObjectLayer objectLayer = null;

    public FindAllLocationsCtrl(ObjectLayer objectModel){
    	this.objectLayer = objectModel;
    }//FindAllLocationsCtrl

    public List<RentalLocation> findAllLocations() throws RARException{
    	List<RentalLocation> locations = null;

    	locations = objectLayer.findRentalLocation(null);

    	return locations;
    }//findAllLocations
}//FindAllLocationCtrl