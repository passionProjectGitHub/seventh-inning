package com.SeventhInning.model;

import java.util.ArrayList;

public class Inning {
	private String name;
	private String description;
	private ArrayList<Question> questions;
	private Question headToHead;
	private int question = 0;

	public Question nextQuestion() {
		if (questions.get(question).answered) {
			question++;
		}

		return questions.get(question);

	}

	// Test
	public boolean hasQuestion() {
		if (!(question < questions.size() - 1)) {
			return false;
		}

		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(ArrayList<Question> questions) {
		this.questions = questions;
	}

	public Question getHeadToHead() {
		return headToHead;
	}

	public void setHeadToHead(Question headToHead) {
		this.headToHead = headToHead;
	}

	public int getQuestion() {
		return question;
	}

	public void setQuestion(int question) {
		this.question = question;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

}
