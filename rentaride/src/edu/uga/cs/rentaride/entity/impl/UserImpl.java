package edu.uga.cs.rentaride.entity.impl;

import java.util.List;
import java.util.Date;
import edu.uga.cs.rentaride.persistence.*;
import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.*;

public class UserImpl
	extends Persistable
	implements User
{
	private int			id;
	private String		firstName;
	private String		lastName;
	private String		userName;
	private String		password;
	private String		email;
	private String		address;
	private Date			createdDate;
	private UserStatus	userStatus;
/*
 * firstName : String
 * lastName : String
 * userName : String
 * password : String
 * email : String
 * address : String
 * createdDate : Date
 */
	
	public UserImpl()
	{
		this.id = 			-1;
		this.firstName = 	null;
		this.lastName = 		null;
		this.userName = 		null;
		this.password = 		null;
		this.email = 		null;
		this.address = 		null;
		this.createdDate =	null;
	}
	
	public UserImpl(int id, String firstName, String lastName, String userName, String password,
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setId(long id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	@Override
	public boolean isPersistent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getFirstName() {
		// TODO Auto-generated method stub
		return firstName;
	}

	@Override
	public void setFirstName(String firstName) {
		// TODO Auto-generated method stub
		this.firstName = firstName;
	}

	@Override
	public String getLastName() {
		// TODO Auto-generated method stub
		return lastName;
	}

	@Override
	public void setLastName(String lastName) {
		// TODO Auto-generated method stub
		this.lastName = lastName;
	}

	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return userName;
	}

	@Override
	public void setUserName(String userName) throws RARException {
		// TODO Auto-generated method stub
		this.userName = userName;
		
	}

	@Override
	public String getEmail() {
		// TODO Auto-generated method stub
		return email;
	}

	@Override
	public void setEmail(String email) {
		// TODO Auto-generated method stub
		this.email = email;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public void setPassword(String password) {
		// TODO Auto-generated method stub
		this.password = password;
	}

	@Override
	public Date getCreatedDate() {
		// TODO Auto-generated method stub
		return createdDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		// TODO Auto-generated method stub
		this.createdDate = createDate;
		
	}

	@Override
	public String getAddress() {
		// TODO Auto-generated method stub
		return address;
	}

	@Override
	public void setAddress(String address) {
		// TODO Auto-generated method stub
		this.address = address;
	}

	@Override
	public UserStatus getUserStatus() {
		// TODO Auto-generated method stub
		return userStatus;	
	}

	@Override
	public void setUserStatus(UserStatus userStatus) {
		// TODO Auto-generated method stub
		this.userStatus = userStatus;
	}	

}// end UserImpl Class
