package com.davis_newman_group18.chess;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class SavedGame {
	
	static ArrayList<SavedGame> savedGames;
	static SavedGame game = null;
	
	String title;
	String date;
	Calendar calendar;
	LinkedList<Coordinate> movesMade; // Will probably be changed from String
	
	public SavedGame(String title) {
		this.title = title;
		calendar = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("MM/dd/yy-HH:mm");
		date = df.format(calendar.getTime());
	}
	
	public String toString() {
		return title + " - " + date;
	}
	
	//TODO will need a compareTo thing
	
	
	

}
