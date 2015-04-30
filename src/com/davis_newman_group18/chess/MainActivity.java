package com.davis_newman_group18.chess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
	Button playButton;
	Button playbackButton;
	
	Intent newGame;
	Intent recordedGames;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		playButton = (Button) findViewById(R.id.playButton);
		playbackButton = (Button) findViewById(R.id.playbackButton);
		newGame = new Intent(this, ChessGame.class);
		recordedGames = new Intent(this, RecordedGames.class);
		
		readData();
		
		playButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ChessGame.replayingGame = false;
				startActivity(newGame);
			}
		});
		
		playbackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(recordedGames);
			}
		});
		
	}
	
	public void readData() {
		
		InputStream is;
		ObjectInputStream ois;
		is = getResources().openRawResource(R.raw.saved_games);
		
		try {
			ois = new ObjectInputStream(is);
			ArrayList<SavedGame> savedGames = (ArrayList<SavedGame>)ois.readObject();
			// TODO is savedGames null here if the file is blank? need to test
			if (savedGames == null)	
				SavedGame.savedGames = new ArrayList<SavedGame>();
			else
				SavedGame.savedGames = savedGames;	
		} catch (Exception e) {
			SavedGame.savedGames = new ArrayList<SavedGame>();
		}
	}
	
}
