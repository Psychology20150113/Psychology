package com.dcy.psychology.gsonbean;

import java.util.ArrayList;

public class GameFishBean {
	private ArrayList<Fish> fish;
	private ArrayList<String> colors;
	
	public ArrayList<Fish> getFish() {
		return fish;
	}
	public void setFish(ArrayList<Fish> fish) {
		this.fish = fish;
	}
	public ArrayList<String> getColors() {
		return colors;
	}
	public void setColors(ArrayList<String> colors) {
		this.colors = colors;
	}

	public class Fish{
		private int type;
		private int pointx;
		private int pointy;
		private int width;
		private int height;
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public int getPointx() {
			return pointx;
		}
		public void setPointx(int pointx) {
			this.pointx = pointx;
		}
		public int getPointy() {
			return pointy;
		}
		public void setPointy(int pointy) {
			this.pointy = pointy;
		}
		public int getWidth() {
			return width;
		}
		public void setWidth(int width) {
			this.width = width;
		}
		public int getHeight() {
			return height;
		}
		public void setHeight(int height) {
			this.height = height;
		}
	}
	
}
