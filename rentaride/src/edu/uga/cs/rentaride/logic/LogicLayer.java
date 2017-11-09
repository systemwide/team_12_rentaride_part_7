package edu.uga.cs.rentaride.logic;

import java.util.List;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.RentalLocation;

public interface LogicLayer{
    public List<RentalLocation> findAllLocations() throws RARException;
}//LogicLayer