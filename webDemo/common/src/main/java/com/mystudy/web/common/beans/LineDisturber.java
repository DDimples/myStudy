package com.mystudy.web.common.beans;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class LineDisturber extends Disturber {

	protected final Random random = new Random();
	ColorFactory f;

	public LineDisturber(Disturber disturber, ColorFactory colorFactory) {
		super(disturber);
		if (colorFactory == null) {
			throw new IllegalArgumentException("ColorFactory could not be null");
		}
		this.f = colorFactory;
	}

	/**
	 * 
	 * @Description:画干扰线使图象中的认证码不易被其它程序探测到
	 * @since 1.0.0
	 * @Date:2012-3-1 下午12:49:47
	 * @return BufferedImage
	 */
	@Override
	public BufferedImage disturb(BufferedImage bufferedImage) {
		BufferedImage bufferedImage2 = parentDisturber.disturb(bufferedImage);
		Graphics2D graphics = bufferedImage2.createGraphics();
		int xs = 0;
		int ys = 0;
		int xe = 0;
		int ye = 0;
		int width = bufferedImage2.getWidth();
		int height = bufferedImage2.getHeight();
		for (int i = 0; i < 15; i++) {
			xs = random.nextInt(width);
			ys = random.nextInt(height);
			xe = random.nextInt(width);
			ye = random.nextInt(height);
			graphics.setColor(f.randomColor());
			graphics.drawLine(xs, ys, xe, ye);
		}
		return bufferedImage2;

	}

}