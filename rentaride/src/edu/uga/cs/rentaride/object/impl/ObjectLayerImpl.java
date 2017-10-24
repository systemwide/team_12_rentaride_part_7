package edu.uga.cs.rentaride.object.impl;

import java.util.Date;
import java.util.List;


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
import edu.uga.cs.rentaride.entity.VehicleCondition;
import edu.uga.cs.rentaride.entity.VehicleStatus;
import edu.uga.cs.rentaride.entity.VehicleType;
import edu.uga.cs.rentaride.entity.impl.*;
import edu.uga.cs.rentaride.object.ObjectLayer;
import edu.uga.cs.rentaride.persistence.PersistenceLayer;
import edu.uga.cs.rentaride.persistence.impl.PersistableImpl;

public class ObjectLayerImpl implements ObjectLayer {
	
	PersistenceLayer persistence = null;
	 /**
     * Create a new Administrator object, given the set of initial attribute values.
     * The UserStatus of the new Administrator object is UserStatus.ACTIVE.
     * @param firstName the first name
     * @param lastName the last name
     * @param userName the user name (login name)
     * @param password the password
     * @param email the email address
     * @param address the address
     * @param createDate the creation date
     * @return the new Administrator object instance with the given attribute values, UserStatus is UserStatus.ACTIVE
     * @throws RARException in case the userName is non-unique
     */
	public ObjectLayerImpl() {
		this.persistence = null;
		System.out.println("ObjectLayerImpl.ObjectLayerImpl(): initialized");
	}
	
	public ObjectLayerImpl(PersistenceLayer persistence) {
		this.persistence = persistence;
		System.out.println("ObjectLayerImpl.ObjectLayerImpl(persistence): initialized");
	}
	
    public Administrator createAdministrator( String firstName, String lastName, String userName, 
                                              String password, String email, String address, Date createDate ) throws RARException{
    	AdministratorImpl admin = new AdministratorImpl(0, firstName, lastName, userName, password, email, address, createDate);
    	PersistableImpl.setPersistenceLayer(persistence);
    	return admin;
    }

    /**
     * Create a new Administrator object with undefined attribute values.
     * The UserStatus of the new Administrator object is UserStatus.ACTIVE.
     * @return the new Administrator object instance
     */
    public Administrator createAdministrator() {
    	AdministratorImpl admin = new AdministratorImpl();
    	PersistableImpl.setPersistenceLayer(persistence);
    	return admin;
    }
    
    /**
     * Return an List of Administrator objects satisfying the search criteria given in the modelAdministrator object.
     * @param modelAdministrator a model Administrator object specifying the search criteria
     * @return an List of the located Administrator objects
     * @throws RARException in case there is a problem with the retrieval of the requested objects
     */
    public List<Administrator> findAdministrator( Administrator modelAdministrator ) throws RARException{
    	return persistence.restoreAdministrator(modelAdministrator);
    }
    
    /**
     * Store a given Administrator object in persistent data store.
     * @param administrator the object to be persisted
     * @throws RARException in case there was an error while persisting the object
     */
    public void storeAdministrator( Administrator administrator ) throws RARException{
    	persistence.storeAdministrator(administrator);
    }
    
    /**
     * Delete this Administrator object.
     * @param administrator the object to be deleted.
     * @throws RARException in case there is a problem with the deletion of the object
     */
    public void deleteAdministrator( Administrator administrator ) throws RARException{
    	persistence.deleteAdministrator(administrator);
    }
    
    /**
     * Create a new Customer object, given the set of initial attribute values.
     * The UserStatus of the new Customer object is UserStatus.ACTIVE.
     * @param firstName the first name
     * @param lastName the last name
     * @param userName the user (login) name
     * @param password the password
     * @param email the email address
     * @param address the address
     * @param createDate the date when the customer has been created
     * @param membershipExpiration the date when the membership expires
     * @param licenseState the issuing state of the driver's license
     * @param licenseNumber the license number
     * @param cardNumber the credit card number
     * @param cardExpiration the expiration date of the credit card
     * @return the new Customer object instance with the given attribute values, UserStatus is UserStatus.ACTIVE
     * @throws RARException in case the userName is non-unique
     */
    public Customer createCustomer( String firstName, String lastName, String userName, String password,
            String email, String address, Date createDate, Date membershipExpiration, String licenseState, 
            String licenseNumber, String cardNumber, Date cardExpiration ) throws RARException{
    	CustomerImpl customer = new CustomerImpl(0, firstName, lastName, userName, password, email, address, createDate, membershipExpiration, licenseState, licenseNumber,cardNumber, cardExpiration, null);
    	PersistableImpl.setPersistenceLayer(persistence);
    	return customer;
    }

    /**
     * Create a new Customer object with undefined attribute values.
     * The UserStatus of the new Administrator object is UserStatus.ACTIVE.
     * @return the new Customer object instance
     */
    public Customer createCustomer() {
    	CustomerImpl customer = new CustomerImpl();
    	PersistableImpl.setPersistenceLayer(persistence);
    	return customer;
    }
    
    /**
     * Return an List of Customer objects satisfying the search criteria given in the modelCustomer object.
     * @param modelCustomer a model Customer object specifying the search criteria
     * @return an List of the located Customer objects
     * @throws RARException in case there is a problem with the retrieval of the requested objects
     */
    public List<Customer> findCustomer( Customer modelCustomer ) throws RARException{
    	return persistence.restoreCustomer(modelCustomer);
    }
    
    /**
     * Store a given Customer object in persistent data store.
     * @param customer the object to be persisted
     * @throws RARException in case there was an error while persisting the object
     */
    public void storeCustomer( Customer customer ) throws RARException{
    	persistence.storeCustomer(customer);
    }
    
    // No need to have a delete Customer method, as a Customer will be "removed" by changing UserStatus
    /**
     * Delete this Customer object.
     * @param customer the object to be deleted.
     * @throws RARException in case there is a problem with the deletion of the object
     */
    // public void deleteCustomer( Customer customer ) throws RARException;      

    /**
     * Create a new RentalLocation object, given the set of initial attribute values.
     * @param name the name of the location
     * @param address the address of the location
     * @param capacity the capacity of the location
     * @return the new RentalLocation object instance with the given attribute values
     * @throws RARException in case the name is non-unique or the capacity is non-positive
     */
    public RentalLocation createRentalLocation( String name, String address, int capacity ) throws RARException{
    	RentalLocationImpl rentalLoc = new RentalLocationImpl();
    	rentalLoc.setName(name);
    	rentalLoc.setAddress(address);
    	rentalLoc.setCapacity(capacity);
    	PersistableImpl.setPersistenceLayer(persistence);
    	return rentalLoc;
    }

    /**
     * Create a new RentalLocation object with undefined attribute values.
     * @return the new RentalLocation object instance
     */
    public RentalLocation createRentalLocation() {
    	RentalLocationImpl rentalLoc = new RentalLocationImpl();
    	PersistableImpl.setPersistenceLayer(persistence);
    	return rentalLoc;
    }

    /**
     * Return an List of RentalLocation objects satisfying the search criteria given in the modelRentalLocation object.
     * @param modelRentalLocation a model RentalLocation object specifying the search criteria
     * @return an List of the located RentalLocation objects
     * @throws RARException in case there is a problem with the retrieval of the requested objects
     */
    public List<RentalLocation> findRentalLocation( RentalLocation modelRentalLocation ) throws RARException{
    	return persistence.restoreRentalLocation(modelRentalLocation);
    }
    
    /**
     * Store a given RentalLocation object in persistent data store.
     * @param rentalLocation the object to be persisted
     * @throws RARException in case there was an error while persisting the object
     */
    public void storeRentalLocation( RentalLocation rentalLocation ) throws RARException{
    	persistence.storeRentalLocation(rentalLocation);
    }
    
    /**
     * Delete this RentalLocation object.
     * @param rentalLocation the object to be deleted.
     * @throws RARException in case there is a problem with the deletion of the object
     */
    public void deleteRentalLocation( RentalLocation rentalLocation ) throws RARException{
    	persistence.deleteRentalLocation(rentalLocation);
    }

    /**
     * Create a new Reservation object, given the set of initial attribute values.
     * @param pickupTime the requested pickup time
     * @param rentalLength the length of the requested rental (in hours)
     * @param vehicleType the type of the vehicle reserved
     * @param rentalLocation the location for this reservation
     * @param customer the customer making the reservation
     * @return the new Reservation object instance with the given attribute values
     * @throws RARException in case either the pickupTime is in the past, rentalLength is non-positive, or if the vehicleType, rentalLocation, customer is null
     */
    public Reservation createReservation( Date pickupTime, int rentalLength, VehicleType vehicleType, 
                                          RentalLocation rentalLocation, Customer customer ) throws RARException{
    	ReservationImpl reservation = new ReservationImpl(0, pickupTime, rentalLength, null, customer, vehicleType, rentalLocation, null);// null1 = Bool cancelled, null2 = rental
    	PersistableImpl.setPersistenceLayer(persistence);
    	return reservation;
    }

    /**
     * Create a new Reservation object with undefined attribute values.
     * @return the new Reservation object instance
     */
    public Reservation createReservation() {
    	ReservationImpl reservation = new ReservationImpl();
    	PersistableImpl.setPersistenceLayer(persistence);
    	return reservation;
    }

    /**
     * Return an List of Reservation objects satisfying the search criteria given in the modelReservation object.
     * @param modelReservation a model Reservation object specifying the search criteria
     * @return an List of the located Reservation objects
     * @throws RARException in case there is a problem with the retrieval of the requested objects
     */
    public List<Reservation> findReservation( Reservation modelReservation ) throws RARException{
    	return persistence.restoreReservation(modelReservation);
    }
    
    /**
     * Store a given Reservation object in persistent data store.
     * @param reservation the object to be persisted
     * @throws RARException in case there was an error while persisting the object
     */
    public void storeReservation( Reservation reservation ) throws RARException{
    	persistence.storeReservation(reservation);
    }
    
    /**
     * Delete this Reservation object.
     * @param reservation the object to be deleted.
     * @throws RARException in case there is a problem with the deletion of the object
     */
    public void deleteReservation( Reservation reservation ) throws RARException{
    	persistence.deleteReservation(reservation);
    }

    /**
     * Create a new Rental object, given the set of initial attribute values.
     * @param pickupTime the pickup time of the vehicle
     * @param reservation the reservation for this rental
     * @param vehicle the vehicle being rented
     * @return the new Reservation object instance with the given attribute values
     * @throws RARException in case either the pickupTime is in the past or if the reservation or the vehicle is null
     */
    public Rental createRental( Date pickupTime, Reservation reservation, Vehicle vehicle ) throws RARException{
    	RentalImpl rental = new RentalImpl(0, pickupTime, null, null, 0, vehicle, null, reservation, null);
    	PersistableImpl.setPersistenceLayer(persistence);
    	return rental;
    }

    /**
     * Create a new Rental object with undefined attribute values.
     * @return the new Rental object instance
     */
    public Rental createRental() {
    	RentalImpl rental = new RentalImpl();
    	PersistableImpl.setPersistenceLayer(persistence);
    	return rental;
    }

    /**
     * Return a List of Rental objects satisfying the search criteria given in the modelRental object.
     * @param modelRental a model Rental object specifying the search criteria
     * @return an List of the located Rental objects
     * @throws RARException in case there is a problem with the retrieval of the requested objects
     */
    public List<Rental> findRental( Rental modelRental ) throws RARException{
    	return persistence.restoreRental(modelRental);
    }
    
    /**
     * Store a given Rental object in persistent data store.
     * @param rental the object to be persisted
     * @throws RARException in case there was an error while persisting the object
     */
    public void storeRental( Rental rental ) throws RARException{
    	persistence.storeRental(rental);
    }
    
    /**
     * Delete this Rental object.
     * @param rental the object to be deleted.
     * @throws RARException in case there is a problem with the deletion of the object
     */
    public void deleteRental( Rental rental ) throws RARException{
    	persistence.deleteRental(rental);
    }
    
    /**
     * Create a new VehicleType object, given the set of initial attribute value.
     * @param name the name of the vehicle type, e.g. "Sedan", "Pickup", etc.
     * @return the new VehicleType object instance with the given attribute value
     * @throws RARException in case the vehicle type name is non-unique
     */
    public VehicleType createVehicleType( String name ) throws RARException{
    	VehicleTypeImpl vehicleType = new VehicleTypeImpl(0, name, null, null, null);
    	PersistableImpl.setPersistenceLayer(persistence);
    	return vehicleType;
    }

    /**
     * Create a new VehicleType object with undefined attribute values.
     * @return the new VehicleType object instance
     */
    public VehicleType createVehicleType() {
    	VehicleTypeImpl vehicle = new VehicleTypeImpl();
    	PersistableImpl.setPersistenceLayer(persistence);
    	return vehicle;
    }

    /**
     * Return a List of VehicleType objects satisfying the search criteria given in the modelVehicleType object.
     * @param modelVehicleType a model VehicleType object specifying the search criteria
     * @return an List of the located VehicleType objects
     * @throws RARException in case there is a problem with the retrieval of the requested objects
     */
    public List<VehicleType> findVehicleType( VehicleType modelVehicleType ) throws RARException{
    	return persistence.restoreVehicleType(modelVehicleType);
    }
    
    /**
     * Store a given VehicleType object in persistent data store.
     * @param vehicleType the object to be persisted
     * @throws RARException in case there was an error while persisting the object
     */
    public void storeVehicleType( VehicleType vehicleType ) throws RARException{
    	persistence.storeVehicleType(vehicleType);
    }
    
    /**
     * Delete this VehicleType object.
     * @param vehicleType the object to be deleted.
     * @throws RARException in case there is a problem with the deletion of the object
     */
    public void deleteVehicleType( VehicleType vehicleType ) throws RARException{
    	persistence.deleteVehicleType(vehicleType);
    }

    /**
     * Create a new Vehicle object, given the set of initial attribute value.
     * @param make the make of the vehicle
     * @param model the model
     * @param year the year of the vehicle
     * @param registrationTag the registration tag
     * @param mileage the current mileage of the vehicle
     * @param lastServiced the date when the vehicle was last serviced
     * @param vehicleType the type of the created vehicle (cannot be null)
     * @param rentalLocation the rental location of this vehicle (cannot be null)
     * @param vehicleCondition the condition of this vehicle 
     * @param vehicleStatus the status of this vehicle 
     * @return the new Vehicle object instance with the given attribute values
     * @throws RARException in case either the year or mileage are non-positive, or the vehicleType and/or the rentalLocation is null
     */
    public Vehicle createVehicle( String make, String model, int year, String registrationTag, int mileage, Date lastServiced,
                                  VehicleType vehicleType, RentalLocation rentalLocation, VehicleCondition vehicleCondition, 
                                  VehicleStatus vehicleStatus ) throws RARException{
    	VehicleImpl vehicle = new VehicleImpl(0, make, model, year,  mileage, registrationTag,lastServiced, vehicleStatus, vehicleCondition, rentalLocation, vehicleType);
    	PersistableImpl.setPersistenceLayer(persistence);
    	return vehicle;
    }

    /**
     * Create a new Vehicle object with undefined attribute values.
     * @return the new Vehicle object instance
     */
    public Vehicle createVehicle() {
    	VehicleImpl vehicle = new VehicleImpl();
    	PersistableImpl.setPersistenceLayer(persistence);
    	return vehicle;
    }

    /**
     * Return a List of Vehicle objects satisfying the search criteria given in the modelVehicle object.
     * @param modelVehicle a model Vehicle object specifying the search criteria
     * @return an List of the located Vehicle objects
     * @throws RARException in case there is a problem with the retrieval of the requested objects
     */
    public List<Vehicle> findVehicle( Vehicle modelVehicle ) throws RARException{
    	return persistence.restoreVehicle(modelVehicle);
    }
    
    /**
     * Store a given Vehicle object in persistent data store.
     * @param vehicle the object to be persisted
     * @throws RARException in case there was an error while persisting the object
     */
    public void storeVehicle( Vehicle vehicle ) throws RARException{
    	persistence.storeVehicle(vehicle);
    }
    
    /**
     * Delete this Vehicle object.
     * @param vehicle the object to be deleted.
     * @throws RARException in case there is a problem with the deletion of the object
     */
    public void deleteVehicle( Vehicle vehicle ) throws RARException{
    	persistence.deleteVehicle(vehicle);
    }

    /**
     * Create a new Comment object, given the set of initial attribute value.
     * @param text the text of the comment
     * @param date the date the comment was made
     * @param rental the rental for which the comment is made
     * @return the new Comment object instance with the given attribute values
     * @throws RARException in case either the rental is null
     */
    public Comment createComment( String text, Date date, Rental rental ) throws RARException{
    	CommentImpl comment = new CommentImpl(0, text, date, null, rental);
    	PersistableImpl.setPersistenceLayer(persistence);
    	return comment;
    }

    /**
     * Create a new Comment object with undefined attribute values.
     * @return the new Comment object instance
     */
    public Comment createComment() {
    	CommentImpl comment = new CommentImpl();
    	PersistableImpl.setPersistenceLayer(persistence);
    	return comment;
    }

    /**
     * Return a List of Comment objects satisfying the search criteria given in the modelComment object.
     * @param modelComment a model Comment object specifying the search criteria
     * @return an List of the located Comment objects
     * @throws RARException in case there is a problem with the retrieval of the requested objects
     */
    public List<Comment> findComment( Comment modelComment ) throws RARException{
    	return persistence.restoreComment(modelComment);
    }
    
    /**
     * Store a given Comment object in persistent data store.
     * @param comment the object to be persisted
     * @throws RARException in case there was an error while persisting the object
     */
    public void storeComment( Comment comment ) throws RARException{
    	persistence.storeComment(comment);
    }
    
    /**
     * Delete this Comment object.
     * @param comment the object to be deleted.
     * @throws RARException in case there is a problem with the deletion of the object
     */
    public void deleteComment( Comment comment ) throws RARException{
    	persistence.deleteComment(comment);
    }

    /**
     * Create a new HourlyPrice object, given the set of initial attribute values.
     * @param maxHrs the maximum number of hours for this price
     * @param price the price for this hourly range
     * @param vehicleType the vehicle type this hourly price is for
     * @return the new HourlyPrice object instance with the given attribute values
     * @throws RARException in case either maxHrs or price is non-positive or if the vehicleType is null
     */
    public HourlyPrice createHourlyPrice( int maxHrs, int price, VehicleType vehicleType ) throws RARException{
    	HourlyPriceImpl hourlyPrice = new HourlyPriceImpl(0, maxHrs, price, vehicleType);
    	PersistableImpl.setPersistenceLayer(persistence);
    	return hourlyPrice;
    }

    /**
     * Create a new HourlyPrice object with undefined attribute values.
     * @return the new HourlyPrice object instance
     */
    public HourlyPrice createHourlyPrice() {
    	HourlyPriceImpl hourlyPrice = new HourlyPriceImpl();
    	PersistableImpl.setPersistenceLayer(persistence);
    	return hourlyPrice;
    }

    /**
     * Return a List of HourlyPrice objects satisfying the search criteria given in the modelHourlyPrice object.
     * @param modelHourlyPrice a model HourlyPrice object specifying the search criteria
     * @return an List of the located HourlyPrice objects
     * @throws RARException in case there is a problem with the retrieval of the requested objects
     */
    public List<HourlyPrice> findHourlyPrice( HourlyPrice modelHourlyPrice ) throws RARException{
    	return persistence.restoreHourlyPrice(modelHourlyPrice);
    }
    
    /**
     * Store a given HourlyPrice object in persistent data store.
     * @param hourlyPrice the object to be persisted
     * @throws RARException in case there was an error while persisting the object
     */
    public void storeHourlyPrice( HourlyPrice hourlyPrice ) throws RARException{
    	persistence.storeHourlyPrice(hourlyPrice);
    }
    
    /**
     * Delete this HourlyPrice object.
     * @param hourlyPrice the object to be deleted.
     * @throws RARException in case there is a problem with the deletion of the object
     */
    public void deleteHourlyPrice( HourlyPrice hourlyPrice ) throws RARException{
    	persistence.deleteHourlyPrice(hourlyPrice);
    }

    /**
     * Create a new RentARideConfig object with undefined attribute values.
     * @return the new RentARideConfig object instance
     */
    public RentARideParams createRentARideParams() {
    	
    }
    
    /**
     * Return the RentARideConfig object.  The RentARideConfig class is a singleton class,
     * so only one object will exist.
     * @return the RentARideConfig object
     */
    public RentARideParams findRentARideParams() {
    	try {
    		return persistence.restoreRentARideConfig();
    	}catch(RARException r) {
    		return null;
    	}
    }
    
    /**
     * Store a given RentARideConfig object in persistent data store.
     * @param rentARideParams the object to be persisted
     * @throws RARException in case there was an error while persisting the object
     */
    public void storeRentARideParams( RentARideParams rentARideParams ) throws RARException{
    	persistence.storeRentARideConfig(rentARideParams);
    }

}