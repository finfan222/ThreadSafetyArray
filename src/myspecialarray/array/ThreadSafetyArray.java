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
	private final int reserve;

	/**
	 * Creates a {@link ThreadSafetyArray} by given {@code <T> Type}.
	 *
	 * @param size default size of {@code safety array}
	 * @param reserve the value which affects on resize method. Defines the
	 * number of additional cells allocated for new future elements.
	 */
	public ThreadSafetyArray(int size, int reserve) {
		this.size = size;
		this.reserve = reserve;
		this.elements = (T[]) new Object[size];
	}

	/**
	 * Doesnt have a nodrmal javadoc.
	 *
	 * @deprecated
	 */
	@Deprecated
	public ThreadSafetyArray() {
		this.size = 10;
		this.reserve = 10;
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
		if (nextIndex >= size - 1) {
			// dynamical increase
			resize(size + reserve);
		}

		locker.lock();
		try {
			elements[nextIndex] = element;
			recalc();
			return true;
		} catch (RuntimeException e) {
			return false;
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
		final T elem = elements[index];
		if (elem == null) {
			throw new NullPointerException("Element at index " + index + " is null.");
		}

		return elem;
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
		for (int i = 0; i < size; i++) {
			sb.append("[").append(i).append("] ").append(elements[i]).append("\n");
		}
		return sb.toString();
	}

}
