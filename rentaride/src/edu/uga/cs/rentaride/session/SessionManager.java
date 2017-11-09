package edu.uga.cs.rentaride.session;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.User;
import edu.uga.cs.rentaride.persistence.impl.DbUtils;

public class SessionManager{
    private static Map<String, Session> sessions;
    private static Map<String, Session> loggedIn;
    
    static{
	sessions = new HashMap<String, Session>();
	loggedIn = new HashMap<String, Session>();
    }

    public static Session createSession() throws RARException{
	Connection conn = null;
	Session s = null;

	try{
	    conn = DbUtils.connect();
	} catch(Exception seq){
	    throw new RARException("SessionManager.login: Unable to get a database connection");
	}//try-catch

	s = new Session(conn);
	
	return s;
    }//createSession

    public static String storeSession(Session session) throws RARException{
	User user = session.getUser();

	if(loggedIn.containsKey(user.getUserName())){
	    Session qs = loggedIn.get(user.getUserName());
	    qs.setUser(user);
	    return qs.getSessionId();
	}//if
	    String ssid = secureHash("RAR" + System.nanoTime());
	    session.setSessionId(ssid);

	    sessions.put(ssid, session);
	    loggedIn.put(user.getUserName(), session);
	    session.start();
	    return ssid;
    }//storeSession

    public static void logout(Session s) throws RARException{
	s.setExpiration(new Date());
	s.interrupt();
	removeSession(s);
    }//logout

    public static void logout(String ssid) throws RARException{
	Session s = getSessionById(ssid);
	s.setExpiration(new Date());
	s.interrupt();
	removeSession(s);
    }//logout

    protected static void removeSession(Session s) throws RARException{
	try{
	    s.getConnection().close();
	} catch(SQLException sqe){
	    throw new RARException("SessionManager.removeSession: Cannot close connection");
	}//try-catch
	sessions.remove(s.getSessionId());
	loggedIn.remove(s.getUser().getUserName());
    }//removeSession

    public static Session getSessionById(String ssid){
	return sessions.get(ssid);
    }//getSessionById

    public static String secureHash(String input) throws RARException {
	StringBuilder output = new StringBuilder();
	try{
	    MessageDigest md = MessageDigest.getInstance("SHA");
	    md.update(input.getBytes());
	    byte[] digest = md.digest();
	    for(int i=0; i<digest.length; ++i){
		String hex = Integer.toHexString(digest[i]);
		if(hex.length()==1){
		    hex = "0"+hex;
		}//if
		hex = hex.substring(hex.length()-2);
		output.append(hex);
	    }//for
	} catch(Exception e){
	    throw new RARException("SessionManager.secureHash: Invalid Encryption Algorithm");
	}//try-catch
	return output.toString();
    }//secureHash

    //LOGGER NEXT!!!

}//SessionManager