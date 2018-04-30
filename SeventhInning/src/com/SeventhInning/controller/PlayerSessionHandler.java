package com.SeventhInning.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;

import com.SeventhInning.model.Player;
import com.SeventhInning.model.Question;



@ApplicationScoped
public class PlayerSessionHandler {
	private final Set<Session> sessions = new HashSet<>();
	   public void addSession(Session session) {
	        sessions.add(session);
	      
	    }

	    public void removeSession(Session session) {
	        sessions.remove(session);
	    }

	    private void sendToAllConnectedSessions(JsonObject message) {
	        for (Session session : sessions) {
	            sendToSession(session, message);
	        }
	    }
	 
	
	    public void updateActivation(Session s, boolean active) {
			JsonProvider provider = JsonProvider.provider();
			JsonObject message = provider.createObjectBuilder().add("active", active).build();
	    	sendToSession(s, message);
	    }
	    
	    public void sendJoinConfirmed(Session s) {
			JsonProvider provider = JsonProvider.provider();
			JsonObject message = provider.createObjectBuilder().add("joined", "yay").build();
	    	sendToSession(s, message);
	    }
	    
	    public void sendGameEnded(Session s) {
	    	JsonProvider provider = JsonProvider.provider();
			JsonObject message = provider.createObjectBuilder().add("ended", "boi").build();
	    	sendToSession(s, message);
	    }
	    
	    public void sendQuestionResult(Session s,boolean correct,boolean timeout) {
	      	JsonProvider provider = JsonProvider.provider();
	    			JsonObject message = provider.createObjectBuilder().add("correct", correct).add("timeout", timeout).build();
	    	    	sendToSession(s, message);	
	    	    	try {
						wait(1800);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	    	    	updateActivation(s, false);
	    }
	    


	    private void sendToSession(Session session, JsonObject message) {
	        try {
	            session.getBasicRemote().sendText(message.toString());
	        } catch (IOException ex) {
	            sessions.remove(session);
	            Logger.getLogger(PlayerSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
	        }
	    }
}
