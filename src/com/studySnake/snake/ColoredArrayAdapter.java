package com.studySnake.snake;


import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ColoredArrayAdapter<T> extends ArrayAdapter<T> {
	//these colors are blue and red
	private int[] colors = {0x300198E1, 0x30ff0000};
	public ColoredArrayAdapter(Context context, int textViewResourceId,
			List<T> objects) {
		super(context, textViewResourceId, objects);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		int color = colors[position % 2];
		view.setBackgroundColor(color);
		return view;
	}
}