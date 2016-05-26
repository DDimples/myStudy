package com.mystudy.web.common.util;


public class BitUtil {
	public static byte[] getBytes(int num) {
		return new byte[] { (byte) (num >> 24), (byte) (num >> 16),
				(byte) (num >> 8), (byte) num };
	}

	public static int getInt(byte[] bytes, final int start) {
		int res = 0;
		if (bytes != null) {
			for (int i = 0; i < 4; i++) {
				res <<= 8;
				if (start + i < bytes.length) {
					res |= 0xff & bytes[start + i];
				}
			}
		}
		return res;
	}

	/**
	 * Using {@link java.nio.ByteOrder#LITTLE_ENDIAN} instead of default order, only used to compute hash code
	 * 
	 * @param bytes
	 * @param start
	 * @return
	 */
	static int getIntR(byte[] bytes, final int start) {
		int res = 0;
		if (bytes != null) {
			int s = Math.min(4, bytes.length - start);
			switch (s) {
			case 4:
				res = (0xff & bytes[start + 3]) << 24;
			case 3:
				res |= (0xff & bytes[start + 2]) << 16;
			case 2:
				res |= (0xff & bytes[start + 1]) << 8;
			case 1:
				res |= 0xff & bytes[start];
				break;
			default:
				break;
			}
		}
		return res;
	}

	public static long getLong(byte[] bytes, final int start) {
		int res = 0;
		if (bytes != null) {
			for (int end = Math.min(start + 7, bytes.length); start <= end; end--) {
				res <<= 8;
				res |= bytes[end];
			}
		}
		return res;
	}

}
