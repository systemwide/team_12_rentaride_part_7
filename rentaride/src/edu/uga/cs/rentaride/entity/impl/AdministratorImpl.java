package edu.uga.cs.rentaride.entity.impl;

import java.util.List;
import java.util.Date;
import edu.uga.cs.rentaride.persistence.*;
import edu.uga.cs.rentaride.entity.*;

public class AdministratorImpl
	extends UserImpl
	implements Administrator
	
		
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
	
	public AdministratorImpl()
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
	
	public AdministratorImpl(long id, String firstName, String lastName, String userName, String password,
			String email, String address, Date createDate )
	{
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.address = address;
		this.createdDate = createDate;
	}
	
}// end AdministratorImpl
