package com.davis_newman_group18.chess;

// Michael
public class Bishop extends ChessPiece {
	
	public Bishop(int rank, int file, char color, ChessBoard game) {
		super(rank, file, color, game);
	}

	@Override
	public char getPieceType() {
		return 'B';
	}

	@Override
	public boolean isValidMove(int toRank, int toFile) {
		ChessPiece[][] board = game.getBoard();
		if(rank == toRank && file == toFile) return false;
		ChessPiece piece;
		piece = board[toRank][toFile];
		if (piece != null && piece.color == color) return false;
		
		int rankDiff = this.rank - toRank;
		int fileDiff = this.file - toFile;
		int rankDiffAbs = Math.abs(rankDiff);
		int fileDiffAbs = Math.abs(fileDiff);
		
		// valid moves are diagonal with no other piece in path
		if (rankDiffAbs == fileDiffAbs) {
			// start curRank and curFile at one place from piece's current location
			int curRank = this.rank - (rankDiff / rankDiffAbs);
			int curFile = this.file - (fileDiff / fileDiffAbs);
			while (curRank != toRank && curFile != toFile) {
				if (board[curRank][curFile] != null) {	// piece in path, invalid move
					return false;
				}
				curRank -= rankDiff / rankDiffAbs;		// (+) or (-) 1
				curFile -= fileDiff / fileDiffAbs;		// (+) or (-) 1
			}
		}
		else {
			return false;
		}
		
		if (game.putsSelfInCheck(this, toRank, toFile)) {
			return false;
		}

		return true;
	}

}
