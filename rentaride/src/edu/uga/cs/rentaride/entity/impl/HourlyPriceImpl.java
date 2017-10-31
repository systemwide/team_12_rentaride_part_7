package edu.uga.cs.rentaride.entity.impl;
import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.*;

public class HourlyPriceImpl
	implements HourlyPrice
{

	private long		id;
	private int		maxHrs;
	private int		price;
	VehicleType		vehicleType;
	public HourlyPriceImpl()
	{
		super();
		this.id =		-1;
		this.maxHrs =	-1;
		this.price =		-1;
		vehicleType =	null;
		
	}
	
	public HourlyPriceImpl(long id, int maxHrs, int price, VehicleType vehicleType)
	{
		this.id = id;
		this.maxHrs = maxHrs;
		this.price = price;
		this.vehicleType = vehicleType;
	}
	
	@Override
	public long getId() {

		return id;
	}

	@Override
	public void setId(long id) {

		this.id = id;
	}

	@Override
	public boolean isPersistent() {

		return id != -1;
	}

	@Override
	public int getMaxHours() {

		return maxHrs;
	}

	@Override
	public void setMaxHours(int maxHours) throws RARException {

		this.maxHrs = maxHours;
	}

	@Override
	public int getPrice() {

		return price;
	}

	@Override
	public void setPrice(int price) throws RARException {

		this.price = price;
	}

	@Override
	public VehicleType getVehicleType() {

		return vehicleType;
	}

	@Override
	public void setVehicleType(VehicleType vehicleType) throws RARException {

		this.vehicleType = vehicleType;
	}
	
}