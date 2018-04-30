package com.SeventhInning.controller;

import java.io.StringReader;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.SeventhInning.model.Game;
import com.SeventhInning.model.Games;
@ApplicationScoped
@ServerEndpoint("/host")
public class HostWebSocket {

 
	@Inject
	private HostSessionHandler sessionHandler;
	@Inject
	private Games games;

	@OnOpen
	public void open(Session session) {
		sessionHandler.addSession(session);
	}

	@OnClose
	public void close(Session session) {
		sessionHandler.removeSession(session);
	}

	@OnError
	public void onError(Throwable error) {
		System.out.println(error);
	}

	@OnMessage
	public void handleMessage(String message, Session session) {
		System.out.println("Message:" + message);

		
		
		try (JsonReader reader = Json.createReader(new StringReader(message))) {
			JsonObject jsonMessage = reader.readObject();
		
			//TODO create game
			if(jsonMessage.getString("create").equals("create")) {
			games.getGames().put(0, new Game());
			}
		
			
			//TODO continue
			else if(jsonMessage.getString("continue").equals("continue")) {
				
				try {
					if(jsonMessage.getString("start").equals("start")) {
					games.getGames().get((jsonMessage.getInt("gameCode"))).start();
					}else {
					games.getGames().get(jsonMessage.getInt("gameCode")).setWaiting(false);
					}
					}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			else if(jsonMessage.getString("end").equals("end")) {
				
				try {
					games.getGames().remove(jsonMessage.getInt("gameCode"));
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		
		}

	}
	
}
