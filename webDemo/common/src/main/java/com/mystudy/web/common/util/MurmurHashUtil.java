package com.mystudy.web.common.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MurmurHashUtil {
	public static int hash(byte[] bs, int seed) {

		int m = 0x5bd1e995;
		int r = 24;

		int h = seed ^ bs.length;

		int k;
		int index = 0;
		int size = bs.length >> 2;
		while (index < size) {
			k = BitUtil.getIntR(bs, index++ << 2);

			k *= m;
			k ^= k >>> r;
			k *= m;
			h *= m;
			h ^= k;
		}

		if ((bs.length & 3) != 0) {
			k = BitUtil.getIntR(bs, index << 2);
			h ^= k;
			h *= m;
		}

		h ^= h >>> 13;
		h *= m;
		h ^= h >>> 15;

		return h;
	}

	/**
	 * 
	 * @param buf
	 * @param seed
	 * @return
	 */
	static int hash(ByteBuffer buf, int seed) {
		// save byte order for later restoration
		ByteOrder byteOrder = buf.order();
		buf.order(ByteOrder.LITTLE_ENDIAN);

		int m = 0x5bd1e995;
		int r = 24;

		int h = seed ^ buf.remaining();

		int k;
		while (buf.remaining() >= 4) {
			k = buf.getInt();

			k *= m;
			k ^= k >>> r;
			k *= m;

			h *= m;
			h ^= k;
		}

		if (buf.remaining() > 0) {
			ByteBuffer finish = ByteBuffer.allocate(4).order(
					ByteOrder.LITTLE_ENDIAN);
			// for big-endian version, use this first:
			// finish.position(4-buf.remaining());
			finish.put(buf).rewind();
			k = finish.getInt();
			h ^= k;
			h *= m;
		}

		h ^= h >>> 13;
		h *= m;
		h ^= h >>> 15;

		buf.order(byteOrder);
		return h;
	}

	public static Integer hash(byte[] bytes) {
		return hash(bytes, 0x1234ABCD);
	}
	public static Integer hash(int i) {
		return hash(BitUtil.getBytes(i), 0x1234ABCD);
	}

}
