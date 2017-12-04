package edu.uga.cs.rentaride.logic.impl;

import java.util.List;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.Customer;
import edu.uga.cs.rentaride.entity.Reservation;
import edu.uga.cs.rentaride.entity.impl.CustomerImpl;
import edu.uga.cs.rentaride.entity.impl.ReservationImpl;
import edu.uga.cs.rentaride.object.ObjectLayer;

public class FindAllReservationsCtrl {

	private ObjectLayer objectLayer = null;

	public FindAllReservationsCtrl(ObjectLayer objectModel) {
		this.objectLayer = objectModel;
	}


	// this is the method for admins - takes no argument and returns all reservations
	public List<Reservation> findAllReservations() throws RARException
	{
		List<Reservation> reservations = null;

		reservations = objectLayer.findReservation(null);

		return reservations;

	}// end findAllReservations for admins



	public List<Reservation> findAllReservations(long id) throws RARException
	{
		List<Reservation> reservations = null;

		Reservation resModel = new ReservationImpl();
		Customer custModel = new CustomerImpl();
		custModel.setId(id);
		
		resModel.setCustomer(custModel);
		
		reservations = objectLayer.findReservation(resModel);
		
		return reservations;
	}


}// end class
