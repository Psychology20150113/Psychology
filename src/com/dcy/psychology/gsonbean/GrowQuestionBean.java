package com.dcy.psychology.gsonbean;

import java.io.Serializable;
import java.util.ArrayList;

public class GrowQuestionBean implements Serializable{
	private ArrayList<OptionBean> question;
	private ArrayList<AnswerBean> answer;
	
	public ArrayList<OptionBean> getQuestion() {
		return question;
	}

	public void setQuestion(ArrayList<OptionBean> question) {
		this.question = question;
	}

	public ArrayList<AnswerBean> getAnswer() {
		return answer;
	}

	public void setAnswer(ArrayList<AnswerBean> answer) {
		this.answer = answer;
	}

	public class OptionBean implements Serializable{
		private int id;
		private String title;
		private ArrayList<String> option;
		private ArrayList<Integer> point;
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public ArrayList<String> getOption() {
			return option;
		}
		public void setOption(ArrayList<String> option) {
			this.option = option;
		}
		public ArrayList<Integer> getPoint() {
			return point;
		}
		public void setPoint(ArrayList<Integer> point) {
			this.point = point;
		}
	}
	
	public class AnswerBean implements Serializable{
		private int startPoint;
		private int endPoint;
		private String comment;
		public int getStartPoint() {
			return startPoint;
		}
		public void setStartPoint(int startPoint) {
			this.startPoint = startPoint;
		}
		public int getEndPoint() {
			return endPoint;
		}
		public void setEndPoint(int endPoint) {
			this.endPoint = endPoint;
		}
		public String getComment() {
			return comment;
		}
		public void setComment(String comment) {
			this.comment = comment;
		}
	}
}
