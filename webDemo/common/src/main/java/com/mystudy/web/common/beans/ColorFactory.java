package com.mystudy.web.common.beans;

import java.awt.Color;

public class ColorFactory {
	private final Color colors[];

	protected static final Color[] dcolors = new Color[] {
			new Color(113, 31, 71), new Color(37, 0, 37),
			new Color(111, 33, 36), new Color(0, 0, 112),
			new Color(14, 51, 16), new Color(1, 1, 1), new Color(72, 14, 73),
			new Color(65, 67, 29), new Color(116, 86, 88),
			new Color(41, 75, 71) };
	protected static final ColorFactory COLOR_FACTORY = new ColorFactory(
			dcolors);

	public static ColorFactory getDefault() {
		return COLOR_FACTORY;
	}

	public ColorFactory(Color colors[]) {
		this.colors = colors;
	}

	public Color randomColor() {
		return RandomFactory.random(colors);
	}

	public Color backgroundColor() {
		return Color.white;
	}

}
