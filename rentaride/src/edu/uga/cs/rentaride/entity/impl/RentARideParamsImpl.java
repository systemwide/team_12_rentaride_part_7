package edu.uga.cs.rentaride.entity.impl;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.RentARideParams;

public class RentARideParamsImpl
	implements RentARideParams
{
	private long		id;
	private int 		membershipPrice;
	private int		lateFee;
	
	public RentARideParamsImpl()
	{
		this.id = 					-1;
		this.membershipPrice = 		-1;
		this.lateFee = 				-1;
	}
	
	public RentARideParamsImpl(long id, int membershipPrice, int lateFee)
	{
		this.id = 					id;
		this.membershipPrice = 		membershipPrice;
		this.lateFee =				lateFee;
	}
	
	
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
	public int getMembershipPrice() {

		return membershipPrice;
	}

	@Override
	public void setMembershipPrice(int membershipPrice) throws RARException {

		this.membershipPrice = membershipPrice;
	}

	@Override
	public int getLateFee() {

		return lateFee;
	}

	@Override
	public void setLateFee(int lateFee) throws RARException {

		this.lateFee = lateFee;
	}
	
}// end class RentARideParamsImpl