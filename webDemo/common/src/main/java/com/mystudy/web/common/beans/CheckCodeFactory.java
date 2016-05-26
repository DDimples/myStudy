package com.mystudy.web.common.beans;

import com.mystudy.web.common.util.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class CheckCodeFactory {

	protected static final int defaultWidth = 65;
	protected static final int defaultHeight = 35;
	protected static final int defaultCodeCount = 4;
	protected int width = defaultWidth;
	protected int height = defaultHeight;
	protected int codeCount = defaultCodeCount;
	protected int codeHeight;
	protected String disturbs;
	private CodeBuilder builder = new NullCodeBuilder();
	protected Disturber disturber;
	protected int codeWidth;

	public String getDisturbs() {
		return disturbs;
	}

	public void setDisturbs(String disturbs) {
		this.disturbs = disturbs;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getCodeCount() {
		return codeCount;
	}

	public void setCodeCount(int codeCount) {
		this.codeCount = codeCount;
	}

	public final CheckCode getCheckCode() {
		return builder.get();
	}

	private class CodeBuilder {

		boolean unable() {
			return false;
		}

		CheckCode get() {
			return new CheckCode(CheckCodeFactory.this);
		}
	}

	static final Pattern wordPattern = Pattern.compile("\\w+");

	private class NullCodeBuilder extends CodeBuilder {

		@Override
		boolean unable() {
			return true;
		}

		@Override
		CheckCode get() {
			if (builder.unable()) {
				synchronized (CheckCodeFactory.this) {
					if (builder.unable()) {
						codeWidth = (width - (width >> 3)) / codeCount;
						int codeW2 = (codeWidth << 1);
						if (codeW2 < height) {
							codeHeight = codeW2;
						} else {
							codeHeight = height;
						}
						if (StringUtil.isNotEmpty(disturbs)) {
							Matcher matcher = wordPattern.matcher(disturbs);
							Disturber disturberTemp = null;
							while (matcher.find()) {
								String d = matcher.group();
								if ("line".equals(d)) {
									if (disturberTemp == null) {
										disturberTemp = LineDisturber
												.getInstance();
									} else {
										disturberTemp = new LineDisturber(
												disturberTemp,
												ColorFactory.getDefault());
									}
								} else if ("wave".equals(d)) {
									if (disturberTemp == null) {
										disturberTemp = WaveDisturber
												.getInstance();
									} else {
										disturberTemp = new WaveDisturber(
												disturberTemp);
									}
								}
							}
							disturber = disturberTemp;
						}
						if (disturber == null) {
							disturber = Disturber.getInstance();
						}
						builder = new CodeBuilder();
					}
				}
			}
			return builder.get();
		}

	}
}
