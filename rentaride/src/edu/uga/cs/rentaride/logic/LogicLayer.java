package edu.uga.cs.rentaride.logic;

import java.util.Date;
import java.util.List;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.Customer;
import edu.uga.cs.rentaride.entity.RentalLocation;
import edu.uga.cs.rentaride.entity.Reservation;
import edu.uga.cs.rentaride.entity.VehicleCondition;
import edu.uga.cs.rentaride.entity.VehicleStatus;
import edu.uga.cs.rentaride.entity.VehicleType;

public interface LogicLayer{
    public List<RentalLocation> findAllLocations() throws RARException;
    public long createRentalLocation(String name, String address, int capacity) throws RARException;
    public void updateRentalLocation(long id, String name, String address, int capacity) throws RARException;
    public void deleteRentalLocation(long id, String name, String address, int capacity) throws RARException;
    public long createVehicle(long id, String make, String model, int year, int mileage, String registrationTag,
			Date lastServiced, VehicleStatus status, VehicleCondition condition, RentalLocation rentalLocation,
			VehicleType vehicleType) throws RARException;
	public List<Customer> browseCustomers();
	public List<Reservation> findAllReservations(int id);
	public List<Reservation> findAllReservations();
	
	
}//LogicLayer