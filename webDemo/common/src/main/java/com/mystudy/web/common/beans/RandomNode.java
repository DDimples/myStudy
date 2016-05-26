package com.mystudy.web.common.beans;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

public class RandomNode<T> implements Iterable<T> {

	protected final T[] nodes;

	public RandomNode(T[] nodes) {
		this.nodes = nodes;
	}

	@Override
	public Iterator<T> iterator() {
		return new RandomItr();
	}

	private class RandomItr implements Iterator<T> {
		// first random index
		int key = -1;
		// remaining
		int cap = nodes.length;
		boolean[] is;

		@Override
		public T next() {
			if (key == -1) {
				if (nodes.length == 1) {
					key = 0;
				} else {
					key = random(nodes.length);
				}
				cap--;
				return nodes[key];
			}
			if (nodes.length > 1) {
				if (is == null) {
					if (cap == 1) {
						// nodes.length==2
						cap = 0;
						return nodes[1 - key];
					}
					// init
					is = new boolean[nodes.length];
					if (key < is.length)
						is[key] = true;
					cap = nodes.length - 1;
				}
				if (cap > 0) {
					if (cap == 1) {
						// find the last one
						int c = 0;
						while (c < is.length && is[c]) {
							c++;
						}
						if (c < is.length) {
							cap = 0;
							return nodes[c];
						}
					} else {
						// random
						int end = random(cap);
						if (end < 0) {
							end = ~end;
						}
						for (int c = 0; c < is.length; c++) {
							if (is[c]) {
								end++;
							} else {
								if (c == end) {
									is[c] = true;
									cap--;
									return nodes[c];
								}
							}
						}
					}
				}
			}

			throw new NoSuchElementException();
		}

		@Override
		public final void remove() {
			// unsupported
		}

		@Override
		public boolean hasNext() {
			return cap > 0;
		}

		int random(int max) {
			int res = ThreadLocalRandom.current().nextInt() % max;
			return res < 0 ? ~res : res;
		}
	}
}
