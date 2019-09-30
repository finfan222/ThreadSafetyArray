/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myspecialarray;

import myspecialarray.array.ThreadSafetyArray;

/**
 *
 * @author finfan
 */
public class Main {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		final ThreadSafetyArray<TestData> safetyArray = new ThreadSafetyArray(3);
		safetyArray.add(new TestData(25));
		safetyArray.add(new TestData(192));
		safetyArray.add(new TestData(1029));
		safetyArray.add(new TestData(123));
		safetyArray.add(new TestData(123));
		safetyArray.add(new TestData(123));
		safetyArray.add(new TestData(123));
		safetyArray.add(new TestData(123));
		safetyArray.add(new TestData(123));
		safetyArray.add(new TestData(123));
		safetyArray.add(new TestData(123));
		safetyArray.add(new TestData(123));
		safetyArray.add(new TestData(123));
		safetyArray.add(new TestData(123));
		safetyArray.add(new TestData(123));
		safetyArray.add(new TestData(123));
		safetyArray.add(new TestData(123));
		safetyArray.add(new TestData(123));
		safetyArray.add(new TestData(123));
		safetyArray.add(new TestData(123));
		safetyArray.add(new TestData(123));
		System.out.println(safetyArray);
	}

	private static class TestData {

		private final int value;

		public TestData(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	}
}
