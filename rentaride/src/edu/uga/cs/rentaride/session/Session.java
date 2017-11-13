package edu.uga.cs.rentaride.session;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.User;
import edu.uga.cs.rentaride.logic.LogicLayer;
import edu.uga.cs.rentaride.logic.impl.LogicLayerImpl;
import edu.uga.cs.rentaride.object.ObjectLayer;
import edu.uga.cs.rentaride.object.impl.ObjectLayerImpl;
import edu.uga.cs.rentaride.persistence.PersistenceLayer;
import edu.uga.cs.rentaride.persistence.impl.PersistenceLayerImpl;

public class Session extends Thread {

	private Connection conn;
	private ObjectLayer objectLayer;
	private LogicLayer logicLayer;
	private User user;
	private String id;
	private Date expiration;

	public Session(Connection conn){
		this.conn = conn;
		objectLayer = new ObjectLayerImpl();
		PersistenceLayer persistence = new PersistenceLayerImpl(conn, objectLayer);
		objectLayer.  setPersistence(persistence);
		logicLayer = new LogicLayerImpl(objectLayer);
		extendExpiration();
	}//Session

	public Connection getConnection(){
		extendExpiration();
		return conn;
	}//getConnection

	public User getUser(){
		extendExpiration();
		return user;
	}//getUser

	public void setUser(User user){
		extendExpiration();
		this.user = user;
	}//setUser

	public String getSessionId(){
		extendExpiration();
		return id;
	}//getSessionId

	public void setSessionId(String id){
		this.id = id;
	}//setSessionId

	public Date getExpiration(){
		return expiration;
	}//getExpiration

	public void setExpiration(Date expiration){
		this.expiration = expiration;
	}//setExpiration

	private void extendExpiration(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, 30);
		this.expiration = c.getTime();
	}//extendExpiration

	public ObjectLayer getObjectLayer(){
		return objectLayer;
	}//getObjectLayer

	public void run(){
		long diff = expiration.getTime()-System.currentTimeMillis();
		while(diff>0){
			try{
				sleep(diff);
			}catch(Exception e){
				break;
			}//try-catch
			diff=expiration.getTime()-System.currentTimeMillis();
		}//while

		try{
			SessionManager.removeSession(this);
		}catch(RARException e){
			try{
				throw e;
			}catch(RARException e1){
				e1.printStackTrace();
			}//try-catch
		}//try-catch
	}//run

	public LogicLayer getLogicLayer(){
		return logicLayer;
	}//getLogicLayer

	public void setLogicLayer(LogicLayer logicLayer){
		this.logicLayer = logicLayer;
	}//setLogicLayer

}//Session