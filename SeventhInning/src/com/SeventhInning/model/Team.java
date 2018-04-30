package com.SeventhInning.model;

import java.util.ArrayList;
import java.util.Collections;

public class Team {

	private int points;
	private int totalBases;
	private int bases;
	private int strikes; // TODO add strikes
	private String name;
	private ArrayList<Player> batting;
	private ArrayList<Player> gone;

	public Team(ArrayList<Player> players, String name) {
		Collections.shuffle(players);
		batting.addAll(players);
		batting.addAll(players);
	}

	public void endGo() {
		bases = 0;
		strikes = 0;
	}

	public void correct() {
		bases++;
		totalBases++;
		if (bases > 3) {
			points++;
		}

	}

	public void incorrect() {
		strikes++;
	}

	

	public Player nextPlayer() {

		if (batting.isEmpty()) {
			batting.addAll(gone);
		}
		batting.remove(0);
		return batting.get(0);

	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getTotalBases() {
		return totalBases;
	}

	public void setTotalBases(int totalBases) {
		this.totalBases = totalBases;
	}

	public int getBases() {
		return bases;
	}

	public void setBases(int bases) {
		this.bases = bases;
	}

	public int getStrikes() {
		return strikes;
	}

	public void setStrikes(int strikes) {
		this.strikes = strikes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Player> getBatting() {
		return batting;
	}

	public void setBatting(ArrayList<Player> batting) {
		this.batting = batting;
	}

	public ArrayList<Player> getGone() {
		return gone;
	}

	public void setGone(ArrayList<Player> gone) {
		this.gone = gone;
	}

}
