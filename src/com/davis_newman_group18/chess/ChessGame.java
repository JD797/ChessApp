package com.davis_newman_group18.chess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class ChessGame extends Activity {
	
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
		
		
	}
}
