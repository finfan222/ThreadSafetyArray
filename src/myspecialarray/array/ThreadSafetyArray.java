/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myspecialarray.array;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Guaranty <b>Thread Safety</b>. <b>Do not</b> resize automatically. When new
 * element added, {@code elements} will realculate.<br>
 *
 * @author finfan
 */
public class ThreadSafetyArray<T> {

	private final ReentrantLock locker = new ReentrantLock();

	private int size;
	private T[] elements;
	private int nextIndex;

	public ThreadSafetyArray(int size) {
		this.size = size;
		this.elements = (T[]) new Object[size];
	}

	public ThreadSafetyArray() {
		this.size = 10;
		this.elements = (T[]) new Object[size];
	}

	/**
	 * Insert the element to an array in next free space. Before element will be
	 * inserted the array will be locked, the lock will be released after
	 * element inserted successfuly.<br>
	 * <ul>
	 * <li>If givable index is more than current array size, the index
	 * automatically setted to LAST (size - 1)</li>
	 * <li>If givable index is less than 0, index automatically sets to 0.</li>
	 * </ul>
	 *
	 * @param element new element ins
	 * @return true if element successfull added to array, false otherwise.
	 */
	public boolean add(T element) {
		if (nextIndex >= size - 1) {
			// dynamical increase
			resize(size + 10);
		}

		locker.lock();
		try {
			elements[nextIndex] = element;
			recalc();
			return true;
		} finally {
			locker.unlock();
		}
	}

	/**
	 * Forcing insert the element to an array with given index.
	 * <font color="red">This method ignores the elements in given index, if
	 * element in given index is exist, he will be rewrighted by new one from
	 * {@code index} argument.</font><br>
	 * Before element will be inserted the array will be locked, the lock will
	 * be released after element inserting and returning the value. After
	 * inserting <i>nextFreeIndex</i> will be
	 * <b>recalculated</b> if given index != nextFreeIndex.<br>
	 * <ul>
	 * <li>If givable index is more than current array size, the index
	 * automatically setted to LAST (size - 1)</li>
	 * <li>If givable index is less than 0, index automatically sets to 0.</li>
	 * </ul>
	 *
	 * @param index given index place
	 * @param element new element inserted
	 */
	public void forceAdd(int index, T element) {
		if (index > size - 1) {
			index = size - 1;
		} else if (index < 0) {
			index = 0;
		}

		locker.lock();
		try {
			elements[index] = element;
			if (index == nextIndex) {
				recalc();
			}
		} finally {
			locker.unlock();
		}
	}

	/**
	 * Delete element from array by given index.
	 * <ul>
	 * <li>If given index > than (size - 1), return false</li>
	 * <li>If element not exist (null) in given index, return false</li>
	 * </ul>
	 * If all checks is done, lock the {@code elements} and remove the element
	 * on given index (by setting to NULL).
	 *
	 * @param index from where remove the element
	 * @return true if removing is success, false otherwise.
	 */
	public boolean remove(int index) {
		if (index > size - 1) {
			throw new ArrayIndexOutOfBoundsException();
		}

		if (elements[index] == null) {
			return false;
		}

		locker.lock();
		try {
			elements[index] = null;
			recalc();
			return true;
		} finally {
			locker.unlock();
		}
	}

	/**
	 * Clear elements array by setting each element to null.<br>
	 * Lock <b>ALL elements</b> and start to setting each element to
	 * {@code null}. Unlock after end of operation. Send debug message if needed
	 * and if debug mode is true.
	 */
	public void clear() {
		locker.lock();
		try {
			for (int i = 0; i < size; i++) {
				elements[i] = null;
			}
		} finally {
			locker.unlock();
		}
	}

	/**
	 * Reclaculate elements while not searched next free index in elements
	 * array. This function is hidden (private) cause it must call only in case
	 * of:
	 * <ul>
	 * <li>add new element</li>
	 * <li>remove the element</li>
	 * <li>resize {@code ThreadSafetyArray}</li>
	 * </ul>
	 * If debug mode is true, you will receive the message about recalculation
	 * results.
	 */
	private void recalc() {
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] == null) {
				nextIndex = i;
				break;
			}
		}
	}

	public void resize(int newSize) {
		locker.lock();
		try {
			this.size = newSize;
			final T[] temp = (T[]) new Object[size];
			for (int i = 0; i < elements.length; i++) {
				if (i > temp.length - 1) {
					break;
				}

				temp[i] = elements[i];
			}
			
			elements = (T[]) new Object[size];
			System.arraycopy(temp, 0, elements, 0, size);
			recalc();
		} finally {
			locker.unlock();
		}
	}

	/**
	 * Full fill array by given elements array. Add element to next free index
	 * from 0 to <b>size - 1</b>.
	 *
	 * @param elements given by client
	 */
	public void fill(T... elements) {
		System.arraycopy(elements, 0, this.elements, 0, elements.length);
	}

	/**
	 * Quick sort with ascending or descending condition.
	 *
	 * @param asc ascending if true, descending otherwise.
	 */
	public void sort(boolean asc) {
		//TODO: sort by ascending or descending with given consumer value (number)
	}

	public int getSize() {
		return size;
	}

	public T[] getElements() {
		return elements;
	}

	public T getElement(int index) {
		return elements[index];
	}

	public boolean isLocked() {
		return locker.isLocked();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Size: ").append(size).append("\n");
		sb.append("Next free index: ").append(nextIndex).append("\n");
		sb.append("Elements:\n");
		for(int i = 0; i < size ;i++) {
			if(elements[i] == null) {
				continue;
			}
			sb.append("[").append(i).append("] ").append(elements[i]).append("\n");
		}
		return sb.toString();
	}
	
	
}
