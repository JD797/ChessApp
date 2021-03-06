package com.davis_newman_group18.chess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChessGame extends Activity {
	
	static boolean replayingGame = false;
	
	GridLayout grid;
	ChessboardSquare[][] chessboardDisplay;
	Button undo, ai, draw, resign;
	Builder drawDialog;
	Builder saveDialog;
	TextView turn;
	EditText input;
	Intent intent;
	
	boolean pieceSelected = false;
	boolean whiteTurn = true;
	Coordinate fromCoordinate;
	Coordinate toCoordinate;
	LinkedList<Coordinate> movesMade;
	ListIterator<Coordinate> iterator;
	
	boolean drawProposed = false;
	boolean validPlayMade = false;
	static int enPassantCounter;
	ChessBoard board;
	ChessPiece[][] chessboard;
	
	ChessPiece lastMovedPiece;
	ChessBoard prevGameState;

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
		
		enPassantCounter = 2;
		movesMade = new LinkedList<Coordinate>();
		chessboardDisplay = new ChessboardSquare[8][8];
		
		if (replayingGame) {
			undo.setVisibility(View.INVISIBLE);
			resign.setVisibility(View.INVISIBLE);
			ai.setText("Next");
			draw.setText("Exit");
			movesMade = SavedGame.game.movesMade;
			iterator = movesMade.listIterator();
			
			ai.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (iterator.hasNext())
						movePiece(iterator.next());
					if (iterator.hasNext())
						movePiece(iterator.next());
					
					if (!iterator.hasNext()) {
						ai.setEnabled(false);
						Toast.makeText(getApplicationContext(), "End of recording", Toast.LENGTH_LONG).show();
					}
				}
			});
			
			draw.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startActivity(intent);
					finish();
				}
			});
			
		} else {
			

			undo.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Boolean undo = false;
					if(prevGameState == null) {
						undo = false;
					} else {
						lastMovedPiece.game = prevGameState;
						undo = true;
					}
					
					// if can undo move, get new pointers to board and board array, then re-draw move
					if (undo) {
						board = lastMovedPiece.game;
						chessboard = board.getBoard();
						validPlayMade = true;
						drawProposed = false;
						prevGameState = null;
						whiteTurn = !whiteTurn;
						movesMade.removeLast();
						movesMade.removeLast();
						updateBoard();
					} else {
						Toast.makeText(getApplicationContext(), "Cannot undo", Toast.LENGTH_SHORT).show();
					}
					
				}
			});
			

			ai.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ai.setEnabled(false);
					Coordinate[] coords;
					if (whiteTurn) {
						coords = board.aiMove('w');
					} else {
						coords = board.aiMove('b');
					}
					if (coords != null) {
						pieceSelected = true;
						fromCoordinate = coords[0];
						movePiece(coords[1]);
					}
					ai.setEnabled(true);
				}
			});
			
			draw.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					drawProposed = true;
					Toast.makeText(getApplicationContext(), "Draw proposed, now please make a valid move", Toast.LENGTH_SHORT).show();
				}
			});
			
			resign.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (whiteTurn)
						Toast.makeText(getApplicationContext(), "White has resigned! Black wins!", Toast.LENGTH_LONG).show();
					else
						Toast.makeText(getApplicationContext(), "Black has resigned! White wins!", Toast.LENGTH_LONG).show();
					endGame();
				}
			});
			
			drawDialog = new AlertDialog.Builder(this)
		    .setTitle("Draw proposed")
		    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	Toast.makeText(getApplicationContext(), "Draw confirmed", Toast.LENGTH_LONG).show();
		        	endGame();
		        }
		     })
		    .setNegativeButton("No", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            drawProposed = false;
		            drawDialog.setMessage("");
		        }
		     })
		    .setIcon(android.R.drawable.ic_dialog_alert);
			
			saveDialog = new AlertDialog.Builder(this)
		    .setTitle("Save Game")
		    .setMessage("Would you like to save a recording of this game?")
		    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	setSaveTitle();
		        }
		     })
		    .setNegativeButton("No", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	startActivity(intent);
	        		finish();
		        }
		     })
		    .setIcon(android.R.drawable.ic_dialog_alert);
		}
		
		initializeBoard();
		startGame();
		
	}
	
	public void setSaveTitle() {
		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.create();
	    final EditText input = new EditText(this);
	    input.setHint("Save title");
	    alertDialog.setTitle("Enter Save Title");
	    alertDialog.setView(input);
	    alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	SavedGame save = new SavedGame(input.getText().toString(), movesMade);
	        	SavedGame.savedGames.add(save);
	        	try {
	        		writeData();
	        	} catch (Exception e) { }
	        	startActivity(intent);
        		finish();
	        }
	     });
	    
	     alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	startActivity(intent);
        		finish();
	        }
	     });
	    alertDialog.show();
	}
	
	public void startGame() {
		board = new ChessBoard();
		chessboard = board.getBoard();
	}
	
	public void movePiece(Coordinate coordinate) {
		
		
		ChessboardSquare square;
		
		if (!pieceSelected) {
			square = chessboardDisplay[coordinate.row][coordinate.col];
			ChessPiece piece = chessboard[coordinate.row][coordinate.col];
			if (piece == null || (piece.color == 'w' && !whiteTurn) || (piece.color == 'b' && whiteTurn)) return;
			square.setBackgroundColor(Color.GREEN);
			fromCoordinate = coordinate;
			pieceSelected = true;
		} else {
			
			// if invalid move, make toast else make appropriate move and change images appropriately
			// then add fromCoordinate and toCoordinate to movesMade
			
			pieceSelected = false;
			toCoordinate = coordinate;
			square = chessboardDisplay[fromCoordinate.row][fromCoordinate.col];
			if ((fromCoordinate.row + fromCoordinate.col) % 2 == 0)
				square.setBackgroundColor(Color.BLUE);
			else
				square.setBackgroundColor(Color.WHITE);
			if (coordinate.equals(fromCoordinate)) {
				fromCoordinate = null;
				toCoordinate = null;
				return;
			}
			
			try {		
				ChessPiece piece = chessboard[fromCoordinate.row][fromCoordinate.col];
				prevGameState = piece.game.deepClone();
				piece.move(toCoordinate.row, toCoordinate.col);
				
				lastMovedPiece = piece;
				
				if (!replayingGame) {
					movesMade.add(fromCoordinate);
					movesMade.add(toCoordinate);
				}
				
				if (!whiteTurn) {
					if (board.playerIsInCheck('w')) {
						if (!board.hasValidMove('w')) {
							Toast.makeText(this, "Checkmate! Black wins!", Toast.LENGTH_LONG).show();
							endGame();
						} else
							Toast.makeText(this, "Check", Toast.LENGTH_SHORT).show();
					} else {
						if (!board.hasValidMove('w')) {
							Toast.makeText(this, "Stalemate!", Toast.LENGTH_LONG).show();
							endGame();
						}
					}
				} else {
					if (board.playerIsInCheck('b')) {
						if (!board.hasValidMove('b')) {
							Toast.makeText(this, "Checkmate! White wins!", Toast.LENGTH_LONG).show();
							endGame();
						} else
							Toast.makeText(this, "Check", Toast.LENGTH_SHORT).show();;
					} else {
						if (!board.hasValidMove('b')) {
							Toast.makeText(this, "Stalemate!", Toast.LENGTH_LONG).show();
							endGame();
						}
					}
				}
				
				enPassantCounter++;
				if (enPassantCounter == 2) {
					board.enPassantPawn = null;
				}
				
				whiteTurn = !whiteTurn;
				updateBoard();
				
				if (drawProposed) {
					String color = whiteTurn ? "White" : "Black";
					drawDialog.setMessage(color + " player, do you accept the draw?");
					drawDialog.show();
				}
								
			} catch (Exception e) {
				Toast.makeText(this, "Invalid Move", Toast.LENGTH_SHORT).show();
				prevGameState = null;
				// TODO might need to make drawProposed false here
			}
			
			fromCoordinate = null;
			toCoordinate = null;
			
		}
		
	}
	
	public void endGame() {
		// TODO if not replayingGame, ask user if they want to save game (get save title if they do), then go back to main activity
		// below savedGame is a test
		saveDialog.show();
		
	}
	
	public void updateBoard() {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				ChessboardSquare square = chessboardDisplay[row][col];
				ChessPiece piece = chessboard[row][col];
				if (piece == null) {
					square.setImageDrawable(null);
					continue;
				}
				if (piece instanceof Bishop) {
					if (piece.color == 'w')
						square.setImageResource(R.drawable.wht_bishop);
					else
						square.setImageResource(R.drawable.blk_bishop);
				} else if (piece instanceof King) {
					if (piece.color == 'w')
						square.setImageResource(R.drawable.wht_king);
					else
						square.setImageResource(R.drawable.blk_king);
				} else if (piece instanceof Knight) {
					if (piece.color == 'w')
						square.setImageResource(R.drawable.wht_knight);
					else
						square.setImageResource(R.drawable.blk_knight);
				} else if (piece instanceof Pawn) {
					if (piece.color == 'w')
						square.setImageResource(R.drawable.wht_pawn);
					else
						square.setImageResource(R.drawable.blk_pawn);
				} else if (piece instanceof Queen) {
					if (piece.color == 'w')
						square.setImageResource(R.drawable.wht_queen);
					else
						square.setImageResource(R.drawable.blk_queen);
				} else {
					if (piece.color == 'w')
						square.setImageResource(R.drawable.wht_rook);
					else
						square.setImageResource(R.drawable.blk_rook);
				}
			}
		}
		
		if (whiteTurn) {
			turn.setTextColor(Color.WHITE);
			turn.setText("White Turn");
		} else {
			turn.setTextColor(Color.BLACK);
			turn.setText("Black Turn");
		}
	}
	
	public void initializeBoard() {
		
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
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
				chessboardDisplay[7-row][col] = square;
			}
		}
		
		// Set row 0 pieces
		chessboardDisplay[0][0].setImageResource(R.drawable.wht_rook);
		chessboardDisplay[0][1].setImageResource(R.drawable.wht_knight);
		chessboardDisplay[0][2].setImageResource(R.drawable.wht_bishop);
		chessboardDisplay[0][3].setImageResource(R.drawable.wht_queen);
		chessboardDisplay[0][4].setImageResource(R.drawable.wht_king);
		chessboardDisplay[0][5].setImageResource(R.drawable.wht_bishop);
		chessboardDisplay[0][6].setImageResource(R.drawable.wht_knight);
		chessboardDisplay[0][7].setImageResource(R.drawable.wht_rook);
		
		// Set row 1 pawns
		for (int col = 0; col < 8; col++) {
			ImageView square = chessboardDisplay[1][col];
			square.setImageResource(R.drawable.wht_pawn);
		}
		
		// Set row 6 pawns
		for (int col = 0; col < 8; col++) {
			ImageView square = chessboardDisplay[6][col];
			square.setImageResource(R.drawable.blk_pawn);
		}
		
		// Set row 7 pieces
		chessboardDisplay[7][0].setImageResource(R.drawable.blk_rook);
		chessboardDisplay[7][1].setImageResource(R.drawable.blk_knight);
		chessboardDisplay[7][2].setImageResource(R.drawable.blk_bishop);
		chessboardDisplay[7][3].setImageResource(R.drawable.blk_queen);
		chessboardDisplay[7][4].setImageResource(R.drawable.blk_king);
		chessboardDisplay[7][5].setImageResource(R.drawable.blk_bishop);
		chessboardDisplay[7][6].setImageResource(R.drawable.blk_knight);
		chessboardDisplay[7][7].setImageResource(R.drawable.blk_rook);
	}
	
	public void writeData() throws Exception {
		//File file = new File(this.getFilesDir(), "saved_games");
		ObjectOutputStream oos = new ObjectOutputStream(openFileOutput("saved_game", Context.MODE_PRIVATE));
		oos.writeObject(SavedGame.savedGames);
		oos.close();
	}
	
}
