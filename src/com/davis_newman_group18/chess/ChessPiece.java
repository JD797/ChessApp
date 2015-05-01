package com.davis_newman_group18.chess;

// Jason
public abstract class ChessPiece {
	
	//ChessPiece[][] board;
	ChessBoard game;
	
	final char color;
	int numMovesMade = 0;
	int rank;
	int file;
	
	
	public ChessPiece(int rank, int file, char color, ChessBoard game) {
		this.color = color;
		numMovesMade = 0;
		this.rank = rank;
		this.file = file;
		this.game = game;
	}
	
	
	public void move(int toRank, int toFile) throws Exception {
		if (isValidMove(toRank, toFile)) {
			ChessPiece[][] board = game.chessBoard;
			board[toRank][toFile] = board[rank][file];
			board[rank][file] = null;
			rank = toRank;
			file = toFile;
			numMovesMade++;
		} else {
			throw new Exception();
		}
	}

	public abstract char getPieceType();
	public abstract boolean isValidMove(int rank, int file);

}
