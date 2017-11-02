package edu.uga.cs.rentaride.entity.impl;

import java.util.Date;
import java.util.List;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.*;


public class CustomerImpl
	extends UserImpl
	implements Customer
{
	private long			id;
	private String		firstName;
	private String		lastName;
	private String		userName;
	private String		password;
	private String		email;
	private String		address;
	private Date			createdDate;
	private Date			memberUntil;
	private String		licenseState;
	private String		licenseNumb;
	private String		ccNumb;
	private Date			ccExpDate;
	private UserStatus	userStatus;
	private List<Reservation> reservations;
	
	public CustomerImpl()
	{
		super();
		this.id = 			-1;
		this.firstName = 	null;
		this.lastName = 		null;
		this.userName = 		null;
		this.password = 		null;
		this.email = 		null;
		this.address = 		null;
		this.createdDate =	null;
		this.memberUntil = 	null;
		this.licenseState = 	null;
		this.licenseNumb =	null;
		this.ccNumb = 		null;
		this.ccExpDate = 	null;
		this.userStatus = 	null;
	}
	
	public CustomerImpl(int id, String firstName, String lastName, String userName, String password,
			String email, String address, Date createDate, Date memberUntil, String licenseState, 
			String licenseNumb, String ccNumb, Date ccExpDate, UserStatus userStatus)
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.address = address;
		this.createdDate = createDate;
		this.memberUntil = 	memberUntil;
		this.licenseState = 	licenseState;
		this.licenseNumb =	licenseNumb;
		this.ccNumb = 		ccNumb;
		this.ccExpDate = 	ccExpDate;
		this.userStatus = 	userStatus;
	}
	
	@Override
	public boolean isPersistent() {
		
		return id != -1;
	}

	@Override
	public Date getMemberUntil() {

		return memberUntil;
	}

	@Override
	public void setMemberUntil(Date memberUntil) throws RARException {

		if(this.userStatus.toString() == "Active") throw new RARException("Member is not currently 'ACTIVE'!");
		
		else this.memberUntil = memberUntil;
	}

	@Override
	public String getLicenseState() {

		return licenseState;
	}

	@Override
	public void setLicenseState(String state) {

		this.licenseState = state;
	}

	@Override
	public String getLicenseNumber() {

		return licenseNumb;
	}

	@Override
	public void setLicenseNumber(String licenseNumber) {

		this.licenseNumb = licenseNumber;
	}

	@Override
	public String getCreditCardNumber() {

		return ccNumb;
	}

	@Override
	public void setCreditCardNumber(String cardNumber) {

		this.ccNumb = cardNumber;
	}

	@Override
	public Date getCreditCardExpiration() {

		return ccExpDate;
	}

	@Override
	public void setCreditCardExpiration(Date cardExpiration) {

		this.ccExpDate = cardExpiration;
	}

	@Override
	public List<Reservation> getReservations() {
		if (reservations == null)
		{
			if(isPersistent())
			{
				try {
					reservations = getPersistenceLayer().restoreCustomerReservation(this);
				}
				catch(RARException e)
				{
					System.out.println(e);
				}
			}
		}
		return reservations;
	}

	@Override
	public List<Comment> getComments() {

		List<Comment> list = null;
		if(this.isPersistent())
		{
			list = this.getComments();
			return list;
		}
		else return null;
	}

	@Override
	public List<Rental> getRentals() {

		List<Rental> list = null;
		if(this.isPersistent())
		{
			list = this.getRentals();
		}
		return list;
	}

}// end class
