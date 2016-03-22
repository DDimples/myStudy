package com.mystudy.web.common.util;

import java.util.concurrent.ThreadLocalRandom;

public final class AccessUtil {
	private AccessUtil() {
	}

	public static Access getAccess(double percent) {
		if (percent <= 0) {
			return Nay.nay;
		}
		if (percent >= 100) {
			return Aye.aye;
		}
		return new RandomAccess(percent);
	}

	public interface Access {
		public boolean access();
	}

	private static class RandomAccess implements Access {
		private double percent;

		RandomAccess(double percent) {
			this.percent = percent / 100.0;
		}

		/*
		 * {@link java.util.concurrent.ThreadLocalRandom#nextDouble()} cost too
		 * much time ,use
		 * {@link java.util.concurrent.ThreadLocalRandom#nextFloat()} instead
		 */
		@Override
		public boolean access() {
			return percent > ThreadLocalRandom.current().nextFloat();
		}
	}

	/**
	 * no to all
	 * 
	 * @author user
	 *
	 */
	private static class Nay implements Access {
		static Nay nay = new Nay();

		private Nay() {
			// System.out.println("nay create");
		}

		@Override
		public boolean access() {
			return false;
		}
	}

	/**
	 * yes to all
	 * 
	 * @author user
	 *
	 */
	private static class Aye implements Access {
		static Aye aye = new Aye();

		private Aye() {
			// System.out.println("aye create");
		}

		@Override
		public boolean access() {
			return true;
		}
	}
}
