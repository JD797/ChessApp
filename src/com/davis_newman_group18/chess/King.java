package com.davis_newman_group18.chess;

// Jason
public class King extends ChessPiece {
	
	boolean castling;
	
	public King(int rank, int file, char color) {
		super(rank, file, color);
		castling = false;
	}

	@Override
	public char getPieceType() {
		return 'K';
	}
	
	@Override
	public void move (int toRank, int toFile) throws Exception {
		if (isValidMove(toRank, toFile)) {
			board[toRank][toFile] = board[rank][file];
			board[rank][file] = null;
			numMovesMade++;
			if (castling) {
				ChessPiece rook;
				if (file < toFile) {
					rook = board[toRank][toFile+1];
					board[toRank][toFile-1] = rook;
					board[toRank][toFile+1] = null;
					rook.file = toFile-1;
				} else {
					rook = board[toRank][toFile-2];
					board[toRank][toFile+1] = rook;
					board[toRank][toFile-2] = null;
					rook.file = toFile+1;
				}
				rook.rank = toRank;
				rook.numMovesMade++;
			}
			rank = toRank;
			file = toFile;
		} else {
			throw new Exception();
		}
	}

	@Override
	public boolean isValidMove(int toRank, int toFile) {
	
		if(rank == toRank && file == toFile) return false;
		ChessPiece piece;
		piece = board[toRank][toFile];
		if (piece != null && piece.color == color) 
			return false;
		
		int rankMovement = Math.abs(toRank-rank);
		int fileMovement = Math.abs(toFile-file);
		if (rankMovement > 1 || fileMovement > 2) 
			return false;
		if (fileMovement == 2) {
			if (numMovesMade !=0 || rankMovement == 1) 
				return false;
			else 
				castling = true;
		}
			
		if (fileMovement == 2) {
			ChessPiece rook;
			if (file < toFile) {
				if (board[rank][file+1] != null || board[rank][toFile] != null) 
					return false;
				rook = board[toRank][toFile+1];
			} else {
				if (board[rank][file-1] != null || board[rank][toFile] != null) 
					return false;
				rook = board[toRank][toFile-2];
			}
			
			if (rook == null || rook.getPieceType() != 'R' || rook.numMovesMade != 0) {
				castling = false;
				return false;
			}
		}	
		
		if (ChessBoard.putsSelfInCheck(this, toRank, toFile)) {
			return false;
		}
		
		return true;
	}

}
