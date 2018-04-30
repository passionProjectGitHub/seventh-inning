package com.SeventhInning.model;

import javax.websocket.Session;

public class Player extends Client {
private boolean active =false,noResponse = true;
private String name="";
private int answer = -1 ;
	public Player(Session sess) {
		super(sess);
		// TODO Auto-generated constructor stub
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public int getAnswer() {
		return answer;
	}
	public void setAnswer(int answer) {
		this.answer = answer;
	}
	public boolean isNoResponse() {
		return noResponse;
	}
	public void setNoResponse(boolean noResponse) {
		this.noResponse = noResponse;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
