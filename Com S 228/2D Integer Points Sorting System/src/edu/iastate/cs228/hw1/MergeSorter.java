package edu.iastate.cs228.hw1;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException;
import java.lang.IllegalArgumentException;
import java.util.InputMismatchException;

/**
 *  
 * @author Omran
 *
 */

/**
 * 
 * This class implements the mergesort algorithm.
 *
 */

public class MergeSorter extends AbstractSorter {
	// Other private instance variables if needed

	/**
	 * Constructor takes an array of points. It invokes the superclass constructor,
	 * and also set the instance variables algorithm in the superclass.
	 * 
	 * @param pts input array of integers
	 */
	public MergeSorter(Point[] pts) {
		super(pts);
		algorithm = ("mergesort");
	}

	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter.
	 * 
	 */
	@Override
	public void sort() {
		mergeSortRec(points);
	}

	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of
	 * points. One way is to make copies of the two halves of pts[], recursively
	 * call mergeSort on them, and merge the two sorted subarrays into pts[].
	 * 
	 * @param pts point array
	 */
	private void mergeSortRec(Point[] pts) {
		int n = pts.length;
		//base case, default array of index 2 is already sorted
		if (n < 2) {
			return; 
		}

		int mid = n / 2;
		Point[] left = new Point[mid];
		Point[] right = new Point[n - mid];

		//copy to left[] and right[]
		for (int i = 0; i < mid; i++) {
			left[i] = pts[i];
		}
		for (int i = mid; i < n; i++) {
			right[i - mid] = pts[i];
		}

		//sort both halves
		mergeSortRec(left);
		mergeSortRec(right);

		//merge halves
		merge(pts, left, right);
	}

	private void merge(Point[] pts, Point[] left, Point[] right) {
		int i = 0; //left[]
		int j = 0; //right[]
		int k = 0; //pts[]

		while (i < left.length && j < right.length) {
			if (pointComparator.compare(left[i], right[j]) <= 0) {
				pts[k++] = left[i++];
			} else {
				pts[k++] = right[j++];
			}
		}

		while (i < left.length) {
			pts[k++] = left[i++];
		}

		while (j < right.length) {
			pts[k++] = right[j++];
		}
	}
}
