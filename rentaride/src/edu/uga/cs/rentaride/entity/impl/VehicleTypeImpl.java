package edu.uga.cs.rentaride.entity.impl;

import java.util.List;
import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.*;
import edu.uga.cs.rentaride.persistence.Persistable;

/** This class represents possible vehicle types of all vehicles in the Rent-A-Ride system.
 * The types should be similar to sedan, pickup, luxury sedan, etc.
 *
 */
public class VehicleTypeImpl
	implements VehicleType
{
	private long 				id;
	private String 				name;
	private List<HourlyPrice> 	hourlyPriceList;
	private 	List<Vehicle> 		vehicleList;
	private	List<Reservation> 	reservationList;
	
	public VehicleTypeImpl()
	{
		this.id = -1;
		this.name = "";
		this.hourlyPriceList = null;
		this.vehicleList = null;
		this.reservationList = null;
	}
	
	public VehicleTypeImpl(long id, String name, List<HourlyPrice> hourlyPrice, List<Vehicle> vehicle, List<Reservation> reservation)
	{
		this.name = name;
		this.hourlyPriceList = hourlyPrice;
		this.vehicleList = vehicle;
		this.reservationList = reservation;
	}
	
    /** Return the name of this vehicle type.
     * @return name of this vehicle type
     */
    public String getName()
    {	
    		return name;
    }
    
    /** Set the name of this vehicle type.
     * @param name the new name of this vehicle type
     * @throws RARException in case name is non-unique or null
     */
    public void setName( String name ) throws RARException
    {
    		this.name = name;
    }
    
    /** Return a list of all hourly prices for this VehicleType.
     * @return a list of all hourly prices made by this VehicleType
     */
    public List<HourlyPrice> getHourlyPrices()
    {
    		return hourlyPriceList;
    }
    
    /** Return a list of all vehicles of this VehicleType.
     * @return a list of all vehicles of this VehicleType
     */
    public List<Vehicle> getVehicles()
    {
    		return vehicleList;
    }
    
    /** Return a list of all reservations for this VehicleType.
     * @return a list of all reservations for this VehicleType
     */
    public List<Reservation> getReservations()
    {
    		return reservationList;
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
}