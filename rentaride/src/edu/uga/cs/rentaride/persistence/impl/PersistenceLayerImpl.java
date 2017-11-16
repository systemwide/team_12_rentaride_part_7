package edu.uga.cs.rentaride.persistence.impl;

import java.sql.Connection;
import java.util.List;

import edu.uga.cs.rentaride.persistence.impl.*;
import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.Administrator;
import edu.uga.cs.rentaride.entity.Comment;
import edu.uga.cs.rentaride.entity.Customer;
import edu.uga.cs.rentaride.entity.HourlyPrice;
import edu.uga.cs.rentaride.entity.RentARideParams;
import edu.uga.cs.rentaride.entity.Rental;
import edu.uga.cs.rentaride.entity.RentalLocation;
import edu.uga.cs.rentaride.entity.Reservation;
import edu.uga.cs.rentaride.entity.Vehicle;
import edu.uga.cs.rentaride.entity.VehicleType;
import edu.uga.cs.rentaride.object.ObjectLayer;
import edu.uga.cs.rentaride.persistence.PersistenceLayer;

public class PersistenceLayerImpl
	implements PersistenceLayer
{
	
	private AdministratorManager administratorManager = null;
    private CommentManager commentManager = null;
    private CustomerManager customerManager = null;
    private HourlyPriceManager hourlyPriceManager = null;
    private VehicleTypeManager vehicleTypeManager = null;
    private VehicleManager vehicleManager = null;
    private ReservationManager reservationManager = null;
    private RentalLocationManager rentalLocationManager = null;
    private RentalManager rentalManager = null;
    
    public PersistenceLayerImpl( Connection conn, ObjectLayer objLayer )
    {
    		administratorManager = new AdministratorManager (conn, objLayer);
    		commentManager = new CommentManager (conn, objLayer);
    		customerManager = new CustomerManager( conn, objLayer);
    		hourlyPriceManager = new HourlyPriceManager(conn, objLayer);
    		vehicleTypeManager = new VehicleTypeManager(conn, objLayer);
    		vehicleManager = new VehicleManager(conn, objLayer);
    		reservationManager = new ReservationManager(conn, objLayer);
    		rentalLocationManager = new RentalLocationManager(conn, objLayer);
    		rentalManager = new RentalManager(conn, objLayer);
    		
    }
     

	@Override
	public List<Administrator> restoreAdministrator(Administrator modelAdministrator) throws RARException {
		// TODO Auto-generated method stub
		return administratorManager.restore( modelAdministrator );
	}

	@Override
	public void storeAdministrator(Administrator administrator) throws RARException {
		// TODO Auto-generated method stub
		administratorManager.store(administrator);
	}

	@Override
	public void deleteAdministrator(Administrator administrator) throws RARException {
		// TODO Auto-generated method stub
		administratorManager.deleteAdministrator(administrator);
	}

	@Override
	public List<Customer> restoreCustomer(Customer modelCustomer) throws RARException {
		// TODO Auto-generated method stub
		return customerManager.restoreCustomer(modelCustomer);
	}

	@Override
	public void storeCustomer(Customer customer) throws RARException {
		// TODO Auto-generated method stub
		customerManager.store(customer);
	}

	@Override
	public List<RentalLocation> restoreRentalLocation(RentalLocation modelRentalLocation) throws RARException {
		// TODO Auto-generated method stub
		return rentalLocationManager.restoreRentalLocation(modelRentalLocation);
	}

	@Override
	public void storeRentalLocation(RentalLocation rentalLocation) throws RARException {
		// TODO Auto-generated method stub
		rentalLocationManager.store(rentalLocation);
	}

	@Override
	public void deleteRentalLocation(RentalLocation rentalLocation) throws RARException {
		// TODO Auto-generated method stub
		rentalLocationManager.delete(rentalLocation);
	}

	@Override
	public List<Reservation> restoreReservation(Reservation modelReservation) throws RARException {
		// TODO Auto-generated method stub
		return reservationManager.restoreReservation(modelReservation);
	}

	@Override
	public void storeReservation(Reservation reservation) throws RARException {
		// TODO Auto-generated method stub
		reservationManager.storeReservation(reservation);	
	}

	@Override
	public void deleteReservation(Reservation reservation) throws RARException {
		// TODO Auto-generated method stub
		reservationManager.deleteReservation(reservation);
	}

	@Override
	public List<Rental> restoreRental(Rental modelRental) throws RARException {
		// TODO Auto-generated method stub
		return rentalManager.restore(modelRental);
	}

	@Override
	public void storeRental(Rental rental) throws RARException {
		// TODO Auto-generated method stub
		rentalManager.store(rental);
	}

	@Override
	public void deleteRental(Rental rental) throws RARException {
		// TODO Auto-generated method stub
		rentalManager.delete(rental);
		
	}

	@Override
	public List<VehicleType> restoreVehicleType(VehicleType modelVehicleType) throws RARException {
		// TODO Auto-generated method stub
		return vehicleTypeManager.restoreVehicleType(modelVehicleType);
	}

	@Override
	public void storeVehicleType(VehicleType vehicleType) throws RARException {
		// TODO Auto-generated method stub
		vehicleTypeManager.storeVehicleType(vehicleType);
	}

	@Override
	public void deleteVehicleType(VehicleType vehicleType) throws RARException {
		// TODO Auto-generated method stub
		vehicleTypeManager.deleteVehicleType(vehicleType);
	}

	@Override
	public List<Vehicle> restoreVehicle(Vehicle modelVehicle) throws RARException {
		// TODO Auto-generated method stub
		return vehicleManager.restoreVehicle(modelVehicle);
	}

	@Override
	public void storeVehicle(Vehicle vehicle) throws RARException {
		// TODO Auto-generated method stub
		vehicleManager.storeVehicle(vehicle);
		
	}

	@Override
	public void deleteVehicle(Vehicle vehicle) throws RARException {
		// TODO Auto-generated method stub
		vehicleManager.deleteVehicle(vehicle);
	}

	@Override
	public List<Comment> restoreComment(Comment modelComment) throws RARException {
		// TODO Auto-generated method stub
		return commentManager.restore(modelComment);
	}

	@Override
	public void storeComment(Comment comment) throws RARException {
		// TODO Auto-generated method stub
		commentManager.store(comment);
	}

	@Override
	public void deleteComment(Comment comment) throws RARException {
		// TODO Auto-generated method stub
		commentManager.deleteComment(comment);
	}

	@Override
	public List<HourlyPrice> restoreHourlyPrice(HourlyPrice modelHourlyPrice) throws RARException {
		// TODO Auto-generated method stub
		return hourlyPriceManager.restore(modelHourlyPrice);
	}

	@Override
	public void storeHourlyPrice(HourlyPrice hourlyPrice) throws RARException {
		// TODO Auto-generated method stub
		hourlyPriceManager.store(hourlyPrice);
	}

	@Override
	public void deleteHourlyPrice(HourlyPrice hourlyPrice) throws RARException {
		// TODO Auto-generated method stub
		hourlyPriceManager.delete(hourlyPrice);
	}

	@Override
	public RentARideParams restoreRentARideConfig() throws RARException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storeRentARideConfig(RentARideParams rentARideConfig) throws RARException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void storeCustomerReservation(Customer customer, Reservation reservation) throws RARException {
		// TODO Auto-generated method stub
		if(customer == null)
		{
			throw new RARException ("Invalid - null customer");
		}
		if(!customer.isPersistent())
		{
			throw new RARException ("Invalid - non-persistent");
		}
		
		Rental rental = reservation.getRental();
		reservation.setCustomer(customer);
		rental.setCustomer(customer);
		rentalManager.store(rental);
		reservationManager.storeReservation(reservation);
		
	}

	@Override
	public Customer restoreCustomerReservation(Reservation reservation) throws RARException {
		// TODO Auto-generated method stub
		return reservationManager.restoreCustomerReservation(reservation);
	}

	@Override
	public List<Reservation> restoreCustomerReservation(Customer customer) throws RARException {
		// TODO Auto-generated method stub
		
		
		List<Reservation> resList = customer.getReservations();
		return resList;
	}

	@Override
	public void deleteCustomerReservation(Customer customer, Reservation reservation) throws RARException {
		// TODO Auto-generated method stub
		
		Rental rental = reservation.getRental();
		rental.setCustomer(null);
		reservation.setCustomer(null);
		rentalManager.store(rental);
		reservationManager.deleteReservation(reservation);
		
	}

	@Override
	public void storeReservationRentalLocation(Reservation reservation, RentalLocation rentalLocation)
			throws RARException 
	{
		// TODO Auto-generated method stub
		if(rentalLocation == null)
			throw new RARException("Invalid - rentalLocation is null");
		if(!rentalLocation.isPersistent())
			throw new RARException("Invalid - rentalLocation is not persistent.");
		reservation.setRentalLocation(rentalLocation);
		reservationManager.storeReservation(reservation);
	}

	@Override
	public RentalLocation restoreReservationRentalLocation(Reservation reservation) throws RARException {
		// TODO Auto-generated method stub
		if(reservation == null)
			throw new RARException("Invalid - reservation is null");
		if(!reservation.isPersistent())
			throw new RARException("Invalid - reservation is not persistent");
		
		return reservationManager.restoreReservationRentalLocation(reservation);
	}

	@Override
	public List<Reservation> restoreReservationRentalLocation(RentalLocation rentalLocation) throws RARException {
		// TODO Auto-generated method stub
		if(rentalLocation == null)
			throw new RARException("Invalid - rentalLocation is null");
		if(!rentalLocation.isPersistent())
			throw new RARException("Invalid - rentalLocation is not persistent");
		
		return rentalLocationManager.restoreReservationRentalLocation(rentalLocation);
	}

	@Override
	public void deleteReservationRentalLocation(Reservation reservation, RentalLocation rentalLocation)
			throws RARException {
		if(reservation == null)
			throw new RARException("Invalid - reservation is null");
		if(rentalLocation == null)
			throw new RARException("Invalid - rentalLocation is null");
		if(!reservation.isPersistent())
			throw new RARException("Invalid - reservation is not persistent");
		if(!rentalLocation.isPersistent())
			throw new RARException("Invalid - rentalLocation is not persistent");
		
		reservation.setRentalLocation(null);
		reservationManager.storeReservation(reservation);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void storeReservationVehicleType(Reservation reservation, VehicleType vehicleType) throws RARException 
		// TODO Auto-generated method stub
	{
		if(reservation == null)
			throw new RARException("Invalid - reservation is null");
		if(vehicleType == null)
			throw new RARException("Invalid - vehicleType is null");
		if(!reservation.isPersistent())
			throw new RARException("Invalid - reservation is not persistent");
		if(!vehicleType.isPersistent())
			throw new RARException("Invalid - vehicleType is not persistent");
		reservation.setVehicleType(vehicleType);
		reservationManager.storeReservation(reservation);
	}

	@Override
	public VehicleType restoreReservationVehicleType(Reservation reservation) throws RARException {
		// TODO Auto-generated method stub
		return reservationManager.restoreReservationVehicleType(reservation);
	}

	@Override
	public List<Reservation> restoreReservationVehicleType(VehicleType vehicleType) throws RARException {
		// TODO Auto-generated method stub
		return vehicleTypeManager.restoreReservationVehicleType(vehicleType);
	}

	@Override
	public void deleteReservationVehicleType(Reservation reservation, VehicleType vehicleType) throws RARException {
		// TODO Auto-generated method stub
		if(reservation == null)
			throw new RARException("Invalid - reservation is null");
		if(vehicleType == null)
			throw new RARException("Invalid - vehicleType is null");
		if(!reservation.isPersistent())
			throw new RARException("Invalid - reservation is not persistent");
		if(!vehicleType.isPersistent())
			throw new RARException("Invalid - vehicleType is not persistent");
		reservation.setVehicleType(null);
		reservationManager.storeReservation(reservation);
	}

	@Override
	public void storeVehicleRentalLocation(Vehicle vehicle, RentalLocation rentalLocation) throws RARException {
		// TODO Auto-generated method stub
		if(vehicle == null)
			throw new RARException("Invalid - vehicle is null");
		if(rentalLocation == null)
			throw new RARException("Invalid - rentalLocation is null");
		if(!vehicle.isPersistent())
			throw new RARException("Invalid - vehicle is not persistent");
		if(!rentalLocation.isPersistent())
			throw new RARException("Invalid - rentalLocation is not persistent");
	
		vehicle.setRentalLocation(rentalLocation);
		vehicleManager.storeVehicle(vehicle);
	}

	@Override
	public RentalLocation restoreVehicleRentalLocation(Vehicle vehicle) throws RARException {
		// TODO Auto-generated method stub
		if(vehicle == null)
			throw new RARException("Invalid - vehicle is null");
		if(!vehicle.isPersistent())
			throw new RARException("Invalid - vehicle is not persistent");
		
		return vehicleManager.restoreVehicleRentalLocation(vehicle);
	}

	@Override
	public List<Vehicle> restoreVehicleRentalLocation(RentalLocation rentalLocation) throws RARException {
		// TODO Auto-generated method stub
		if(rentalLocation == null)
			throw new RARException("Invalid - rentalLocation is null");
		if(!rentalLocation.isPersistent())
			throw new RARException("Invalid - rentalLocation is not persistent");
		
		return rentalLocationManager.restoreVehicleRentalLocation(rentalLocation);
	}

	@Override
	public void deleteVehicleRentalLocation(Vehicle vehicle, RentalLocation rentalLocation) throws RARException {
		// TODO Auto-generated method stub
		if(vehicle == null)
			throw new RARException("Invalid - vehicle is null");
		if(rentalLocation == null)
			throw new RARException("Invalid - rentalLocation is null");
		if(!vehicle.isPersistent())
			throw new RARException("Invalid - vehicle is not persistent");
		if(!rentalLocation.isPersistent())
			throw new RARException("Invalid - rentalLocation is not persistent");
		
		vehicle.setRentalLocation(null);
		vehicleManager.storeVehicle(vehicle);
		
	}

	@Override
	public void storeVehicleVehicleType(Vehicle vehicle, VehicleType vehicleType) throws RARException {
		// TODO Auto-generated method stub
		if(vehicle == null)
			throw new RARException("Invalid - vehicle is null");
		if(vehicleType == null)
			throw new RARException("Invalid - vehicleType is null");
		if(!vehicle.isPersistent())
			throw new RARException("Invalid - vehicle is not persistent");
		if(!vehicleType.isPersistent())
			throw new RARException("Invalid - vehicleType is not persistent");
		
		vehicle.setVehicleType(vehicleType);
		vehicleManager.storeVehicle(vehicle);
	}

	@Override
	public VehicleType restoreVehicleVehicleType(Vehicle vehicle) throws RARException {
		// TODO Auto-generated method stub
		return vehicleManager.restoreVehicleVehicleType(vehicle);
	}

	@Override
	public List<Vehicle> restoreVehicleVehicleType(VehicleType vehicleType) throws RARException {
		// TODO Auto-generated method stub
		return vehicleTypeManager.restoreVehicleVehicleType(vehicleType);
	}

	@Override
	public void deleteVehicleVehicleType(Vehicle vehicle, VehicleType vehicleType) throws RARException {
		// TODO Auto-generated method stub
		if(vehicle == null)
			throw new RARException("Invalid - vehicle is null");
		if(vehicleType == null)
			throw new RARException("Invalid - vehicleType is null");
		if(!vehicle.isPersistent())
			throw new RARException("Invalid - vehicle is not persistent");
		if(!vehicleType.isPersistent())
			throw new RARException("Invalid - vehicleType is not persistent");
		
		vehicle.setVehicleType(null);
		vehicleManager.storeVehicle(vehicle);
	}

	@Override
	public void storeVehicleTypeHourlyPrice(VehicleType vehicleType, HourlyPrice hourlyPrice) throws RARException {
		// TODO Auto-generated method stub
		
		if(hourlyPrice == null)
			throw new RARException("Invalid - hourlyPrice is null");
		if(vehicleType == null)
			throw new RARException("Invalid - vehicleType is null");
		if(!hourlyPrice.isPersistent())
			throw new RARException("Invalid - hourlyPrice is not persistent");
		if(!vehicleType.isPersistent())
			throw new RARException("Invalid - vehicleType is not persistent");
		hourlyPrice.setVehicleType(vehicleType);
		hourlyPriceManager.store(hourlyPrice);
	}

	@Override
	public VehicleType restoreVehicleTypeHourlyPrice(HourlyPrice hourlyPrice) throws RARException {
		// TODO Auto-generated method stub
		return (VehicleType) hourlyPriceManager.restoreVehicleTypeHourlyPrice(hourlyPrice);
	}
// still need to fix restoreVehicleTypeHourlyPrice....
	@Override
	public List<HourlyPrice> restoreVehicleTypeHourlyPrice(VehicleType vehicleType) throws RARException {
		// TODO Auto-generated method stub
		return vehicleTypeManager.restoreVehicleTypeHourlyPrice(vehicleType);
	}

	@Override
	public void deleteVehicleTypeHourlyPrice(VehicleType vehicleType, HourlyPrice hourlyPrice) throws RARException {
		// TODO Auto-generated method stub
		if(hourlyPrice == null)
			throw new RARException("Invalid - hourlyPrice is null");
		if(vehicleType == null)
			throw new RARException("Invalid - vehicleType is null");
		if(!hourlyPrice.isPersistent())
			throw new RARException("Invalid - hourlyPrice is not persistent");
		if(!vehicleType.isPersistent())
			throw new RARException("Invalid - vehicleType is not persistent");
		
		hourlyPrice.setVehicleType(vehicleType);
		hourlyPriceManager.store(hourlyPrice);
	}

	@Override
	public void storeRentalComment(Rental rental, Comment comment) throws RARException {
		// TODO Auto-generated method stub
		if(rental == null)
			throw new RARException("Invalid - rental is null");
		if(comment == null)
			throw new RARException("Invalid - comment is null");
		if(!rental.isPersistent())
			throw new RARException("Invalid - rental is not persistent");
		if(!comment.isPersistent())
			throw new RARException("Invalid - comment is not persistent");
		
		Customer customer = rental.getCustomer();
		comment.setRental(rental);
		comment.setCustomer(customer);
	}

	@Override
	public Rental restoreRentalComment(Comment comment) throws RARException {
		// TODO Auto-generated method stub
		return commentManager.restoreRentalComment(comment);
	}

	@Override
	public Comment restoreRentalComment(Rental rental) throws RARException {
		// TODO Auto-generated method stub
		
		return rentalManager.restoreRentalComment(rental);
	}

	@Override
	public void deleteRentalComment(Rental rental, Comment comment) throws RARException {
		// TODO Auto-generated method stub
		rental.setComment(null);
		commentManager.store(comment);
		
	}

	@Override
	public void storeRentalReservation(Rental rental, Reservation reservation) throws RARException {
		// TODO Auto-generated method stub
		rental.setReservation(reservation);
		rentalManager.store(rental);		
	}

	@Override
	public Rental restoreRentalReservation(Reservation reservation) throws RARException {
		// TODO Auto-generated method stub
		return reservationManager.restoreRentalReservation(reservation);
	}

	@Override
	public Reservation restoreRentalReservation(Rental rental) throws RARException {
		// TODO Auto-generated method stub
		return rentalManager.restoreRentalReservation(rental);
	}

	@Override
	public void deleteRentalReservation(Rental rental, Reservation reservation) throws RARException {
		// TODO Auto-generated method stub
		
		if(rental == null)
			throw new RARException("Invalid - rental is null");
		if(reservation == null)
			throw new RARException("Invalid - reservation is null");
		if(!rental.isPersistent())
			throw new RARException("Invalid - rental is not persistent");
		if(!reservation.isPersistent())
			throw new RARException("Invalid - reservation is not persistent");
		
		reservation.setRental(null);
		reservationManager.storeReservation(reservation);
	}

	@Override
	public void storeVehicleRental(Vehicle vehicle, Rental rental) throws RARException {
		// TODO Auto-generated method stub
		if(rental == null)
			throw new RARException("Invalid - rental is null");
		if(vehicle == null)
			throw new RARException("Invalid - vehicle is null");
		if(!rental.isPersistent())
			throw new RARException("Invalid - rental is not persistent");
		if(!vehicle.isPersistent())
			throw new RARException("Invalid - vehicle is not persistent");
		
		
		rental.setVehicle(vehicle);
		rentalManager.store(rental);
		
	}

	@Override
	public List<Rental> restoreVehicleRental(Vehicle vehicle) throws RARException {
		// TODO Auto-generated method stub
		return vehicleManager.restoreVehicleRental(vehicle);
	}

	@Override
	public Vehicle restoreVehicleRental(Rental rental) throws RARException {
		// TODO Auto-generated method stub
		return rentalManager.restoreVehicleRental(rental);
	}

	@Override
	public void deleteVehicleRental(Vehicle vehicle, Rental rental) throws RARException {
		// TODO Auto-generated method stub
		rental.setVehicle(null);
		rentalManager.store(rental);
	}
	
}// end class PeristenceLayerImpl