package edu.uga.cs.rentaride.entity.impl;

import java.util.Date;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.Comment;
import edu.uga.cs.rentaride.entity.Customer;
import edu.uga.cs.rentaride.entity.Rental;
import edu.uga.cs.rentaride.persistence.impl.PersistableImpl;

public class CommentImpl
	extends PersistableImpl
	implements Comment
{
	private long			id;
	private String		text;
	private Date			date;
	private Customer		customer;
	private Rental		rental;
	
	public CommentImpl()
	{
		this.id = -1;
		this.text = null;
		this.date = null;
		this.customer = null;
		this.rental = null;		
	}// end Constructor 1
	
	public CommentImpl(long id, String text, Date date, Customer customer, Rental rental)
	{
		this.id = id;
		this.text = text;
		this.date = date;
		this.customer = customer;
		this.rental = rental;
	}// end constructor 2
	
	
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
		
		return id == -1;
	}

	@Override
	public String getText() {

		return text;
	}

	@Override
	public void setText(String text) {

		this.text = text;
	}

	@Override
	public Date getDate() {

		return date;
	}

	@Override
	public void setDate(Date date) {

		this.date = date;
	}

	@Override
	public Rental getRental() {

		return rental;
	}

	@Override
	public void setRental(Rental rental) throws RARException {

		this.rental = rental;
	}

	@Override
	public Customer getCustomer() {

		return customer;
	}
	
}// end CommentImpl class