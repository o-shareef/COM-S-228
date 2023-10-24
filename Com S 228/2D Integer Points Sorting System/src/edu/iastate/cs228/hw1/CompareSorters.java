package edu.iastate.cs228.hw1;

/**
 *  
 * @author Omran
 *
 */
/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class CompareSorters {
	/**
	 * Repeatedly take integer sequences either randomly generated or read from
	 * files. Use them as coordinates to construct points. Scan these points with
	 * respect to their median coordinate point four times, each time using a
	 * different sorting algorithm.
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException {

		int numPts = 100;

		Random rand = new Random();

		Point[] points = generateRandomPoints(numPts, rand);

		PointScanner[] scanners = new PointScanner[4];
		// a) Initialize the array scanners[].
		scanners[0] = new PointScanner(points, Algorithm.SelectionSort);
		scanners[1] = new PointScanner(points, Algorithm.InsertionSort);
		scanners[2] = new PointScanner(points, Algorithm.MergeSort);
		scanners[3] = new PointScanner(points, Algorithm.QuickSort);

		// b) Iterate through the array scanners[], and have every scanner call the
		// scan()
		// method in the PointScanner class.
		for (PointScanner scanner : scanners) {
			scanner.scan();
		}

		// c) After all four scans are done for the input, print out the statistics
		// table from
		// section 2.
		System.out.println("Algorithm      Points Scanned     Execution Time (ns)");
		System.out.println("-----------------------------------------------");
		for (PointScanner scanner : scanners) {
			String algorithmName = getAlgorithmName(scanner);
			int numScanned = getPointsScanned(scanner);
			long executionTime = getExecutionTime(scanner);
			System.out.printf("%-15s %d \t %d%n", algorithmName, numScanned, executionTime);
		}
	}

	/**
	 * This method generates a given number of random points. The coordinates of
	 * these points are pseudo-random numbers within the range [-50,50] � [-50,50].
	 * Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing.
	 * 
	 * @param numPts number of points
	 * @param rand   Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException {
		if (numPts < 1) {
			throw new IllegalArgumentException("Number of points must be greater than zero");
		}

		Point[] points = new Point[numPts];
		for (int i = 0; i < numPts; i++) {
			int x = rand.nextInt(101) - 50; // Range: -50 to 50
			int y = rand.nextInt(101) - 50; // Range: -50 to 50
			points[i] = new Point(x, y);
		}

		return points;
	}

	/**
	 * Returns the name of the sorting algorithm used by the given PointScanner
	 * object.
	 *
	 * @param scanner the PointScanner object
	 * @return the name of the sorting algorithm
	 */
	private static String getAlgorithmName(PointScanner scanner) {
		Algorithm algorithm = scanner.getAlgorithm();
		if (algorithm == Algorithm.SelectionSort) {
			return "Selection Sort";
		} else if (algorithm == Algorithm.InsertionSort) {
			return "Insertion Sort";
		} else if (algorithm == Algorithm.MergeSort) {
			return "Merge Sort";
		} else if (algorithm == Algorithm.QuickSort) {
			return "Quick Sort";
		} else {
			return "";
		}
	}

	/**
	 * Returns the execution time in nanoseconds for the given PointScanner object.
	 *
	 * @param scanner the PointScanner object
	 * @return the execution time in nanoseconds
	 */
	private static long getExecutionTime(PointScanner scanner) {
		return scanner.scanTime;
	}

	/**
	 * Returns the number of points scanned by the given PointScanner object.
	 *
	 * @param scanner the PointScanner object
	 * @return the number of points scanned
	 */
	private static int getPointsScanned(PointScanner scanner) {
		return scanner.getPoints().length;
	}

}
