package edu.uga.cs.rentaride.entity.impl;
import java.util.Date;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.*;


public class RentalImpl
	implements Rental
{
	private long 			id;
	private Date 			pickupTime;
	private Date				returnTime;
	private Boolean			isLate;
	private int				charges;
	private Vehicle			vehicle;
	private Customer			customer;
	private Reservation		reservation;
	private Comment			comment;
	
	
	public RentalImpl()
	{
		super();
		this.id =			-1;
		this.pickupTime = 	null;
		this.returnTime = 	null;
		this.isLate = 		null;
		this.charges = 		-1;
		this.vehicle =		null;
		this.customer = 		null;
		this.reservation =	null;
		this.comment =		null;
	}// end constructor 1
	
	
	public RentalImpl(long id, Date pickupTime, Date returnTime, Boolean isLate, int charges, Vehicle vehicle,
			Customer customer, Reservation reservation, Comment comment)
	{
		super();
		this.id =			id;
		this.pickupTime = 	pickupTime;
		this.returnTime = 	returnTime;
		this.isLate = 		isLate;
		this.charges = 		charges;
		this.vehicle =		vehicle;
		this.customer = 		customer;
		this.reservation =	reservation;	
		this.comment = 		comment;
	}// end constructor 2
	
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
	public Date getPickupTime() {

		return pickupTime;
	}

	@Override
	public void setPickupTime(Date pickupTime) {

		this.pickupTime = pickupTime;
	}

	@Override
	public Date getReturnTime() {

		return returnTime;
	}

	@Override
	public void setReturnTime(Date returnTime) throws RARException {
		// TODO Handle some exception ?
		this.returnTime = returnTime;
	}

	@Override
	public boolean getLate() {

		return isLate;
	}

	@Override
	public int getCharges() {

		return charges;
	}

	@Override
	public void setCharges(int charges) throws RARException {

		this.charges = charges;
	}

	@Override
	public Reservation getReservation() {

		return reservation;
	}

	@Override
	public void setReservation(Reservation reservation) throws RARException {

		this.reservation = reservation;
	}

	@Override
	public Vehicle getVehicle() {

		return vehicle;
	}

	@Override
	public void setVehicle(Vehicle vehicle) throws RARException {

		this.vehicle = vehicle;
	}

	@Override
	public Customer getCustomer() {

		return customer;
	}

	@Override
	public Comment getComment() {

		return comment;
	}

	@Override
	public void setComment(Comment comment) {

		this.comment = comment;
	}


	@Override
	public void setCustomer(Customer customer) {
		// TODO Auto-generated method stub
		this.customer = customer;
	}
	
}// end Class RentalImpl