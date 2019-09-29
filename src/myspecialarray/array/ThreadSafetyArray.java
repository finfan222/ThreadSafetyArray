/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myspecialarray.array;

import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author finfan
 */
public class ThreadSafetyArray<T> {

	private final ReentrantLock locker = new ReentrantLock();

	private int size;
	private T[] elements;
	private int index;

	/**
	 * Creates a {@link ThreadSafetyArray} by given {@code <T> Type}.
	 *
	 * @param size default size of {@code safety array}
	 */
	public ThreadSafetyArray(int size) {
		this.size = size;
		this.elements = (T[]) new Object[size];
	}

	/**
	 * Insert the element to an array in next free space. Before element will be
	 * inserted, check the {@code nextIndex}. If {@code nextIndex} is more or
	 * equals to {@code size - 1}, array will resized automatically on a given
	 * {@code reserve} value.<br>
	 * If index check is ok, element will add to {@code elements} and array will
	 * be locked (and unlocked after success adding of element).<br>
	 *
	 * @param element new element ins
	 * @return true if element successfull added to array, false otherwise.
	 */
	public boolean add(T element) {
		if (index >= size - 1) {
			// dynamical increase
			resize(size + 1);
		}

		locker.lock();
		try {
			elements[index++] = element;
			return true;
		} finally {
			locker.unlock();
		}
	}

	/**
	 * Delete element from array by given index.
	 * <ul>
	 * <li>If given index > than {@code size - 1}, throw
	 * {@link ArrayIndexOutOfBoundsException}</li>
	 * <li>If element not exist (null) in given index then return
	 * {@code false}</li>
	 * </ul>
	 * If all checks is done, lock the {@code elements} and remove the element
	 * on given index (by setting index pos element to null).
	 *
	 * @param idx from where remove the element
	 * @return true if removing is success, false otherwise.
	 */
	public boolean remove(int idx) {
		if (idx > size - 1) {
			throw new ArrayIndexOutOfBoundsException();
		}

		if (elements[idx] == null) {
			return false;
		}

		locker.lock();
		try {
			elements[idx] = null;
			if (idx < index - 1) {
				for (int i = idx; i < size; i++) {
					final int next = i + 1;
					if (next < size) {
						elements[i] = elements[next];
					}
				}
			}
			index--;
			return true;
		} finally {
			locker.unlock();
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

	public T getLastElement() {
		return elements[index - 2];
	}
	
	public T getElement(int idx) {
		return elements[idx];
	}

	public boolean isLocked() {
		return locker.isLocked();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Size: ").append(size).append("\n");
		sb.append("Next free index: ").append(index).append("\n");
		sb.append("Elements:\n");
		for (int i = 0; i < size; i++) {
			sb.append("[").append(i).append("] ").append(elements[i]).append("\n");
		}
		sb.append("Last index: ").append(index);
		return sb.toString();
	}

}
