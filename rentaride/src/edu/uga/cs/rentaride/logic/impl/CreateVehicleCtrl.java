package edu.uga.cs.rentaride.logic.impl;

import java.util.Date;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.RentalLocation;
import edu.uga.cs.rentaride.entity.Vehicle;
import edu.uga.cs.rentaride.entity.VehicleCondition;
import edu.uga.cs.rentaride.entity.VehicleStatus;
import edu.uga.cs.rentaride.entity.VehicleType;
import edu.uga.cs.rentaride.object.ObjectLayer;

public class CreateVehicleCtrl {
	
	private ObjectLayer obj = null;
	
	public CreateVehicleCtrl(ObjectLayer obj)
	{
		this.obj = obj;
	}
	
	public long createVehicle(long id, String make, String model, int year, int mileage, String registrationTag,
			Date lastServiced, VehicleStatus status, VehicleCondition condition, RentalLocation rentalLocation,
			VehicleType vehicleType) throws RARException
	{
		Vehicle newVehicle = null;
		try {
			newVehicle = obj.createVehicle(make, model, year, registrationTag, mileage, lastServiced, vehicleType, 
					rentalLocation, condition, status);
		} catch (RARException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newVehicle.getId();
	}
}
