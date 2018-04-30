package com.SeventhInning.model;

import javax.websocket.Session;

public class Client {
private Session session;

public Client(Session sess) {
	session = sess;
}

public Session getSession() {
	return session;
}

public void setSession(Session session) {
	this.session = session;
}
	

}
