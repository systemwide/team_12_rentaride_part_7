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
	
	
	public RentalImpl(long id, Date pickupDate, Date returnDate, Boolean isLate, int charges, Vehicle vehicle,
			Customer customer, Reservation reservation, Comment comment)
	{
		super();
		this.id =			id;
		this.pickupTime = 	pickupDate;
		this.returnTime = 	returnDate;
		this.isLate = 		isLate;
		this.charges = 		charges;
		this.vehicle =		vehicle;
		this.customer = 		customer;
		this.reservation =	reservation;	
		this.comment = 		comment;
	}// end constructor 2
	
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
	public Date getPickupTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPickupTime(Date pickupTime) {
		// TODO Auto-generated method stub
		this.pickupTime = pickupTime;
	}

	@Override
	public Date getReturnTime() {
		// TODO Auto-generated method stub
		return returnTime;
	}

	@Override
	public void setReturnTime(Date returnTime) throws RARException {
		// TODO Handle some exception ?
		this.returnTime = returnTime;
	}

	@Override
	public boolean getLate() {
		// TODO Auto-generated method stub
		return isLate;
	}

	@Override
	public int getCharges() {
		// TODO Auto-generated method stub
		return charges;
	}

	@Override
	public void setCharges(int charges) throws RARException {
		// TODO Auto-generated method stub
		this.charges = charges;
	}

	@Override
	public Reservation getReservation() {
		// TODO Auto-generated method stub
		return reservation;
	}

	@Override
	public void setReservation(Reservation reservation) throws RARException {
		// TODO Auto-generated method stub
		this.reservation = reservation;
	}

	@Override
	public Vehicle getVehicle() {
		// TODO Auto-generated method stub
		return vehicle;
	}

	@Override
	public void setVehicle(Vehicle vehicle) throws RARException {
		// TODO Auto-generated method stub
		this.vehicle = vehicle;
	}

	@Override
	public Customer getCustomer() {
		// TODO Auto-generated method stub
		return customer;
	}

	@Override
	public Comment getComment() {
		// TODO Auto-generated method stub
		return comment;
	}

	@Override
	public void setComment(Comment comment) {
		// TODO Auto-generated method stub
		this.comment = comment;
	}
	
}// end Class RentalImpl