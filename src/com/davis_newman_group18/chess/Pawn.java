package com.davis_newman_group18.chess;

// Jason
public class Pawn extends ChessPiece {
	
	public Pawn(int rank, int file, char color, ChessBoard game) {
		super(rank, file, color, game);
	}

	@Override
	public char getPieceType() {
		return 'p';
	}

	@Override
	public boolean isValidMove(int toRank, int toFile) {
		ChessPiece[][] board = game.getBoard();
		if(rank == toRank && file == toFile) return false;
		ChessPiece piece;
		piece = board[toRank][toFile];
		if (piece != null && piece.color == color) return false;
		
		boolean enPassant = false;
		int ranksMoved = Math.abs(toRank - rank);
		int filesMoved = Math.abs(toFile - file);
		
		if (file == toFile) {
			if (ranksMoved > 2)
				return false;
			if (ranksMoved == 2) {
				if(numMovesMade == 0)
					enPassant = true;
				else
					return false;
			}
			if (rank < toRank) {
				if (color != 'w') return false;
				for (int i = rank+1; i <= toRank; i++) {
					piece = board[i][file];
					if (piece != null) return false;
				}
			} else {
				if (color != 'b') return false;
				for (int i = rank-1; i >= toRank; i--) {
					piece = board[i][file];
					if (piece != null) return false;
				}
			}
		} else {
			// TODO either taking a piece diagonally or en passant
			if (filesMoved > 1 || ranksMoved != 1)
				return false;
			if (rank < toRank) {
				if (color != 'w') return false;
			} else {
				if (color != 'b') return false;
			}
			
			if (board[toRank][toFile] == null) {
				ChessPiece adjacentPiece = board[rank][toFile];
				if (adjacentPiece == null || adjacentPiece.getPieceType() != 'p' || adjacentPiece != game.enPassantPawn)
					return false;
				
				board[rank][toFile] = null;
			}
			
		}
		
		if (game.putsSelfInCheck(this, toRank, toFile)) {
			return false;
		}
		
		if (enPassant) {
			game.enPassantPawn = this;
			ChessGame.enPassantCounter = 0;
		}
		return true;
	}
	

	public void move(int toRank, int toFile, char promoteType) throws Exception {
		ChessPiece[][] board = game.getBoard();
		if ((toRank == 7 && color == 'w') || (toRank == 0 && color == 'b')) {
			if (isValidMove(toRank, toFile)) {
				board[toRank][toFile] = board[rank][file];
				board[rank][file] = null;
				rank = toRank;
				file = toFile;
				numMovesMade++;
				game.promote(toRank, toFile, 'Q');
			} else {
				throw new Exception();
			}
		} else {
			throw new Exception();
		}
	}
	
	
	@Override
	public void move(int toRank, int toFile) throws Exception {
		ChessPiece[][] board = game.getBoard();
		if (isValidMove(toRank, toFile)) {
			board[toRank][toFile] = board[rank][file];
			board[rank][file] = null;
			rank = toRank;
			file = toFile;
			numMovesMade++;
			if ((toRank == 7 && color == 'w') || (toRank == 0 && color == 'b'))
				game.promote(toRank, toFile, 'Q');
		} else {
			throw new Exception();
		}
	}
	
	
	public void promote(char type) {
		game.promote(rank, file, type);
	}

}
