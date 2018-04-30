package com.SeventhInning.model;

import java.util.ArrayList;
import java.util.Random;

import javax.inject.Inject;

import com.SeventhInning.controller.HostSessionHandler;
import com.SeventhInning.controller.PlayerSessionHandler;

public class Game {

	private Host host;
	private int number=1;
	private boolean addTofirst = true;
	private volatile boolean t1 = false, t2 = false, waiting = true;
	private GameTimer timer = new GameTimer();
	private OneVOne uno, dos;
	private Team team1, team2;
	private ArrayList<Inning> innings;
	@Inject
	private PlayerSessionHandler handler;
	@Inject
	private HostSessionHandler hostHandler;

	public void start() {
		for (Inning inning : innings) {
			play(inning);
		}
		end();
	}

	class GameTimer implements Runnable {
		private volatile int timeout;
		private volatile boolean finished = false;

		public GameTimer() {
			this(10);
		}

		public void reset() {
			finished = true;
		}

		public GameTimer(int timeout) {
			this.timeout = timeout;
		}

		@Override
		public void run() {
			try {
				wait(timeout);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private void play(Inning inning) {
hostHandler.sendInningStart(host.getSession(), inning, number);
waitOnHostContinue();

		// One v one question toss up, Winning team goes
		Team playing = askTossup(inning.getHeadToHead());
		hostHandler.sendBattingOrder(host.getSession(), team1, team2);
		waitOnHostContinue();
		while (playing.getStrikes() < 3 && inning.hasQuestion()) {
			haveTeamPlay(inning, playing);
			hostHandler.sendScore(host.getSession(), this, false);
			waitOnHostContinue();
		}
		playing.endGo();
		if (playing.equals(team1)) {
			playing = team2;
		} else {
			playing = team1;
		}
		if (inning.hasQuestion()) {
			while ((playing.getStrikes() < 3 && inning.hasQuestion())) {
				haveTeamPlay(inning, playing);
				hostHandler.sendScore(host.getSession(), this, false);
				waitOnHostContinue();
			}

		}
		playing.endGo();
		hostHandler.sendInningOver(host.getSession());
		number++;
		waitOnHostContinue();
	}

	private void haveTeamPlay(Inning inning, Team playing) {
		Question q = inning.nextQuestion();
		Player current = playing.nextPlayer();
		// Send question to host
		hostHandler.sendQuestion(host.getSession(), q);
		// send result to host and player
		boolean correct = askQuestion(current, q);
		hostHandler.sendQuestionResponse(host.getSession(), correct, current.getAnswer());
		hostHandler.sendBattingOrder(host.getSession(), team1, team2);
		current.setAnswer(-1);
		if (correct) {
			playing.correct();
			q.setAnswered(true);
			waitOnHostContinue();
		} else {
			playing.incorrect();
		}
	}

	private void waitOnHostContinue() {
		waiting = true;
		while (waiting) {
			// TODO add somthing in Host web socket that allows them to blame out
		}
	}

	class OneVOne implements Runnable {

		// TODO send message to host and wait to continue
		private Player p;
		private Question q;
		private volatile boolean done = false, correct = false;

		public OneVOne(Player p, Question q) {
			super();
			this.p = p;
			this.q = q;

		}

		@Override
		public void run() {
			correct = askQuestion(p, q);
			done = true;
		}

	}

	private Team askTossup(Question tossup) {
		hostHandler.sendQuestion(host.getSession(), tossup);
		Player p1 = team1.nextPlayer();
		Player p2 = team2.nextPlayer();
		uno = new OneVOne(p1, tossup);
		dos = new OneVOne(p2, tossup);
		uno.run();
		dos.run();
		// timeout built into ask question
		while (true) {

			if (uno.correct) {
				hostHandler.tossup(host.getSession(), 1, false);
				return team1;
			}

			if (dos.correct) {
hostHandler.tossup(host.getSession(), 2, false);
				return team2;
			}

			if (uno.done && dos.done) {

				// chooses random team
				Random r = new Random();
				int next = r.nextInt(2);
				int team = 2;
				if (next == 1) {
					team = 1;
					hostHandler.tossup(host.getSession(), team,true);
					return team1;
				}
				hostHandler.tossup(host.getSession(), team,true);
				return team2;
			}

		}

	}

	private boolean askQuestion(Player toAsk, Question q) {
		boolean correct = false;

		timer.run();
		while (toAsk.isNoResponse()) {

			if (timer.finished) {
				toAsk.setActive(false);
				toAsk.setAnswer(-1);
		handler.sendQuestionResult(toAsk.getSession(), false, true);
				timer.reset();
				return false;
			}

		}
		toAsk.setNoResponse(true);
		toAsk.setActive(false);// actually deactivate player
		if (q.getCorrect()==toAsk.getAnswer()) {
			correct = true;
		}
		
		// send message teling them if they got it right or wron
		handler.sendQuestionResult(toAsk.getSession(), correct, false);
		q.getChosen().add(toAsk.getAnswer());
		return correct;
	}

	private void end() {
		hostHandler.sendScore(host.getSession(), this, true);
	}

	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
	}

	public boolean isT1() {
		return t1;
	}

	public void setT1(boolean t1) {
		this.t1 = t1;
	}

	public boolean isT2() {
		return t2;
	}

	public void setT2(boolean t2) {
		this.t2 = t2;
	}

	public GameTimer getTimer() {
		return timer;
	}

	public void setTimer(GameTimer timer) {
		this.timer = timer;
	}

	public OneVOne getUno() {
		return uno;
	}

	public void setUno(OneVOne uno) {
		this.uno = uno;
	}

	public OneVOne getDos() {
		return dos;
	}

	public void setDos(OneVOne dos) {
		this.dos = dos;
	}

	public Team getTeam1() {
		return team1;
	}

	public void setTeam1(Team team1) {
		this.team1 = team1;
	}

	public Team getTeam2() {
		return team2;
	}

	public void setTeam2(Team team2) {
		this.team2 = team2;
	}

	public ArrayList<Inning> getInnings() {
		return innings;
	}

	public void setInnings(ArrayList<Inning> innings) {
		this.innings = innings;
	}

	public PlayerSessionHandler getHandler() {
		return handler;
	}

	public void setHandler(PlayerSessionHandler handler) {
		this.handler = handler;
	}

	public HostSessionHandler getHostHandler() {
		return hostHandler;
	}

	public void setHostHandler(HostSessionHandler hostHandler) {
		this.hostHandler = hostHandler;
	}

	public boolean isAddTofirst() {
		return addTofirst;
	}

	public void setAddTofirst(boolean addTofirst) {
		this.addTofirst = addTofirst;
	}

	public boolean isWaiting() {
		return waiting;
	}

	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}
	

}
