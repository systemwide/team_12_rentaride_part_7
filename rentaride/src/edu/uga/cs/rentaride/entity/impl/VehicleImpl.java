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
public interface VehicleImpl
    extends Persistable
{
	long id;
	String make;
	String model;
	int year;
	String registrationTag;
	int mileage;
	Date lastServiced;
	VehicleStatus status;
	VehicleCondition condition;
	VehicleType vehicleType;
	RentalLocation rentalLocation;
}