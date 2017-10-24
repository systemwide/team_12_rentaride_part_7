package edu.uga.cs.rentaride.entity.impl;

import java.util.Date;
import java.util.List;

import edu.uga.cs.rentaride.entity.*;
import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.persistence.Persistable;



/** This class represents a vehicle maintained by the Rent-A-Ride system.  In addition to
 * several vehicle attributes, each vehicle
 * has a vehicle type and is assigned to a rental location.  
 *
 */
public class VehicleImpl
	implements Vehicle
{
	private long 					id;
	private	String 					make;
	private String 					model;
	private int 						year;
	private int 						mileage;
	private String 					registrationTag;
	private Date 					lastServiced;
	private VehicleStatus 			status;
	private VehicleCondition 		condition;
	private RentalLocation 			rentalLocation;
	private VehicleType 				vehicleType;
	
	public VehicleImpl()
	{
		super();
		this.id = 					-1;
		this.make =					null;
		this.model =					null;
		this.year =					-1;
		this.mileage = 				-1;
		this.registrationTag = 		null;
		this.lastServiced =			null;
		this.status =				null;
		this.condition = 			null;
		this.rentalLocation =		null;
		this.vehicleType = 			null;
	}
	
	public VehicleImpl(long id, String make, String model, int year, int mileage, String registrationTag,
			Date lastServiced, VehicleStatus status, VehicleCondition condition, RentalLocation rentalLocation,
			VehicleType vehicleType)
	{
		super();
		this.id = 					id;
		this.make =					make;
		this.model =					model;
		this.year =					year;
		this.mileage = 				mileage;
		this.registrationTag = 		registrationTag;
		this.lastServiced =			lastServiced;
		this.status =				status;
		this.condition = 			condition;
		this.rentalLocation =		rentalLocation;
		this.vehicleType = 			vehicleType;
	}
	
	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return id;
	}
	@Override
	public void setId(long id) {
		// TODO Auto-generated method stub
		this.id = id;
	}
	@Override
	public boolean isPersistent() {
		// TODO Auto-generated method stub
		return id != -1;
	}
	@Override
	public String getMake() {
		// TODO Auto-generated method stub
		return make;
	}
	@Override
	public void setMake(String make) {
		// TODO Auto-generated method stub
		this.make = make;
	}
	@Override
	public String getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	@Override
	public void setModel(String model) {
		// TODO Auto-generated method stub
		this.model = model;
	}
	@Override
	public int getYear() {
		// TODO Auto-generated method stub
		return year;
	}
	@Override
	public void setYear(int year) throws RARException {
		// TODO Auto-generated method stub
		this.year = year;
	}
	@Override
	public String getRegistrationTag() {
		// TODO Auto-generated method stub
		return registrationTag;
	}
	@Override
	public void setRegistrationTag(String registrationTag) {
		// TODO Auto-generated method stub
		this.registrationTag = registrationTag
	}
	@Override
	public int getMileage() {
		// TODO Auto-generated method stub
		return mileage;
	}
	@Override
	public void setMileage(int mileage) throws RARException {
		// TODO Auto-generated method stub
		this.mileage = mileage;
	}
	@Override
	public Date getLastServiced() {
		// TODO Auto-generated method stub
		return lastServiced;
	}
	@Override
	public void setLastServiced(Date lastServiced) {
		// TODO Auto-generated method stub
		this.lastServiced = lastServiced;
	}
	@Override
	public VehicleStatus getStatus() {
		// TODO Auto-generated method stub
		return status;
	}
	@Override
	public void setStatus(VehicleStatus status) {
		// TODO Auto-generated method stub
		this.status = status;
	}
	@Override
	public VehicleCondition getCondition() {
		// TODO Auto-generated method stub
		return condition;
	}
	@Override
	public void setCondition(VehicleCondition condition) {
		// TODO Auto-generated method stub
		this.condition = condition;
	}
	@Override
	public VehicleType getVehicleType() {
		// TODO Auto-generated method stub
		return vehicleType;
	}
	@Override
	public void setVehicleType(VehicleType vehicleType) {
		// TODO Auto-generated method stub
		this.vehicleType = vehicleType;
	}
	@Override
	public RentalLocation getRentalLocation() {
		// TODO Auto-generated method stub
		return rentalLocation;
	}
	@Override
	public void setRentalLocation(RentalLocation rentalLocation) throws RARException {
		// TODO Auto-generated method stub
		this.rentalLocation = rentalLocation;
	}
	@Override
	public List<Rental> getRentals() {
		// TODO Auto-generated method stub
		return null;
	}
}