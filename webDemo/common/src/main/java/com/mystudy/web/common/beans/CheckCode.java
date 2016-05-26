package com.mystudy.web.common.beans;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author xiang.liu
 *
 */
public class CheckCode {

	/**
	 * 验证码字符串
	 */
	private String strCode;
	private boolean ready = false;
	private CheckCodeFactory factory;
	/**
	 * 验证码图片
	 */
	protected BufferedImage buffImg;
	private Graphics2D graphics;

	CheckCode(CheckCodeFactory factory) {
		this.factory = factory;
		buffImg = new BufferedImage(this.factory.width, this.factory.height,
				BufferedImage.TYPE_INT_ARGB);
		graphics = buffImg.createGraphics();
	}

	protected static final Color[] colors = new Color[] {
			new Color(113, 31, 71), new Color(37, 0, 37),
			new Color(111, 33, 36), new Color(0, 0, 112),
			new Color(14, 51, 16), new Color(1, 1, 1), new Color(72, 14, 73),
			new Color(65, 67, 29), new Color(116, 86, 88),
			new Color(41, 75, 71) };
	// excluding {O,0,1,I} , BTW j and l is also hard to be recognized
	protected static final String[] codeSequence;
	static {
		String[] codes = new String[256];
		char[] normalCodes = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
				'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
				'W', 'X', 'Y', 'Z' };
		int count = 0;
		for (int i = 0; i < normalCodes.length; i++) {
			codes[count++] = String.valueOf(normalCodes[i]);
			codes[count++] = String.valueOf(Character
					.toLowerCase(normalCodes[i]));
		}
		String[] nums = new String[] { "2", "3", "4", "5", "6", "7", "8", "9" };
		for (int i = 0; i < nums.length; i++) {
			codes[count++] = nums[i];
		}
		codes[count++] = "L";
		codeSequence = new String[count];
		System.arraycopy(codes, 0, codeSequence, 0, count);
	}
	protected static final String fontNames[] = new String[] { "Verdana",
			"Microsoft Sans Serif", "Comic Sans MS", "Arial" };
	// ITALIC is too thin to be recognized
	protected static final Integer fontStyles[] = new Integer[] { Font.PLAIN,
			Font.BOLD /* , Font.ITALIC */};
	// default color-factory
	protected static final ColorFactory COLOR_FACTORY = new ColorFactory(colors);

	protected static Color randomColor() {
		return COLOR_FACTORY.randomColor();
	}

	protected static Color backgroudColor() {
		return COLOR_FACTORY.backgroundColor();
	}

	protected synchronized final void create() {
		if (!ready) {
			fillBackGround();
			createRandomCode();
			disturb();
			ready = true;
		}
	}

	private void fillBackGround() {
		graphics.fillRect(0, 0, factory.width, factory.height);

	}

	/**
	 * 
	 * @Description:绘制干扰特性
	 * @since 1.0.0
	 * @Date:2014.12.05
	 * @return boolean 图片是否变化
	 */
	protected boolean disturb() {
		BufferedImage buffImg2 = factory.disturber.disturb(buffImg);
		if (buffImg2 == buffImg) {
			return false;
		} else {
			buffImg = buffImg2;
			return true;
		}
	}

	/**
	 * 
	 * @Description:随机产生的验证码
	 * @since 1.0.0
	 * @Date:2012-3-1 上午10:20:05
	 * @return String
	 */
	protected void createRandomCode() {
		Random random = ThreadLocalRandom.current();
		Color color = randomColor();
		graphics.setColor(color);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		StringBuilder sbd = new StringBuilder(factory.codeCount);
		// int idle = factory.width - (factory.codeWidth * factory.codeCount);
		// int blankX = idle >> 1;
		// int offsetX = random.nextInt(idle - blankX) + (blankX >> 1);
		CharCode[] ccs = new CharCode[factory.codeCount];
		int totalWidth = 0;
		for (int i = 0; i < factory.codeCount; i++) {
			String ch = randomCode(random);
			Font font = randomFont(random);
			FontMetrics fontMetrics = graphics.getFontMetrics(font);
			int currCodeWidth = fontMetrics.stringWidth(ch);
			totalWidth += currCodeWidth;
			int currCodeHeight = fontMetrics.getAscent();
			ccs[i] = new CharCode(ch, font, currCodeWidth, currCodeHeight);
			sbd.append(ch);
		}
		// 计算字符间距压缩量
		int leftX = totalWidth >> 4;
		int idle = -1;
		int offsetX = 0;
		while (idle < leftX) {
			idle = factory.width + (leftX * factory.codeCount) - totalWidth;
			if (idle < leftX) {
				leftX += ((leftX - idle) / factory.codeCount) + 1;
			}
		}// now idle will be positive
		int rangeX = idle - leftX;
		offsetX = leftX + (rangeX == 0 ? 0 : random.nextInt(rangeX));
		for (CharCode cc : ccs) {
			double rotate = (random.nextInt(720) - 360) * Math.PI / 3600.0;
			offsetX -= leftX;
			int x = offsetX + (cc.codeWidth >> 1);
			int midy = (int) (factory.height * ((random.nextDouble() * .1) + .45));
			graphics.rotate(rotate, x, midy);
			graphics.setFont(cc.font);
			graphics.drawString(cc.code, offsetX, midy + (cc.codeHeight >> 1));
			offsetX += cc.codeWidth;
			graphics.rotate(-rotate, x, midy);
		}
		strCode = sbd.toString();
	}

	static class CharCode {
		String code;
		Font font;
		int codeWidth;
		int codeHeight;

		public CharCode(String code, Font font, int codeWidth, int codeHeight) {
			this.code = code;
			this.font = font;
			this.codeWidth = codeWidth;
			this.codeHeight = codeHeight;
		}
	}

	protected String randomCode(Random random) {
		return RandomFactory.random(codeSequence, random);
	}

	protected Font randomFont(Random random) {
		String fontName = RandomFactory.random(fontNames, random);
		return new Font(fontName, RandomFactory.random(fontStyles, random),
				(int) (factory.codeHeight * (.7 + (random.nextDouble() * .3))));
	}

	public final String getCode() {
		create();
		return strCode;
	}

	public final BufferedImage getBuffImg() {
		create();
		return buffImg;
	}

}
