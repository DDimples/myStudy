package com.mystudy.web.common.beans;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Disturber {
	protected final Disturber parentDisturber;
	private static final Disturber DISTURBER = new Disturber();

	protected Disturber(Disturber disturber) {
		if (disturber == null) {
			disturber = Copier.getInstance();
		}
		this.parentDisturber = disturber;
	}

	private Disturber() {
		parentDisturber = null;
	}

	protected static final Random random = new Random();

	public BufferedImage disturb(BufferedImage bufferedImage) {
		return bufferedImage;
	}

	public static Disturber getInstance() {
		return DISTURBER;
	}

	private static class Copier extends Disturber {
		public Copier() {
			super();
		}

		private static final Disturber DISTURBER = new Copier();

		public BufferedImage disturb(BufferedImage bufferedImage) {
			BufferedImage bufferedImage2 = copy(bufferedImage);
			return bufferedImage2;
		}

		protected BufferedImage copy(BufferedImage bufferedImage) {
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();
			BufferedImage bufferedImage2 = new BufferedImage(width, height,
					bufferedImage.getType());
			bufferedImage.copyData(bufferedImage2.getRaster());
			return bufferedImage2;
		}

		public static Disturber getInstance() {
			return DISTURBER;
		}
	}
}