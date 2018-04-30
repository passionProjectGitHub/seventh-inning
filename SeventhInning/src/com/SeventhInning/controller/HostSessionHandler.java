package com.SeventhInning.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;

import com.SeventhInning.model.Game;
import com.SeventhInning.model.Inning;
import com.SeventhInning.model.Player;
import com.SeventhInning.model.Question;
import com.SeventhInning.model.Team;

@ApplicationScoped
public class HostSessionHandler {
	private final HashSet<Session> sessions = new HashSet<>();

	
	
	public void sendQuestion(Session session, Question question) {
		sendToSession(session, question.asMessage());
	}
	
	
	   public void addSession(Session session) {
	        sessions.add(session);
	      
	    }

	    public void removeSession(Session session) {
	        sessions.remove(session);
	    }

	    public void sendScore(Session s,Game g,boolean fin) {
	    	JsonProvider provider = JsonProvider.provider();
			JsonObjectBuilder message = provider.createObjectBuilder();
			message.add("team1Score", g.getTeam1().getPoints());
			message.add("team1Bases", g.getTeam1().getTotalBases());
			message.add("team2Score", g.getTeam1().getPoints());
			message.add("team2Bases", g.getTeam1().getTotalBases());
			message.add("final", fin);
	    	sendToSession(s, message.build());
	    }
	    
	    
	    public void sendQuestionResponse(Session s,boolean correct, int chosen) {
	    	JsonProvider provider = JsonProvider.provider();
				JsonObjectBuilder message = provider.createObjectBuilder();
				message.add("correct", correct);
				message.add("chosen", chosen);
				sendToSession(s, message.build());
	    }
	    
	    public void tossup(Session s,int team,boolean nobody) {
	     	JsonProvider provider = JsonProvider.provider();
			JsonObjectBuilder message = provider.createObjectBuilder();
			message.add("nobody", nobody);
			message.add("team", team);
			sendToSession(s, message.build());
	    }
	    
	    public void sendBattingOrder(Session s,Team t1,Team t2) {
	    	JsonProvider provider = JsonProvider.provider();
				JsonObjectBuilder message = provider.createObjectBuilder();
				JsonArrayBuilder builder1 = provider.createArrayBuilder();
				
				for(Player p:t1.getBatting()) {
					builder1.add(p.getName());
				}
				message.add("team1", builder1);
	JsonArrayBuilder builder2 = provider.createArrayBuilder();
				
				for(Player p:t2.getBatting()) {
					builder2.add(p.getName());
				}
				message.add("team2", builder2);
				
				sendToSession(s, message.build());		
	    }
	    
	    
	    
	    
	    public void sendInningOver(Session s) {
	    	JsonProvider provider = JsonProvider.provider();
			JsonObject message = provider.createObjectBuilder().add("over", "it over").build();
	    	sendToSession(s, message);
	    }
	    
	    public void sendInningStart(Session s,Inning inning,int number) {
	    	JsonProvider provider = JsonProvider.provider();
			JsonObjectBuilder message = provider.createObjectBuilder();
	    	message.add("Name",inning.getName());
	    	message.add("Description",inning.getDescription());
	    	message.add("Number", number);
			sendToSession(s, message.build());
	    }
	    
	    public void sendGameStart(Session s) {
	    	JsonProvider provider = JsonProvider.provider();
			JsonObject message = provider.createObjectBuilder().add("starting", ":)").build();
			sendToSession(s, message);
	    }
	    
	    
	    
	    private void sendToAllConnectedSessions(JsonObject message) {
	        for (Session session : sessions) {
	            sendToSession(session, message);
	        }
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
