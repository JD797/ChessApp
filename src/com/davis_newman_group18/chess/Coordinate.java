package com.davis_newman_group18.chess;

public class Coordinate {
	
	int row;
	int col;
	
	public Coordinate(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Coordinate)) return false;
		Coordinate coordinate = (Coordinate)o;
		return (coordinate.row == row && coordinate.col == col);
	}

}
