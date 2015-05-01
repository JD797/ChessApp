package com.davis_newman_group18.chess;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class SavedGame implements Serializable {
		
	private static final long serialVersionUID = 1L;
	
	static ArrayList<SavedGame> savedGames;
	static SavedGame game = null;
	
	String title;
	String date;
	Calendar calendar;
	LinkedList<Coordinate> movesMade;
	
	public SavedGame(String title, LinkedList<Coordinate> moves) {
		this.title = title;
		movesMade = moves;
		calendar = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("MM/dd/yy-HH:mm");
		date = df.format(calendar.getTime());
	}
	
	public String toString() {
		return title + " - " + date;
	}
	
	//TODO will need a compareTo thing
	
	
	

}
