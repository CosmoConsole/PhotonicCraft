package email.com.gmail.cosmoconsole.forge.photoniccraft.util;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A simple helper class that allows iterating over several lists consecutively.
 */
public class ListConcatenator<T> implements Iterable<T> {

	public class LCIterator implements Iterator<T> {
		private ListConcatenator<T> lc;
		private int listPointer;
		private int listIndex;

		public LCIterator(ListConcatenator<T> listConcatenator) {
			this.lc = listConcatenator;
			this.listPointer = this.listIndex = 0;
		}

		@Override
		public boolean hasNext() {
			while (true) {
				if (this.listPointer >= lc.lists.length) {
					return false;
				}
				if (this.listIndex >= lc.lists[this.listPointer].size()) {
					this.listIndex = 0;
					++this.listPointer;
					continue;
				}
				return true;
			}
		}

		@Override
		public T next() {
			if (!this.hasNext())
				throw new NoSuchElementException();
			return lc.lists[this.listPointer].get(this.listIndex++);
		}
	}

	List<T>[] lists;

	public ListConcatenator(List<T>... args) {
		this.lists = args;
	}

	@Override
	public Iterator<T> iterator() {
		return new ListConcatenator.LCIterator(this);
	}
}
