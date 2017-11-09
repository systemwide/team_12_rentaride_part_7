package test;

import java.sql.Date;
import java.util.List;

import edu.uga.cs.rentaride.entity.*;
import edu.uga.cs.rentaride.entity.impl.*;


public class RentARideTester {

	public static void main(String[] args) {
		//had to declare rentals and comments here so that the reservations would know they exist.
		RentalImpl rental1 = null;
		RentalImpl rental2 = null;
		
		CommentImpl comment1 = null;
		CommentImpl comment2 = null;
		
		Date commentDate = java.sql.Date.valueOf("2017-10-26");
		Date created = java.sql.Date.valueOf("2017-10-23");
		Date memberUntil = java.sql.Date.valueOf("2018-10-23");
		Date dateLastServiced = java.sql.Date.valueOf("2017-10-23");
		Date ccExp = java.sql.Date.valueOf("2020-12-12");
		Date pickupTime = java.sql.Date.valueOf("2017-10-26");
		Date returnTime = java.sql.Date.valueOf("2017-10-26");
		
		List<HourlyPrice> 	hourlyPriceList = null; //for vehicleTypeImpl
		List<Vehicle> 		vehicleList = null;
		List<Reservation> 	reservationList = null;
		
		VehicleStatus INLOCATION = VehicleStatus.INLOCATION;
		VehicleStatus INRENTAL = VehicleStatus.INRENTAL;
		VehicleCondition GOOD = VehicleCondition.GOOD;
		VehicleCondition NEEDSMAINTENANCE = VehicleCondition.NEEDSMAINTENANCE;
		UserStatus ACTIVE = UserStatus.ACTIVE;
		UserStatus TERMINATED = UserStatus.TERMINATED;
		UserStatus CANCELLED = UserStatus.CANCELLED;
		//end variables
		
		//create administrators
		AdministratorImpl admin1 = new AdministratorImpl(1, "Trey", "Speight", "tspeight", "treypassword", "treyemail",
				"treyaddress", created);
		AdministratorImpl admin2 = new AdministratorImpl(2, "Ben", "Rotolo", "brotolo", "benpassword", "benemail",
				"benaddress", created);
		System.out.println("Administrators Created\n");
		
		//create rental locations
		RentalLocationImpl r1 = new RentalLocationImpl(3, "r1", "r1Address", 25);
		RentalLocationImpl r2 = new RentalLocationImpl(4, "r2", "r2Address", 45);
		System.out.println("Rental Locations Created\n");
		
		//create vehicle types
		VehicleTypeImpl car = new VehicleTypeImpl(5, "car", hourlyPriceList, vehicleList, reservationList );
		VehicleTypeImpl truck = new VehicleTypeImpl(6, "truck", hourlyPriceList, vehicleList, reservationList );
		System.out.println("Vehicle Types Created\n");
		
		//create vehicles, 2 of each type, 2 at each rental location
		VehicleImpl car1 = new VehicleImpl(7, "Honda", "Civic", 2015, 20000, "ABC1234", dateLastServiced, INLOCATION, GOOD, r1, car);
		VehicleImpl truck1 = new VehicleImpl(8, "Ford", "F150", 2017, 25000, "ABC1235", dateLastServiced, INRENTAL, GOOD, r1, truck);
		VehicleImpl car2 = new VehicleImpl(9,"Ford", "Focus", 2013, 30000, "ABC1236", dateLastServiced, INLOCATION, GOOD, r2, car);
		VehicleImpl truck2 = new VehicleImpl(10, "Dodge", "Ram", 2014, 15000, "ABC1237", dateLastServiced, INRENTAL, NEEDSMAINTENANCE, r2, truck);
		System.out.println("Vehicles Created\n");
		
		//create customers
		CustomerImpl cust1 = new CustomerImpl(11, "Arissa", "Vercande", "uname1", "pass1", "arissa@uga", "address1", created, memberUntil, "GA", "1234", "ccNum1", ccExp, ACTIVE);
		CustomerImpl cust2 = new CustomerImpl(12, "Natalie", "Lins", "uname2", "pass2", "natalie@uga", "address2", created, memberUntil, "GA", "4321", "ccNum2", ccExp, ACTIVE);
		System.out.println("Customers Created\n");
		
		//create 2 reservations per customer with vehicle type and rental location
		ReservationImpl cust1_res1 = new ReservationImpl(13, pickupTime, 1, false, cust1, car, r1, rental1);
		ReservationImpl cust1_res2 = new ReservationImpl(14, pickupTime, 1, false, cust1, car, r2, null);
		//conflicting instructions -- Create 4 total reservations, but only 1 per customer has a rental and comment
		//can't create a reservation without a rental so I set it to null
		ReservationImpl cust2_res1 = new ReservationImpl(15, pickupTime, 1, false, cust1, car, r2, rental2);
		ReservationImpl cust2_res2 = new ReservationImpl(16, pickupTime, 1, false, cust1, truck, r2, null);
		System.out.println("Reservations Created\n");
		
		//for each reservation create a rental
		rental1 = new RentalImpl(17, pickupTime, returnTime, false, 25, car1, cust1, cust1_res1, comment1);
		rental2 = new RentalImpl(18, pickupTime, returnTime, false, 25, car1, cust2, cust2_res1, comment2);
		System.out.println("Rentals Created\n");
		
		//for each reservation create a comment
		comment1 = new CommentImpl(19, "Comment", commentDate, cust1, rental1);
		comment2	= new CommentImpl(20, "Comment", commentDate, cust2, rental2);
		System.out.println("Comments Created\n");
		
		//delete everything
		//todo
		
		System.out.println("Done\n");
	}
}
