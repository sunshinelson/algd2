package ch.fhnw.algd2.arraycollections;

import java.util.Arrays;
import java.util.Set;

public class SortedSet<E extends Comparable<? super E>> extends
		AbstractArrayCollection<E> implements Set<E> {
	public static final int DEFAULT_CAPACITY = 100;
	private Object[] data;
	private int maxSize, size;

	public SortedSet() {
		this(DEFAULT_CAPACITY);
		maxSize = DEFAULT_CAPACITY;
	}

	public SortedSet(int capacity) {
		maxSize = capacity;
		data = new Object[capacity];
		size = 0;
	}

	@Override
	public boolean add(E e) {
		if (e.equals(null) || (size() == maxSize))
			throw new UnsupportedOperationException();
		if (contains(e)){
			return false;
		}else{
			data[size()] = e;
			size++;
			Arrays.sort(data, 0, size());
			return true;
		}		
	}

	@Override
	public boolean remove(Object o) {
		if (o.equals(null))
			throw new UnsupportedOperationException();
		if(contains(o)){
			int index = Arrays.binarySearch(data, 0, size(), o);
			for(int i= index; i < (size() - 1) ; i++){
				data[i] = data [i+1];
			}
			data[size() -1] = null;
			size--;
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean contains(Object o) {
		if (o.equals(null))
			throw new UnsupportedOperationException();
		for (int i=0; i < size(); i++){
			if(data[i].equals(o)){
				return true;
			}
		}
		return false;		
	}

	@Override
	public Object[] toArray() {
		return Arrays.copyOf(data, size());
	}

	@Override
	public int size() {
		return size;
	}

	public static void main(String[] args) {
		SortedSet<Integer> bag = new SortedSet<Integer>();
		bag.add(2);
		bag.add(2);
		bag.add(1);
		System.out.println(Arrays.toString(bag.toArray()));
	}
}
