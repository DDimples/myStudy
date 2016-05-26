package com.mystudy.web.common.beans;

import java.awt.image.BufferedImage;

public class WaveDisturber extends Disturber {

	public WaveDisturber(Disturber disturber) {
		super(disturber);
	}

	/**
	 * 
	 * @Description:正弦曲线Wave扭曲图片
	 * @since 1.0.0
	 * @Date:2012-3-1 下午12:49:47
	 * @return BufferedImage
	 */
	@Override
	public BufferedImage disturb(BufferedImage bufferedImage) {
		bufferedImage = parentDisturber.disturb(bufferedImage);
		double dMultValue = random.nextInt(7) + 3;// 波形的幅度倍数，越大扭曲的程序越高，一般为3
		double dPhase = random.nextDouble() * 2 * Math.PI;// 波形的起始相位，取值区间（0-2＊PI）

		BufferedImage destBi = new BufferedImage(bufferedImage.getWidth(),
				bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

		for (int i = 0; i < destBi.getWidth(); i++) {
			for (int j = 0; j < destBi.getHeight(); j++) {
				int nOldX = getXPosition4Twist(dPhase, dMultValue,
						destBi.getHeight(), i, j);
				int nOldY = j;
				if (nOldX >= 0 && nOldX < destBi.getWidth() && nOldY >= 0
						&& nOldY < destBi.getHeight()) {
					destBi.setRGB(nOldX, nOldY, bufferedImage.getRGB(i, j));
				}
			}
		}
		return destBi;
	}

	/**
	 * 
	 * @Description:获取扭曲后的x轴位置
	 * @since 1.0.0
	 * @Date:2012-3-1 下午3:17:53
	 * @param dPhase
	 * @param dMultValue
	 * @param height
	 * @param xPosition
	 * @param yPosition
	 * @return int
	 */
	private int getXPosition4Twist(double dPhase, double dMultValue,
			int height, int xPosition, int yPosition) {
		double dx = (double) (Math.PI * yPosition) / height + dPhase;
		double dy = Math.sin(dx);
		return xPosition + (int) (dy * dMultValue);
	}

	private static final Disturber DISTURBER = new WaveDisturber(null);

	public static Disturber getInstance() {
		return DISTURBER;
	}

}