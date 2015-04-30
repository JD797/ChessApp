package com.davis_newman_group18.chess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class ChessGame extends Activity {
	
	static boolean replayingGame = false;
	
	Button undo, ai, draw, resign;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chess_game);
		
		intent = new Intent(this, MainActivity.class);
		
		undo = (Button) findViewById(R.id.undo);
		ai = (Button) findViewById(R.id.ai);
		draw = (Button) findViewById(R.id.draw);
		resign = (Button) findViewById(R.id.resign);
		
		/* FOR TESTING: CURRENTLY WORKS
		try {
			SavedGame savedGame = new SavedGame("test2");
			SavedGame.savedGames.add(savedGame);
			SavedGame savedGame2 = new SavedGame("test");
			SavedGame.savedGames.add(savedGame2);
			writeData();
		} catch (Exception e) { }
		*/ 
		
		Log.v("DIRECTORY", getApplicationInfo().dataDir);
	}
	
	public void writeData() throws Exception {
		//TODO make sure this works
		File file = new File("android.resource://com.davis_newman_group18/raw/saved_games");
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		oos.writeObject(SavedGame.savedGames);
		oos.close();
	}
}
