package edu.iastate.cs228.hw1;

public class simpleTests {
	public static void main(String[] args) {
		//----------------------------POINT-------------------------------------------------------------------------
        // Test case 1: xORy = true, this.x < q.x
        Point p1 = new Point(2, 3);
        Point p2 = new Point(4, 3);
        Point.xORy = true;
        System.out.println(p1.compareTo(p2)); // Expected output: -1

        // Test case 2: xORy = true, this.x == q.x, this.y < q.y
        Point p3 = new Point(4, 3);
        Point p4 = new Point(4, 5);
        Point.xORy = true;
        System.out.println(p3.compareTo(p4)); // Expected output: -1

        // Test case 3: xORy = false, this.y < q.y
        Point p5 = new Point(2, 3);
        Point p6 = new Point(2, 5);
        Point.xORy = false;
        System.out.println(p5.compareTo(p6)); // Expected output: -1

        // Test case 4: xORy = false, this.y == q.y, this.x < q.x
        Point p7 = new Point(2, 5);
        Point p8 = new Point(4, 5);
        Point.xORy = false;
        System.out.println(p7.compareTo(p8)); // Expected output: -1

        // Test case 5: this.x == q.x and this.y == q.y
        Point p9 = new Point(2, 3);
        Point p10 = new Point(2, 3);
        Point.xORy = true;
        System.out.println(p9.compareTo(p10)); // Expected output: 0

        // Test case 6: General case, this.x and this.y are greater than q.x and q.y
        Point p11 = new Point(4, 5);
        Point p12 = new Point(2, 3);
        Point.xORy = true;
        System.out.println(p11.compareTo(p12)); // Expected output: 1
        System.out.println("");
        
        //--------------------------SELECTION SORTER-------------------------------------------------
        // Test Case 1: Sorting by x-coordinate
        Point[] points1 = { new Point(4, 2), new Point(1, 5), new Point(3, 3), new Point(2, 4) };
        testSelectionSort(points1, 0);

        // Test Case 2: Sorting by y-coordinate
        Point[] points2 = { new Point(4, 2), new Point(1, 5), new Point(3, 3), new Point(2, 4) };
        testSelectionSort(points2, 1);

        // Test Case 3: Empty array
        /*Point[] points3 = {};
        testSelectionSort(points3, 0);*/ // This one will return an exception because it is empty. 

        // Test Case 4: Array with one point
        Point[] points4 = { new Point(4, 2) };
        testSelectionSort(points4, 1);

        // Test Case 5: Array with duplicate points by x-coordinate
        Point[] points5 = { new Point(2, 3), new Point(1, 2), new Point(2, 1), new Point(1, 2) };
        testSelectionSort(points5, 0);
        
        // Test Case 6: Array with duplicate points by y-coordinate
        Point[] points6 = { new Point(2, 3), new Point(1, 2), new Point(2, 1), new Point(1, 2) };
        testSelectionSort(points6, 1);
    }

	private static void testSelectionSort(Point[] points, int order) {
	    SelectionSorter sorter = new SelectionSorter(points);
	    sorter.setComparator(order);
	    sorter.sort();

	    Point[] sortedPoints = new Point[points.length];
	    sorter.getPoints(sortedPoints);

	    System.out.println("Sorting order: " + (order == 0 ? "x-coordinate" : "y-coordinate"));
	    System.out.println("Original points:");
	    for (Point point : points) {
	        System.out.println(point);
	    }

	    System.out.println("Sorted points:");
	    for (int i = 0; i < sortedPoints.length; i++) {
	        System.out.println("Actual: " + sortedPoints[i]);
	        System.out.println("Expected: " + points[i]);
	    }

	    System.out.println("--------------------");
	    //--------------------------Quick Sorter-------------------------------------------------
	    // Create an array of points
	    Point[] quickPoints = { new Point(4, 2), new Point(1, 5), new Point(3, 3), new Point(2, 4) };

	    // Create a QuickSorter object
	    QuickSorter sorter1 = new QuickSorter(quickPoints);

	    // Set the comparator to sort by x-coordinate (order = 0)
	    sorter1.setComparator(0);

	    // Sort the points array
	    sorter1.sort();

	    // Get the sorted points
	    Point[] sortedPoints1 = new Point[quickPoints.length];
	    sorter1.getPoints(sortedPoints1);

	    // Print the sorted points
	    System.out.println("Sorted Points (QuickSorter X Coords):");
	    for (Point point : sortedPoints1) {
	        System.out.println(point);
	    }
	    
	    // Create an array of points
	    Point[] quickPoints1 = { new Point(4, 2), new Point(1, 5), new Point(3, 3), new Point(2, 4) };

	    // Create a QuickSorter object
	    QuickSorter sorter11 = new QuickSorter(quickPoints1);

	    // Set the comparator to sort by x-coordinate (order = 0)
	    sorter11.setComparator(1);

	    // Sort the points array
	    sorter11.sort();

	    // Get the sorted points
	    Point[] sortedPoints11 = new Point[quickPoints1.length];
	    sorter11.getPoints(sortedPoints11);

	    // Print the sorted points
	    System.out.println("Sorted Points (QuickSorter Y Coords):");
	    for (Point point : sortedPoints11) {
	        System.out.println(point);
	    }
	}
    }
