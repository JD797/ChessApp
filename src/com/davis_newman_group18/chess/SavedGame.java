package com.davis_newman_group18.chess;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SavedGame {
	
	static ArrayList<SavedGame> savedGames;
	static SavedGame game = null;
	
	String title;
	String date;
	Calendar calendar;
	
	public SavedGame(String title) {
		this.title = title;
		calendar = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy-HH:mm");
		date = df.format(calendar.getTime());
	}
	
	public String toString() {
		return title + "\t" + date;
	}
	
	//TODO will need a compareTo thing
	
	
	

}
