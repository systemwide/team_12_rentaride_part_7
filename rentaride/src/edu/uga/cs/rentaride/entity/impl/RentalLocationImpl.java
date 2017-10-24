package edu.uga.cs.rentaride.entity.impl;
import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.RentalLocation;

public class RentalLocationImpl
	implements RentalLocation
{
	private long 		id;
	private String 		name;
	private String 		address;
	private int			capacity;	
	
	
	public RentalLocationImpl()
	{
		super();
		this.id =		-1;
		this.name = 		null;
		this.address = 	null;
		this.capacity =	-1;
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
	public String getName() {

		return name;
	}

	@Override
	public void setName(String name) throws RARException {

		this.name = name;
	}

	@Override
	public String getAddress() {

		return address;
	}

	@Override
	public void setAddress(String address) {

		this.address = address;
	}

	@Override
	public int getCapacity() {

		return capacity;
	}

	@Override
	public void setCapacity(int capacity) throws RARException {

		this.capacity = capacity;
	}
	
}