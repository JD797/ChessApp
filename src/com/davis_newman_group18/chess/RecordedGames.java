package com.davis_newman_group18.chess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
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
		
		ArrayAdapter<SavedGame> adapter = new ArrayAdapter<SavedGame>(this,
	              android.R.layout.simple_list_item_1, android.R.id.text1, SavedGame.savedGames);
		
		listView.setAdapter(adapter);		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				SavedGame.game = (SavedGame)listView.getItemAtPosition(position);
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
		
		
	}
	
	
	
}
