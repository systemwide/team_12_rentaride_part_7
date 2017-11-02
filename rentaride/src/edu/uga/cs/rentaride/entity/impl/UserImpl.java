package edu.uga.cs.rentaride.entity.impl;

import java.util.List;
import java.util.Date;
import edu.uga.cs.rentaride.persistence.*;
import edu.uga.cs.rentaride.persistence.impl.Persistent;
import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.*;

public class UserImpl
	extends Persistent
	implements User		
{
	private long			id;
	private String		firstName;
	private String		lastName;
	private String		userName;
	private String		password;
	private String		email;
	private String		address;
	private Date			createdDate;
	private UserStatus	userStatus;
	
	public UserImpl()
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
	}
	
	public UserImpl(long id, String firstName, String lastName, String userName, String password,
			String email, String address, Date createDate )
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.address = address;
		this.createdDate = createDate;
	}
	@Override
	public long getId() {

		return 0;
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
	public String getFirstName() {

		return firstName;
	}

	@Override
	public void setFirstName(String firstName) {

		this.firstName = firstName;
	}

	@Override
	public String getLastName() {

		return lastName;
	}

	@Override
	public void setLastName(String lastName) {

		this.lastName = lastName;
	}

	@Override
	public String getUserName() {

		return userName;
	}

	@Override
	public void setUserName(String userName) throws RARException {

		this.userName = userName;
		
	}

	@Override
	public String getEmail() {

		return email;
	}

	@Override
	public void setEmail(String email) {

		this.email = email;
	}

	@Override
	public String getPassword() {

		return password;
	}

	@Override
	public void setPassword(String password) {

		this.password = password;
	}

	@Override
	public Date getCreatedDate() {

		return createdDate;
	}

	@Override
	public void setCreateDate(Date createDate) {

		this.createdDate = createDate;
		
	}

	@Override
	public String getAddress() {

		return address;
	}

	@Override
	public void setAddress(String address) {

		this.address = address;
	}

	@Override
	public UserStatus getUserStatus() {

		return userStatus;	
	}

	@Override
	public void setUserStatus(UserStatus userStatus) {

		this.userStatus = userStatus;
	}	

}// end UserImpl Class
