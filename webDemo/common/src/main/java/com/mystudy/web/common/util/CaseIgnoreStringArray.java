package com.mystudy.web.common.util;

/**
 * used to compare whether two StringArray have same contents, ignore case
 * 
 * @author xiang.liu
 *
 */
public final class CaseIgnoreStringArray {

	private final String[] keys;
	private static final String[] EMPTYKEYS = new String[0];

	public CaseIgnoreStringArray(String... strs) {
		if (strs == null || strs.length == 0) {
			keys = EMPTYKEYS;
		} else {
			keys = new String[strs.length];
			System.arraycopy(strs, 0, keys, 0, strs.length);
		}
	}

	// cache hash
	private Integer hash;

	@Override
	public int hashCode() {
		if (hash == null) {
			int thash = 0;
			for (String key : keys) {
				if (key != null) {
					thash ^= key.toUpperCase().hashCode();
					thash = (thash << 13) | (thash >>> -13);
				}
			}
			hash = thash;
		}
		return hash.intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			// the same one
			return true;
		}
		if (CaseIgnoreStringArray.class.isInstance(obj)) {
			CaseIgnoreStringArray other = (CaseIgnoreStringArray) obj;
			if (this.keys.length == other.keys.length) {
				for (int i = 0; i < this.keys.length; i++) {
					if (this.keys[i] == null ? other.keys[i] != null
							: !this.keys[i].equalsIgnoreCase(other.keys[i])) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

}
