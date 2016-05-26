package com.mystudy.web.common.beans;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class RandomFactory {
	public static <T> T random(T[] ts, Random random) {
		return ts[random.nextInt(ts.length)];
	}

	public static <T> T random(T[] ts) {
		return random(ts, ThreadLocalRandom.current());
	}
}
