package com.SeventhInning.model;

public class Turn {
	private int strikes;
	private Team team;
	private Inning inning;

	public void Strike(Team team, Inning inning) {

	}

	public void takeTurn() {

		while (strikes < 3) {
			if (!askQuestion(team.nextPlayer())) {
				strikes++;
			} else {
				team.correct();
			}

		}
		endTurn();
	}

	private boolean askQuestion(Player toAsk) {

		return false;
	}

	private void endTurn() {

	}

}
