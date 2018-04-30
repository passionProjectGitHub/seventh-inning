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
import com.SeventhInning.model.Player;

@ApplicationScoped
@ServerEndpoint("/play")
public class PlayerWebSocket {

	@Inject
	private PlayerSessionHandler sessionHandler;
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
			String game = jsonMessage.getString("gameCode");
			if (games.getGames().containsKey(game)) {
				Game clientGame = games.getGames().get(game);
				
				//TODO join
				if(jsonMessage.getString("join").equals("join")&&!(session.getUserProperties().containsKey("player"))) {
					Player player = new Player(session);
					player.setName(jsonMessage.getString("name"));
					if(clientGame.isAddTofirst()) {
						clientGame.getTeam1().getBatting().add(player);
						clientGame.setAddTofirst(false);
					}else {
						clientGame.getTeam2().getBatting().add(player);
						clientGame.setAddTofirst(true);
					}
					session.getUserProperties().put("player", player);
				}
				else if(session.getUserProperties().containsKey("player")) {
					Player player =(Player)session.getUserProperties().get("player");
				if(player.isActive()) {
					player.setAnswer(jsonMessage.getInt("answer"));
				}
				}

			}
		}

	}

}
