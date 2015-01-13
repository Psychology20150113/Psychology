package com.dcy.psychology.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.dcy.psychology.util.ThoughtReadingUtils.QuestionType;

public class QuestionModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6587128995661957125L;
	private int questionId;
	private QuestionType questionType;
	private String questionTitle;
	private ArrayList<String> optionList;
	private String answer;
	private String explain;
	private ArrayList<Integer> point;
	
	public ArrayList<Integer> getPoint() {
		return point;
	}

	public void setPoint(ArrayList<Integer> point) {
		this.point = point;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public QuestionType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public ArrayList<String> getOptionList() {
		return optionList;
	}

	public void setOptionList(ArrayList<String> optionList) {
		this.optionList = optionList;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}
}
