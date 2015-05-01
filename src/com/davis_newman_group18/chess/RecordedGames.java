package com.davis_newman_group18.chess;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class RecordedGames extends Activity {
	
	ListView listView;
	Button sortByDate, sortByTitle, replay;
	TextView selectedGameTitle;
	Intent intent;
	
	SavedGame game;
	ArrayAdapter<SavedGame> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recorded_games);
		
		intent = new Intent(this, ChessGame.class);
		listView = (ListView)findViewById(R.id.listView);
		sortByDate = (Button) findViewById(R.id.sortByDate);
		sortByTitle = (Button) findViewById(R.id.sortByTitle);
		replay = (Button) findViewById(R.id.replay);
		selectedGameTitle = (TextView) findViewById(R.id.selectedGameTitle);
		
		readData();
		
		if (SavedGame.savedGames.size() == 0) {
			selectedGameTitle.setText("No games saved");
			sortByDate.setEnabled(false);
			sortByTitle.setEnabled(false);
		} else {
			selectedGameTitle.setText("Select a game");
			sortByDate.setEnabled(true);
			sortByTitle.setEnabled(true);
		}
		
		adapter = new ArrayAdapter<SavedGame>(this,
	              android.R.layout.simple_list_item_1, android.R.id.text1, SavedGame.savedGames);
		
		listView.setAdapter(adapter);		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				SavedGame.game = (SavedGame) listView.getItemAtPosition(position);
				replay.setEnabled(true);
				selectedGameTitle.setText(SavedGame.game.title);
			}	
		});
		
		replay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ChessGame.replayingGame = true;
				startActivity(intent);
			}
		});
		
		sortByDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<SavedGame> games = SavedGame.savedGames;
				for (int i = 1; i < SavedGame.savedGames.size(); i++) {
					for (int j = i; j > 0; j--) {
						SavedGame curr = games.get(j);
						SavedGame prev = games.get(j-1);
						if (curr.calendar.before(prev.calendar)) {
							SavedGame tmp = prev;
							games.set(j-1, curr);
							games.set(j, tmp);
						}
					}
				}		
				adapter.notifyDataSetChanged();
			}
		});
		
		sortByTitle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<SavedGame> games = SavedGame.savedGames;
				for (int i = 1; i < SavedGame.savedGames.size(); i++) {
					for (int j = i; j > 0; j--) {
						SavedGame curr = games.get(j);
						SavedGame prev = games.get(j-1);
						if (curr.title.compareTo(prev.title) < 0) {
							SavedGame tmp = prev;
							games.set(j-1, curr);
							games.set(j, tmp);
						}
					}
				}		
				adapter.notifyDataSetChanged();
			}
		});
			
	}
	
	public void readData() {
		
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(openFileInput("saved_game"));
			ArrayList<SavedGame> savedGames = (ArrayList<SavedGame>)ois.readObject();
			if (savedGames == null)	
				SavedGame.savedGames = new ArrayList<SavedGame>();
			else
				SavedGame.savedGames = savedGames;	
		} catch (Exception e) {
			SavedGame.savedGames = new ArrayList<SavedGame>();
		}
	}
	
	
	
}
