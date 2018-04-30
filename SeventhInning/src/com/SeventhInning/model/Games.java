package com.SeventhInning.model;

import java.util.HashMap;

import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class Games {
private HashMap<Integer,Game>  games= new HashMap();

public HashMap<Integer, Game> getGames() {
	return games;
}

public void setGames(HashMap<Integer, Game> games) {
	this.games = games;
}


	
	
	
}
