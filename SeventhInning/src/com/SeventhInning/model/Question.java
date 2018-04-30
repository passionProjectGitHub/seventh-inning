package com.SeventhInning.model;

import java.util.ArrayList;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;

public class Question implements Sendable {
	private String question;
	private ArrayList<String> answers = new ArrayList<String>();
	private int correct;
	private ArrayList<Integer> chosen;
	public boolean answered = false,tossup =false;

	public Question(String question, ArrayList<String> answers, int correct) {
		this.question = question;
		this.answers = answers;
		this.correct = correct;
	}

	@Override
	public JsonObject asMessage() {
		JsonProvider provider = JsonProvider.provider();
		JsonObjectBuilder message = provider.createObjectBuilder();
		// Add Question
		message.add("Question", question);

		// Add answer choices to Array
		JsonArrayBuilder answers = provider.createArrayBuilder();
		for (String answer : getAnswers()) {
			answers.add(answer);
		}
		message.add("Answers", answers);
		// Have chosen represented here

		JsonArrayBuilder chosen = provider.createArrayBuilder();
		for (Integer picked : getChosen()) {
			chosen.add(picked);
		}
		message.add("Chosen", chosen);
		message.add("Tossup", "tossup");
		return message.build();
	}

	public ArrayList<String> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<String> answers) {
		this.answers = answers;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getCorrect() {
		return correct;
	}

	public void setCorrect(int correct) {
		this.correct = correct;
	}

	public boolean isAnswered() {
		return answered;
	}

	public void setAnswered(boolean answered) {
		this.answered = answered;
	}

	public ArrayList<Integer> getChosen() {
		return chosen;
	}

	public void setChosen(ArrayList<Integer> chosen) {
		this.chosen = chosen;
	}

	
	
}
