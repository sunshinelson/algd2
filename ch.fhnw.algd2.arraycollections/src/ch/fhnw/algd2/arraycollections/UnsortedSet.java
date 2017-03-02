package ch.fhnw.algd2.arraycollections;

import java.util.Arrays;
import java.util.Set;

public class UnsortedSet<E> extends AbstractArrayCollection<E> implements
		Set<E> {
	public static final int DEFAULT_CAPACITY = 100;
	private Object[] data;

	public UnsortedSet() {
		this(DEFAULT_CAPACITY);
	}

	public UnsortedSet(int capacity) {
		data = new Object[capacity];
	}

	@Override
	public boolean add(E e) {
		// TODO implement unless collection shall be immutable
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		// TODO implement unless collection shall be immutable
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object o) {
		// TODO must be implemented
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		return Arrays.copyOf(data, size());
	}

	@Override
	public int size() {
		// TODO must be implemented
		return 0;
	}

	public static void main(String[] args) {
		UnsortedSet<Integer> bag = new UnsortedSet<Integer>();
		bag.add(2);
		bag.add(2);
		bag.add(1);
		System.out.println(Arrays.toString(bag.toArray()));
	}
}
