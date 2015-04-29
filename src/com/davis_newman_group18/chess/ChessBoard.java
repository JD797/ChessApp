package com.davis_newman_group18.chess;

// Jason
public class ChessBoard {
	
	static char[] pieceTypes = {'B', 'R', 'Q', 'p', 'N', 'K'};
	static String[][] boardColors = { {"##", "  ", "##", "  ", "##", "  ", "##", "  "}, {"  ", "##", "  ", "##", "  ", "##", "  ", "##"},
			{"##", "  ", "##", "  ", "##", "  ", "##", "  "}, {"  ", "##", "  ", "##", "  ", "##", "  ", "##"},
			{"##", "  ", "##", "  ", "##", "  ", "##", "  "}, {"  ", "##", "  ", "##", "  ", "##", "  ", "##"},
			{"##", "  ", "##", "  ", "##", "  ", "##", "  "}, {"  ", "##", "  ", "##", "  ", "##", "  ", "##"} };
	
	static ChessPiece[][] chessBoard;
	static ChessPiece wKing;
	static ChessPiece bKing;
	static ChessPiece enPassantPawn = null;
	

	public static void setupBoard() {
						
		chessBoard = new ChessPiece[8][8];
		
		chessBoard[0][0] = new Rook(0, 0, 'w');
		chessBoard[0][1] = new Knight(0, 1, 'w');
		chessBoard[0][2] = new Bishop(0, 2, 'w');
		chessBoard[0][3] = new Queen(0, 3, 'w');
		chessBoard[0][4] = new King(0, 4, 'w');
		chessBoard[0][5] = new Bishop(0, 5, 'w');
		chessBoard[0][6] = new Knight(0, 6, 'w');
		chessBoard[0][7] = new Rook(0, 7, 'w');
		
		for (int file = 0; file < 8; file ++) {
			chessBoard[1][file] = new Pawn(1, file, 'w');
		}
		
		for (int file = 0; file < 8; file ++) {
			chessBoard[6][file] = new Pawn(6, file, 'b');
		}
		
		chessBoard[7][0] = new Rook(7, 0, 'b');
		chessBoard[7][1] = new Knight(7, 1, 'b');
		chessBoard[7][2] = new Bishop(7, 2, 'b');
		chessBoard[7][3] = new Queen(7, 3, 'b');
		chessBoard[7][4] = new King(7, 4, 'b');
		chessBoard[7][5] = new Bishop(7, 5, 'b');
		chessBoard[7][6] = new Knight(7, 6, 'b');
		chessBoard[7][7] = new Rook(7, 7, 'b');
		
		wKing = chessBoard[0][4];
		bKing = chessBoard[7][4];
	}
	
	
	public static ChessPiece[][] getBoard() {
		return chessBoard;
	}
	
	
	public static ChessPiece getPiece(int rank, int file) {
		return chessBoard[rank][file];
	}
	
	public static void printBoard() {
		
		for (int rank = 7; rank >= 0; rank--) {
			for (int file = 0; file < 8; file++) {
				ChessPiece chessPiece = chessBoard[rank][file];
				if (chessPiece == null)
					System.out.print(boardColors[rank][file]);
				else
					System.out.print("" + chessPiece.color + chessPiece.getPieceType());
				System.out.print(" ");
			}
			System.out.println(rank+1);
		}
		
		System.out.println(" a  b  c  d  e  f  g  h");
		System.out.println();
	}
	
	
	public static void promote(int rank, int file, char type) {
		ChessPiece pawn = chessBoard[rank][file];
		ChessPiece promotedPiece;
		
		if (type == 'Q')
			promotedPiece = new Queen(rank, file, pawn.color);
		else if (type == 'B')
			promotedPiece = new Bishop(rank, file, pawn.color);
		else if (type == 'N')
			promotedPiece = new Knight(rank, file, pawn.color);
		else
			promotedPiece = new Rook(rank, file, pawn.color);
		
		promotedPiece.rank = pawn.rank;
		promotedPiece.file = pawn.file;
		chessBoard[rank][file] = promotedPiece;
	}
	
	
	// Checks if a player puts themselves in check with a move, which is illegal
	public static boolean putsSelfInCheck(ChessPiece movedPiece, int toRank, int toFile) {
		
		boolean kingInCheck = false;
		
		// Change the board so it's as if a legal move was made, check for checkmate, then put everything back
		int rank = movedPiece.rank;
		int file = movedPiece.file;
		movedPiece.rank = toRank;
		movedPiece.file = toFile;
		ChessPiece placeHolder = chessBoard[toRank][toFile];
		chessBoard[toRank][toFile] = movedPiece;
		chessBoard[rank][file] = null;
		
		// Get current location of player's King
		int kingRank;
		int kingFile;
		char color = movedPiece.color;
		if (color == 'w') {
			kingRank = wKing.rank;
			kingFile = wKing.file;
		} else {
			kingRank = bKing.rank;
			kingFile = bKing.file;
		}
		
		for (int r = 0; r < 8; r++) {
			for (int f = 0; f < 8; f++) {
				ChessPiece piece = chessBoard[r][f];
				if (piece != null && piece.color != color) {
					if (piece.isValidMove(kingRank, kingFile))
						kingInCheck = true;
				}
			}
			
		}
		
		chessBoard[rank][file] = chessBoard[toRank][toFile];
		chessBoard[toRank][toFile] = placeHolder;
		movedPiece.rank = rank;
		movedPiece.file = file;
		
		return kingInCheck;
	}
	
	// Checks whether the player of the given color is in check or not
	public static boolean playerIsInCheck(char color) {
		int kingRank, kingFile;
		if (color == 'w') {
			kingRank = wKing.rank;
			kingFile = wKing.file;
		} else {
			kingRank = bKing.rank;
			kingFile = bKing.file;
		}
		
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				ChessPiece piece = chessBoard[rank][file];
				if (piece != null && piece.color != color) {
					if (piece.isValidMove(kingRank, kingFile)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	// Checks whether the player of the given color has any valid move options or not
	public static boolean hasValidMove(char color) {
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				ChessPiece piece = chessBoard[rank][file];
				if (piece == null || piece.color != color) continue;
				for (int r = 0; r < 8; r++) {
					for(int f = 0; f < 8; f++) {
						if (piece.isValidMove(r, f))
							return true;
					}
				}
			}
		}
		
		return false;
	}
	
}