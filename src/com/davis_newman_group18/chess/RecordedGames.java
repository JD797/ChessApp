package com.davis_newman_group18.chess;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class RecordedGames extends Activity {
	
	ListView listView;
	Button sortByDate, sortByTitle, replay;
	Intent intent;
	
	SavedGame game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recorded_games);
		
		intent = new Intent(this, ChessGame.class);
		listView = (ListView)findViewById(R.id.listView);
		sortByDate = (Button) findViewById(R.id.sortByDate);
		sortByTitle = (Button) findViewById(R.id.sortByTitle);
		replay = (Button) findViewById(R.id.replay);
		
		ArrayAdapter<SavedGame> adapter = new ArrayAdapter<SavedGame>(this,
	              android.R.layout.simple_list_item_1, android.R.id.text1, SavedGame.savedGames);
		
		listView.setAdapter(adapter);		
		listView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				SavedGame.game = (SavedGame)listView.getSelectedItem();
				replay.setEnabled(true);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				replay.setEnabled(false);
			}
			
		});
		
		replay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ChessGame.replayingGame = true;
				startActivity(intent);
			}
		});
		
		
	}
	
	
	
}
