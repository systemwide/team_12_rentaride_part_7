import java.util.Date;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.*;

public class ReservationImpl
	implements Reservation
{
	private long				id;
	private Date				pickup;
	private int				length;
	private Boolean			cancelled;
	private Customer			customer;
	private VehicleType		vehicleType;
	private RentalLocation	rentalLocation;
	private Rental			rental;
	
	public ReservationImpl()
	{
		super();
		this.id = 				-1;
		this.pickup = 			null;
		this.length =			-1;
		this.cancelled = 		null;
		this.customer = 			null;
		this.vehicleType = 		null;
		this.rentalLocation = 	null;
		this.rental =			null;
	}// end constructor 1
	
	public ReservationImpl(long id, Date pickup, int length, Boolean cancelled, Customer customer,
			VehicleType vehicleType, RentalLocation rentalLocation, Rental rental)
	{
		super();
		this.id = 				id;
		this.pickup = 			pickup;
		this.length =			length;
		this.cancelled = 		cancelled;
		this.customer = 			customer;
		this.vehicleType = 		vehicleType;
		this.rentalLocation = 	rentalLocation;
		this.rental =			rental;
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

		return pickup;
	}

	@Override
	public void setPickupTime(Date pickupTime) throws RARException {

		this.pickup = pickupTime;
	}

	@Override
	public int getLength() {

		return length;
	}

	@Override
	public void setLength(int length) throws RARException {

		this.length = length;
	}

	@Override
	public Customer getCustomer() {

		return customer;
	}

	@Override
	public void setCustomer(Customer customer) throws RARException {

		this.customer = customer;
	}

	@Override
	public VehicleType getVehicleType() {

		return vehicleType;
	}

	@Override
	public void setVehicleType(VehicleType vehicleType) throws RARException {

		this.vehicleType = vehicleType;
	}

	@Override
	public RentalLocation getRentalLocation() {

		return rentalLocation;
	}

	@Override
	public void setRentalLocation(RentalLocation rentalLocation) throws RARException {

		this.rentalLocation = rentalLocation;
	}

	@Override
	public Rental getRental() {

		return rental;
	}

	@Override
	public void setRental(Rental rental) {

		this.rental = rental;
	}
	
}// end ReservationImpl class