package com.davis_newman_group18.chess;

// Jason
public class Rook extends ChessPiece {

	public Rook(int rank, int file, char color, ChessBoard game) {
		super(rank, file, color, game);
	}
	
	@Override
	public char getPieceType() {
		return 'R';
	}
	
	@Override
	public boolean isValidMove(int toRank, int toFile) {
		ChessPiece[][] board = game.getBoard();
		if(rank == toRank && file == toFile) return false;
		ChessPiece piece;
		piece = board[toRank][toFile];
		if (piece != null && piece.color == color) return false;
		
		int ranksMoved = Math.abs(toRank-rank);
		int filesMoved = Math.abs(toFile-file);
		if (ranksMoved > 0 && filesMoved > 0) return false;
		
		if (rank == toRank) {
			if (file < toFile) {
				for (int i = file+1; i < toFile; i++) {
					piece = board[rank][i];
					if (piece != null) return false;
				}
			} else {
				for (int i = file-1; i > toFile; i--) {
					piece = board[rank][i];
					if (piece != null) return false;
				}
			}
			
		} else {
			if (rank < toRank) {
				for (int i = rank+1; i < toRank; i++) {
					piece = board[i][file];
					if (piece != null) return false;
				}
			} else {
				for (int i = rank-1; i > toRank; i--) {
					piece = board[i][file];
					if (piece != null) return false;
				}
			}
		}
		
		
		if (game.putsSelfInCheck(this, toRank, toFile)) {
			return false;
		}

		return true;
	}
	
}
