package com.davis_newman_group18.chess;

import java.util.Scanner;

/**
 * 
 * @author Jason Davis and Michael Newman
 *
 */
public class Chess {
	
	static Scanner sc;
	static boolean drawProposed;
	static boolean validPlayMade;
	static int enPassantCounter = 2;
	
	public static void main(String[] args) {

		sc = new Scanner(System.in);
		ChessBoard.setupBoard();
		ChessBoard.printBoard();
		drawProposed = false;
		while(true) {
			whiteTurn();
			blackTurn();
		}
	}
	
	public static void whiteTurn() {
		
		if (ChessBoard.playerIsInCheck('w')) {
			if (!ChessBoard.hasValidMove('w')) {
				System.out.println("Checkmate\n\nBlack wins");
				endGame();
			} else {
				System.out.println("Check\n");
			}
		} else {
			if (!ChessBoard.hasValidMove('w')) {
				System.out.println("Stalemate");
				endGame();
			}
		}
		
		validPlayMade = false;
		while (!validPlayMade) {
			System.out.print("White's move: ");
			playNextMove('w');
		}
		System.out.println();
		ChessBoard.printBoard();
		enPassantCounter++;
		if (enPassantCounter == 2) {
			ChessBoard.enPassantPawn = null;
		}

	}
	
	public static void blackTurn() {
		
		if (ChessBoard.playerIsInCheck('b')) {
			if (!ChessBoard.hasValidMove('b')) {
				System.out.println("Checkmate\n\nWhite wins");
				endGame();
			} else {
				System.out.println("Check\n");
			}
		} else {
			if (!ChessBoard.hasValidMove('b')) {
				System.out.println("Stalemate");
				endGame();
			}
		}
		
		validPlayMade = false;
		while(!validPlayMade) {
			System.out.print("Black's move: ");
			playNextMove('b');
		}
		System.out.println();
		ChessBoard.printBoard();
		enPassantCounter++;
		if (enPassantCounter == 2) {
			ChessBoard.enPassantPawn = null;
		}

	}
	
	// Play the next move of the player of the given color
	public static void playNextMove(char color) {
		String move = sc.nextLine();
		if (move.equals("resign")) endGame();
		if (move.equals("draw")) {
			if (drawProposed) {
				System.out.println("\nDraw");
				endGame();
			} else {
				printErrorMessage();
				return;
			}
		}
		
		if (move.length() != 5 && move.length() != 7 && move.length() != 11) {
			printErrorMessage();
			return;
		}
		else if (move.charAt(0) < 'a' || move.charAt(0) > 'h' || move.charAt(1) < '1' || 
			move.charAt(1) > '8' || move.charAt(2) != ' ' || move.charAt(3) < 'a' || 
			move.charAt(3) > 'h' || move.charAt(4) < '1' || move.charAt(4) > '8') {
			printErrorMessage();
			return;
		}
		
		int fromFile = move.charAt(0) - 'a';
		int fromRank = Integer.parseInt(move.charAt(1) + "") - 1;
		int toFile = move.charAt(3) - 'a';
		int toRank = Integer.parseInt(move.charAt(4) + "") - 1;
		
		ChessPiece piece = ChessBoard.getPiece(fromRank, fromFile);
		if (piece == null || piece.color != color) {
			printErrorMessage();
			return;
		}
		
		if (move.length() == 5) {
			try {
				ChessBoard.getPiece(fromRank, fromFile).move(toRank, toFile);
			} catch (Exception e) {
				printErrorMessage();
				return;
			}
		}
		if (move.length() == 7) {
			if (move.charAt(5) != ' ' || new String(ChessBoard.pieceTypes).indexOf(move.charAt(6)) < 0) {
				printErrorMessage();
				return;
			} else {
				char promoteType = move.charAt(6);
				try {
					((Pawn)ChessBoard.getPiece(fromRank, fromFile)).move(toRank, toFile, promoteType);
				} catch (Exception e) {
					printErrorMessage();
					return;
				}
				return;
			}
		} else if (move.length() == 11) {
			if (!move.substring(5).equals(" draw?")) {
				printErrorMessage();
				return;
			} else {
				try {
					ChessBoard.getPiece(fromRank, fromFile).move(toRank, toFile);
					drawProposed = true;
					validPlayMade = true;
					return;
				} catch (Exception e) {
					printErrorMessage();
					return;
				}
			}
		}
		
		drawProposed = false;
		validPlayMade = true;
	}
	
	
	public static void printErrorMessage() {
		System.out.println("\nIllegal move, try again\n");
	}
	
	
	public static void endGame() {
		System.out.println();
		System.exit(0);
	}

}
