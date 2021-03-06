package edu.uga.cs.rentaride.logic.impl;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import edu.uga.cs.rentaride.logic.LogicLayer;
import edu.uga.cs.rentaride.logic.impl.CreateRentalLocCtrl;
import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.Customer;
import edu.uga.cs.rentaride.entity.RentalLocation;
import edu.uga.cs.rentaride.entity.Reservation;
import edu.uga.cs.rentaride.entity.VehicleCondition;
import edu.uga.cs.rentaride.entity.VehicleStatus;
import edu.uga.cs.rentaride.entity.VehicleType;
import edu.uga.cs.rentaride.object.ObjectLayer;
import edu.uga.cs.rentaride.object.impl.ObjectLayerImpl;
import edu.uga.cs.rentaride.persistence.PersistenceLayer;
import edu.uga.cs.rentaride.persistence.impl.PersistenceLayerImpl;
import edu.uga.cs.rentaride.session.Session;
import edu.uga.cs.rentaride.session.SessionManager;

public class LogicLayerImpl implements LogicLayer{
	private ObjectLayer objectLayer = null;

	public LogicLayerImpl(Connection conn){
		objectLayer = new ObjectLayerImpl();
		PersistenceLayer persistenceLayer = new PersistenceLayerImpl(conn, objectLayer);
		objectLayer.setPersistence(persistenceLayer);
		System.out.println("LogicLayerImpl.LogicLayerImpl(conn): initialized");
	}//LogicLayerImpl

	public LogicLayerImpl(ObjectLayer objectLayer){
		this.objectLayer = objectLayer;
		System.out.println("LogicLayerImpl.LogicLayerImpl(objectLayer):initialized");
	}//LogicLayerImol

	public List<RentalLocation> findAllLocations() throws RARException{
		FindAllLocationsCtrl ctrlFindAllLocations = new FindAllLocationsCtrl(objectLayer);
		return ctrlFindAllLocations.findAllLocations();
	}//findAllLocations
	
	public long createRentalLocation(String name, String address, int capacity) throws RARException{
		CreateRentalLocCtrl ctrlCreateLoc = new CreateRentalLocCtrl(objectLayer);
		return ctrlCreateLoc.createRentalLocation(name, address, capacity);
	}
	
	public void updateRentalLocation(long id, String name, String address, int capacity) throws RARException {
		UpdateRentalLocCtrl ctrlUpdateLoc = new UpdateRentalLocCtrl(objectLayer);
		ctrlUpdateLoc.updateRentalLocation(id, name, address, capacity);
	}
	
	public void deleteRentalLocation(long id, String name, String address, int capacity) throws RARException {
		DeleteRentalLocCtrl ctrlDeleteLoc = new DeleteRentalLocCtrl(objectLayer);
		ctrlDeleteLoc.deleteRentalLocation(id, name, address, capacity);
	}
	
	public long createVehicle(long id, String make, String model, int year, int mileage, String registrationTag,
			Date lastServiced, VehicleStatus status, VehicleCondition condition, RentalLocation rentalLocation,
			VehicleType vehicleType) throws RARException
	{
		CreateVehicleCtrl ctrlCreateVehicle = new CreateVehicleCtrl(objectLayer);
		return ctrlCreateVehicle.createVehicle(id, make, model, year, mileage, registrationTag, lastServiced, 
				status, condition, rentalLocation, vehicleType);
	}

	@Override
	public List<Customer> browseCustomers() {
		BrowseCustomerCtrl browseCustCtrl = new BrowseCustomerCtrl(objectLayer);
		try {
			return browseCustCtrl.ViewCustomerInfo();
		} catch (RARException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<Reservation> findAllReservations(int id) {
		FindAllReservationsCtrl ctrlFindAllReservations = new FindAllReservationsCtrl( objectLayer );
		try {
			return ctrlFindAllReservations.findAllReservations(id);
		} catch (RARException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}// ebd findAllReservations

	@Override
	public List<Reservation> findAllReservations() {
		FindAllReservationsCtrl ctrlFindAllReservations = new FindAllReservationsCtrl( objectLayer );
		try {
			return ctrlFindAllReservations.findAllReservations();
		} catch (RARException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}// end findAllReservations

	@Override
	public String login(Session session, String username, String password) {
		LoginCtrl ctrlVerifyUser = new LoginCtrl(objectLayer);
		try {
			return ctrlVerifyUser.login(session, username, password);
		} catch (RARException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}// end login
}//LogicLayerImpl