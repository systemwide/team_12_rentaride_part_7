package edu.uga.cs.rentaride.logic;

import java.util.List;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.RentalLocation;

public interface LogicLayer{
    public List<RentalLocation> findAllLocations() throws RARException;
    public long createRentalLocation(String name, String address, int capacity) throws RARException;
    public void updateRentalLocation(long id, String name, String address, int capacity) throws RARException;
    public void deleteRentalLocation(long id, String name, String address, int capacity) throws RARException;
}//LogicLayer