package com.davis_newman_group18.chess;

import android.content.Context;
import android.widget.ImageView;

public class ChessboardSquare extends ImageView {
	
	Coordinate coordinate;

	public ChessboardSquare(Context context, Coordinate coordinate) {
		super(context);
		this.coordinate = coordinate;
	}

}
