package com.davis_newman_group18.chess;

// Michael
public class Knight extends ChessPiece {
	
	public Knight(int rank, int file, char color) {
		super(rank, file, color);
	}

	@Override
	public char getPieceType() {
		return 'N';
	}

	@Override
	public boolean isValidMove(int toRank, int toFile) {
		
		if(rank == toRank && file == toFile) return false;
		ChessPiece piece;
		piece = board[toRank][toFile];
		if (piece != null && piece.color == color) return false;
		
		int rankDiffAbs = Math.abs(this.rank - toRank);
		int fileDiffAbs = Math.abs(this.file - toFile);
		
		// if not 2 moves in rank 1 in file, or 2 moves file 1 in rank move is invalid
		if (!((rankDiffAbs == 2 && fileDiffAbs == 1) || (fileDiffAbs == 2 && rankDiffAbs == 1))) {
			return false;
		}
		
		if (ChessBoard.putsSelfInCheck(this, toRank, toFile)) {
			return false;
		}

		return true;
	}

}
