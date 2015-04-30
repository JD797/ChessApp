package com.davis_newman_group18.chess;

import android.content.Context;
import android.util.Pair;
import android.widget.ImageView;

public class ChessboardSquare extends ImageView {
	
	Pair<Integer, Integer> coordinate;

	public ChessboardSquare(Context context, Pair<Integer, Integer> coordinate) {
		super(context);
		this.coordinate = coordinate;
	}

}
