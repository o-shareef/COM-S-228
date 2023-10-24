package edu.iastate.cs228.hw1;

/**
 *  
 * @author Omran
 *
 */

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

/**
 * 
 * This class sorts all the points in an array of 2D points to determine a
 * reference point whose x and y coordinates are respectively the medians of the
 * x and y coordinates of the original points.
 * 
 * It records the employed sorting algorithm as well as the sorting time for
 * comparison.
 *
 */
public class PointScanner {
	private Point[] points;

	private Point medianCoordinatePoint; // point whose x and y coordinates are respectively the medians of
											// the x coordinates and y coordinates of those points in the array
											// points[].
	private Algorithm sortingAlgorithm;

	protected long scanTime; // execution time in nanoseconds.

	/**
	 * This constructor accepts an array of points and one of the four sorting
	 * algorithms as input. Copy the points into the array points[].
	 * 
	 * @param pts input array of points
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public PointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException {
		if (pts == null || pts.length == 0) {
			throw new IllegalArgumentException("Given array is null");
		}
		setPoints(Arrays.copyOf(pts, pts.length));
		sortingAlgorithm = algo;
	}

	/**
	 * This constructor reads points from a file.
	 * 
	 * @param inputFileName
	 * @throws FileNotFoundException
	 * @throws InputMismatchException if the input file contains an odd number of
	 *                                integers
	 */
	protected PointScanner(String inputFileName, Algorithm algo) throws FileNotFoundException, InputMismatchException {
		File inputFile = new File(inputFileName);
		if (!inputFile.exists()) {
			throw new FileNotFoundException("File not found: " + inputFileName);
		}

		Scanner scanner = new Scanner(inputFile);
		int numIntegers = 0;
		while (scanner.hasNextInt()) {
			numIntegers++;
			scanner.nextInt();
		}
		scanner.close();

		if (numIntegers % 2 != 0) {
			throw new InputMismatchException("File contains an odd number of integers.");
		}

		scanner = new Scanner(inputFile);
		int numPoints = numIntegers / 2;
		setPoints(new Point[numPoints]);
		for (int i = 0; i < numPoints; i++) {
			int x = scanner.nextInt();
			int y = scanner.nextInt();
			getPoints()[i] = new Point(x, y);
		}
		scanner.close();

		sortingAlgorithm = algo;
	}

	/**
	 * Carry out two rounds of sorting using the algorithm designated by
	 * sortingAlgorithm as follows:
	 * 
	 * a) Sort points[] by the x-coordinate to get the median x-coordinate. b) Sort
	 * points[] again by the y-coordinate to get the median y-coordinate. c)
	 * Construct medianCoordinatePoint using the obtained median x- and
	 * y-coordinates.
	 * 
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter,
	 * InsertionSorter, MergeSorter, or QuickSorter to carry out sorting.
	 * 
	 * @param algo
	 * @return
	 */
	public void scan() {

		// create an object to be referenced by aSorter according to sortingAlgorithm.
		// for each of the two
		// rounds of sorting, have aSorter do the following:
		//
		// a) call setComparator() with an argument 0 or 1.
		//
		// b) call sort().
		//
		// c) use a new Point object to store the coordinates of the
		// medianCoordinatePoint
		//
		// d) set the medianCoordinatePoint reference to the object with the correct
		// coordinates.
		//
		// e) sum up the times spent on the two sorting rounds and set the instance
		// variable scanTime.
		long startTime = System.nanoTime();

		AbstractSorter aSorter;
		if (sortingAlgorithm == Algorithm.SelectionSort) {
			aSorter = new SelectionSorter(getPoints());
		} else if (sortingAlgorithm == Algorithm.InsertionSort) {
			aSorter = new InsertionSorter(getPoints());
		} else if (sortingAlgorithm == Algorithm.MergeSort) {
			aSorter = new MergeSorter(getPoints());
		} else {
			aSorter = new QuickSorter(getPoints());
		}

		aSorter.setComparator(0);
		aSorter.sort();
		int medianIndex = getPoints().length / 2;
		int medianX = getPoints()[medianIndex].getX();

		aSorter.setComparator(1);
		aSorter.sort();
		int medianY = getPoints()[medianIndex].getY();

		medianCoordinatePoint = new Point(medianX, medianY);

		long endTime = System.nanoTime();
		scanTime = endTime - startTime;
	}

	/**
	 * Outputs performance statistics in the format:
	 * 
	 * <sorting algorithm> <size> <time>
	 * 
	 * For instance,
	 * 
	 * selection sort 1000 9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project description.
	 */

	public String stats() {
		String algorithmName;
		if (sortingAlgorithm == Algorithm.SelectionSort) {
			algorithmName = "selection sort";
		} else if (sortingAlgorithm == Algorithm.InsertionSort) {
			algorithmName = "insertion sort";
		} else if (sortingAlgorithm == Algorithm.MergeSort) {
			algorithmName = "merge sort";
		} else {
			algorithmName = "quick sort";
		}
		return algorithmName + "\t" + getPoints().length + "\t" + scanTime;
	}

	/**
	 * Write MCP after a call to scan(), in the format "MCP: (x, y)" The x and y
	 * coordinates of the point are displayed on the same line with exactly one
	 * blank space in between.
	 *
	 */
	@Override
	public String toString() {
		return "MCP: " + medianCoordinatePoint.toString();
	}

	/**
	 * 
	 * This method, called after scanning, writes point data into a file by
	 * outputFileName. The format of data in the file is the same as printed out
	 * from toString(). The file can help you verify the full correctness of a
	 * sorting result and debug the underlying algorithm.
	 * 
	 * @throws FileNotFoundException
	 */
	public void writeMCPToFile() throws FileNotFoundException {
		String outputFileName = "output.txt";

		File outputFile = new File(outputFileName);
		PrintWriter writer = new PrintWriter(outputFile);

		writer.print(toString());

		writer.close();

	}

	/**
	 * get Algorithm getter method
	 * 
	 * @return
	 */
	protected Algorithm getAlgorithm() {
		return sortingAlgorithm;
	}

	/**
	 * get point getter method
	 * 
	 * @return
	 */
	protected Point[] getPoints() {
		return points;
	}

	/*
	 * set point setter method
	 */
	protected void setPoints(Point[] points) {
		this.points = points;
	}
}
