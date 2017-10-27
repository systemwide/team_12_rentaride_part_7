package edu.uga.cs.rentaride.persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;
import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.Comment;
import edu.uga.cs.rentaride.entity.Customer;
import edu.uga.cs.rentaride.entity.Rental;
import edu.uga.cs.rentaride.entity.Reservation;
import edu.uga.cs.rentaride.entity.Vehicle;
import edu.uga.cs.rentaride.entity.impl.RentalImpl;
import edu.uga.cs.rentaride.object.ObjectLayer;

public class CommentManager{
	private ObjectLayer objectLayer = null; 
	private Connection conn = null;
	PreparedStatement stmt = null;
	int inscnt;
	long commentId;
	
	public CommentManager(Connection conn, ObjectLayer objectLayer) {
		this.conn = conn;
		this.objectLayer = objectLayer;
	}//commentManager

	public void store(Comment comment) throws RARException {
		String insertCommentSQL = "insert into comment ( ?, ?, ?, ?)";
		String updateClubSQL = "update club set text = ?, date = ?, userID = ?, rentalID = ?";
		
		try {
			if(!comment.isPersistent()) {
				stmt = (PreparedStatement) conn.prepareStatement(insertCommentSQL);
			}//if
			else {
				stmt = (PreparedStatement) conn.prepareStatement(updateClubSQL);
			}//else
			if(comment.getText()!=null) {
				stmt.setString(1,comment.getText());
			}//if
			else {
				stmt.setNull(1, java.sql.Types.VARCHAR);//since text can be NULL on a question
			}
			if(comment.getDate()!= null) {
				java.util.Date jDate = comment.getDate();
				java.sql.Date sDate = new java.sql.Date(jDate.getTime());
				stmt.setDate(2, sDate);
			}
			else {
				stmt.setNull(2, java.sql.Types.DATE);
			}
			if(comment.getCustomer()!=null) {
				stmt.setLong(3, comment.getCustomer().getId());
			}
			else {
				throw new RARException("CommentManager.store: can't save a Comment: customer is not set or not persistent");
			}
			if(comment.getRental()!=null) {
				stmt.setLong(4,  comment.getRental().getId());
			}
			else {
				throw new RARException("CommentManager.store: can't save a Comment: rental is not set or not persistent");
			}
			if(comment.isPersistent())
				stmt.setLong(5, comment.getId());
			inscnt = stmt.executeUpdate();
			if(!comment.isPersistent()) {
				if(inscnt >= 1) {
					String sql = "select last_insert_id()";
					if(stmt.execute(sql)) {//stmt return a result
						ResultSet r = stmt.getResultSet();
						
						while(r.next()) {
							commentId = r.getLong(1);
							if(commentId >0)
								comment.setId(commentId);
						}
					}
				}
				else {
					throw new RARException( "CommentManager.store: failed to save a comment");
				}
			}
			else {
				if(inscnt<1) {
					throw new RARException("CommentManager.store: failed to save a comment");
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
			throw new RARException("CommentManager.store: failed to save a comment");
		}
	}//store
	
	public List<Comment> restore(Comment comment) throws RARException {
		String selectCommentSQL = "select id, text, date, userID, rentalID from comment";
		Statement stmt =null;
		StringBuffer query = new StringBuffer(100);
		StringBuffer condition = new StringBuffer(100);
		List<Comment> comments = new ArrayList<Comment>();
		
		condition.setLength(0);
		
		query.append(selectCommentSQL);
		
		if(comment!=null) {
			if(comment.getId()>=0) {
				query.append(" where id = "+comment.getId());
			}
			else{
				if(comment.getText()!=null) {				
					condition.append(" where address = '"+comment.getText() + "'");
				}
				if(comment.getCustomer()!=null) {
					if(condition.length()>0)
						condition.append(" and");
					else
						condition.append(" where");
					condition.append(" userID = "+comment.getCustomer().getId());
				}
				if(comment.getDate()!=null) {
					if(condition.length()>0)
						condition.append(" and");
					else
						condition.append(" where");
					condition.append(" date = "+comment.getDate());
				}
				if(comment.getRental()!=null) {
					if(condition.length()>0)
						condition.append(" and");
					else
						condition.append(" where");
					condition.append(" rentalID = "+comment.getRental().getId());
				}
				if(condition.length()>0)
					query.append(condition);
			}
		}
		
		try {
			stmt = conn.createStatement();
			
			if(stmt.execute(query.toString())) {
				long id;
				String text;
				long userID;
				long rentalID;
				Date date;
				Comment nextComment = null;
				
				ResultSet rs = stmt.getResultSet();
				
				while(rs.next()) {
					id = rs.getLong(1);
					text = rs.getString(2);
					date = rs.getDate(3);
					userID = rs.getLong(4);
					rentalID = rs.getLong(5);
					//RentalImpl rental = new RentalImpl();
					//rental.setId(rentalID);
					
					nextComment = objectLayer.createComment();
					nextComment.setId(id);
					nextComment.setText(text);
					nextComment.setDate(date);
					nextComment.setRental(null);//"lazy" association traversal
					
					comments.add(nextComment);
				}
				return comments;
			}
		}
		catch(Exception e) {
			throw new RARException("CommentManager.restore: could not restore persistent Comment objects; Root cause: "+e);
		}
		throw new RARException("CommentManager.restore: could not restore persistent Comment objects.");
	}
	
	public void deleteComment(Comment comment) throws RARException {
		String deleteCommentSQL = "delete from comment where id = ?";
		java.sql.PreparedStatement stmt = null;
		int inscnt;
		
		if(!comment.isPersistent()) {
			return;
		}
		
		try {
			stmt = (PreparedStatement) conn.prepareStatement(deleteCommentSQL);
			stmt.setLong(1, comment.getId());
			inscnt = stmt.executeUpdate();
			if(inscnt==1)
				return;
			else
				throw new RARException("CommentManager.delete: failed to delete a Comment");
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new RARException("CommentManager.delete: failed to delete a Comment: "+e);
		}
	}
	
	public Rental restoreRentalComment(Comment comment) throws RARException {
		String selectRentalSQL = "select r.id, r.pickupDate, r.return, r.isLate, r.charges, r.vehicleID, r.customer, r.reservationID where p.id = c.userID";
		Statement stmt = null;
		StringBuffer query = new StringBuffer(100);
		StringBuffer condition = new StringBuffer(100);
		
		condition.setLength(100);
		
		query.append(selectRentalSQL);
		
		if(comment!=null) {
			if(comment.getId()>=0){
				query.append(" and c.id = " + comment.getId());
			}
			else if(comment.getRental()!=null)
				query.append(" and c.rentalID = " + comment.getRental().getId());
			else {
				if(comment.getText()!=null) {
					condition.append(" and c.text = '"+ comment.getText()+"'");
				}
				if(comment.getCustomer()!=null) {
					condition.append(" and c.userID = " + comment.getCustomer().getId());
				}
				if(comment.getDate()!=null) {
					condition.append(" and c.date = '" + comment.getDate()+"'");
				}
				if(condition.length()>0)
					query.append(condition);
			}
			
		}
		
		try {
			stmt = conn.createStatement();
			
			if(stmt.execute(query.toString())) {
				ResultSet rs = stmt.getResultSet();
				
				long id;
				Date pickupDate;
				Date returnDate;
				boolean isLate;
				float charges;
				long vehicleID;
				long customerID;
				long reservationID; 
				Rental rental = null;
				
				while(rs.next()) {
					id = rs.getLong(1);
					pickupDate = rs.getDate(2);
					returnDate = rs.getDate(3);
					isLate = rs.getBoolean(4);
					charges = rs.getFloat(5);
					vehicleID = rs.getLong(6);
					customerID = rs.getLong(7);
					reservationID = rs.getLong(8);
					
					rental = objectLayer.createRental(pickupDate, null, null);
					rental.setId(id);
				}
				
				return rental;
			}
			else
				return null;
		}
		catch (Exception e) {
			throw new RARException("CommentManager.restoreRentalComment: Could not restore persistent Rental object; Root Cause: "+e);
		}
	}
}