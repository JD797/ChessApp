package com.davis_newman_group18.chess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class ChessGame extends Activity {
	
	static boolean replayingGame = false;
	
	GridLayout grid;
	ChessboardSquare[][] chessboard;
	Button undo, ai, draw, resign;
	TextView turn;
	Intent intent;
	
	boolean pieceSelected = false;
	Coordinate currentCoordinate;
	LinkedList<Coordinate> movesMade;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chess_game);
		
		intent = new Intent(this, MainActivity.class);
		grid = (GridLayout) findViewById(R.id.gridLayout);
		undo = (Button) findViewById(R.id.undo);
		ai = (Button) findViewById(R.id.ai);
		draw = (Button) findViewById(R.id.draw);
		resign = (Button) findViewById(R.id.resign);
		turn = (TextView) findViewById(R.id.turn);
		turn.setText("White Turn");
		
		movesMade = new LinkedList<Coordinate>();
		chessboard = new ChessboardSquare[8][8];
		
		/* FOR TESTING: CURRENTLY WORKS
		try {
			SavedGame savedGame = new SavedGame("test2");
			SavedGame.savedGames.add(savedGame);
			SavedGame savedGame2 = new SavedGame("test");
			SavedGame.savedGames.add(savedGame2);
			writeData();
		} catch (Exception e) { }
		
		Log.v("DIRECTORY", getApplicationInfo().dataDir);
		*/ 
		
		initializeBoard();	
		
	}
	
	public void initializeBoard() {
		
		//LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				//ImageView square = (ImageView) inflater.inflate(R.layout.chessboard_square, null);
				ChessboardSquare square = new ChessboardSquare(this, new Coordinate(7-row, col));
				if ((row+col) % 2 == 0)
					square.setBackgroundColor(Color.WHITE);
				else
					square.setBackgroundColor(Color.BLUE);
				LayoutParams params = new LayoutParams(GridLayout.spec(row), GridLayout.spec(col));
				params.width = 40;
				params.height = 40;
				params.setGravity(Gravity.CENTER);
				square.setLayoutParams(params);
				square.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ChessboardSquare square = (ChessboardSquare)v;
						movePiece(square.coordinate);
					}
				});
				
				grid.addView(square, params);
				chessboard[7-row][col] = square;
			}
		}
		
		// Set row 0 pieces
		chessboard[0][0].setImageResource(R.drawable.wht_rook);
		chessboard[0][1].setImageResource(R.drawable.wht_knight);
		chessboard[0][2].setImageResource(R.drawable.wht_bishop);
		chessboard[0][3].setImageResource(R.drawable.wht_queen);
		chessboard[0][4].setImageResource(R.drawable.wht_king);
		chessboard[0][5].setImageResource(R.drawable.wht_bishop);
		chessboard[0][6].setImageResource(R.drawable.wht_knight);
		chessboard[0][7].setImageResource(R.drawable.wht_rook);
		
		// Set row 1 pawns
		for (int col = 0; col < 8; col++) {
			ImageView square = chessboard[1][col];
			square.setImageResource(R.drawable.wht_pawn);
		}
		
		// Set row 6 pawns
		for (int col = 0; col < 8; col++) {
			ImageView square = chessboard[6][col];
			square.setImageResource(R.drawable.blk_pawn);
		}
		
		// Set row 7 pieces
		chessboard[7][0].setImageResource(R.drawable.blk_rook);
		chessboard[7][1].setImageResource(R.drawable.blk_knight);
		chessboard[7][2].setImageResource(R.drawable.blk_bishop);
		chessboard[7][3].setImageResource(R.drawable.blk_queen);
		chessboard[7][4].setImageResource(R.drawable.blk_king);
		chessboard[7][5].setImageResource(R.drawable.blk_bishop);
		chessboard[7][6].setImageResource(R.drawable.blk_knight);
		chessboard[7][7].setImageResource(R.drawable.blk_rook);
	}
	
	public void movePiece(Coordinate coordinate) {
		pieceSelected = !pieceSelected;
		ChessboardSquare square;
		if (pieceSelected) {
			square = chessboard[coordinate.row][coordinate.col];
			square.setBackgroundColor(Color.GREEN);
			currentCoordinate = coordinate;
			movesMade.add(coordinate);
		} else {
			
			// if invalid move, make toast then delete last item in movesMade,
			// else make appropriate move and change images appropriately using currentCoordinate and coordinate to access the ChessboardSquares
			// then add coordinate to movesMade
			
			square = chessboard[currentCoordinate.row][currentCoordinate.col];
			if ((currentCoordinate.row + currentCoordinate.col) % 2 == 0)
				square.setBackgroundColor(Color.BLUE);
			else
				square.setBackgroundColor(Color.WHITE);
		}
		Log.v("MOVE", coordinate.row + ", " + coordinate.col);
	}
	
	public void writeData() throws Exception {
		//TODO make sure this works
		File file = new File("android.resource://com.davis_newman_group18/raw/saved_games");
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		oos.writeObject(SavedGame.savedGames);
		oos.close();
	}
	
}
