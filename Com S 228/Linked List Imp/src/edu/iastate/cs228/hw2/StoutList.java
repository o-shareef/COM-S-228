package edu.iastate.cs228.hw2;

import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * @author omran 
 * Implementation of the list interface based on linked nodes that
 * store multiple items per node. Rules for adding and removing elements
 * ensure that each node (except possibly the last one) is at least half
 * full.
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E> {
	/**
	 * Default number of elements that may be stored in each node.
	 */
	private static final int DEFAULT_NODESIZE = 4;

	/**
	 * Number of elements that can be stored in each node.
	 */
	private final int nodeSize;

	/**
	 * Dummy node for head. It should be private but set to public here only for
	 * grading purpose. In practice, you should always make the head of a linked
	 * list a private instance variable.
	 */
	public Node head;

	/**
	 * Dummy node for tail.
	 */
	private Node tail;

	/**
	 * Number of elements in the list.
	 */
	private int size;

	/**
	 * Constructs an empty list with the default node size.
	 */
	public StoutList() {
		this(DEFAULT_NODESIZE);
	}

	/**
	 * Constructs an empty list with the given node size.
	 * 
	 * @param nodeSize number of elements that may be stored in each node, must be
	 *                 an even number
	 */
	public StoutList(int nodeSize) {
		if (nodeSize <= 0 || nodeSize % 2 != 0)
			throw new IllegalArgumentException();

		// dummy nodes
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		this.nodeSize = nodeSize;
	}

	/**
	 * Constructor for grading only. Fully implemented.
	 * 
	 * @param head
	 * @param tail
	 * @param nodeSize
	 * @param size
	 */
	public StoutList(Node head, Node tail, int nodeSize, int size) {
		this.head = head;
		this.tail = tail;
		this.nodeSize = nodeSize;
		this.size = size;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean add(E item) {
		// if item is null, throw NullPointerException
		if (item == null) {
			throw new NullPointerException();
		}

		// check if the item already exists in the list
		if (contains(item)) {
			return false;
		}

		// if the last node is full, create a new node
		if (size == 0 && item != null) {
			Node newNode = new Node();
			newNode.addItem(item);
			head.next = newNode;
			newNode.previous = head;
			tail.previous = newNode;
			newNode.next = tail;
		} else {
			Node lastNode = tail.previous;

			// add item to last node
			if (lastNode.count < nodeSize) {
				lastNode.addItem(item);
			}
			// new node when last node is full
			else {
				// create another node to accommodate for new item
				Node newNode = new Node();
				newNode.addItem(item);
				Node temp = lastNode.next;
				lastNode.next = newNode;
				newNode.previous = lastNode;
				newNode.next = temp;
				temp.previous = newNode;
			}
		}

		// increase the size of the list
		size++;
		return true;
	}

	/**
	 * Adds an item at the specified position in the list.
	 *
	 * @param position the position at which the item should be added
	 * @param element  the item to be added
	 */
	public void add(int pos, E item) {
		// check if the position is out of bounds, throw IndexOutOfBoundsException if
		// true
		if (pos < 0 || pos > size)
			throw new IndexOutOfBoundsException();

		// check if the list is empty, add the element at the beginning if true
		if (head.next == tail) {
			Node newNode = new Node();
			newNode.addItem(item);
			newNode.next = tail;
			newNode.previous = head;
			head.next = newNode;
			tail.previous = newNode;
			size++;
			return;
		}

		Node currentNode = head.next;
		int currentPosition = 0;

		// traverse the list to find the node at the specified position
		while (currentNode != tail) {
			if (currentPosition + currentNode.count <= pos) {
				currentPosition += currentNode.count;
				currentNode = currentNode.next;
				continue;
			}

			// check if there is space in the current node, add the element at the specified
			// offset
			if (currentNode.count < DEFAULT_NODESIZE) {
				int offset = pos - currentPosition;
				currentNode.addItem(offset, item);
			} else {
				// split the current node and redistribute the items
				Node newSucc = new Node();
				int splitPoint = currentNode.count / 2;
				int count = 0;
				while (count < splitPoint) {
					newSucc.addItem(currentNode.data[splitPoint]);
					currentNode.removeItem(splitPoint);
					count++;
				}

				Node oldSucc = currentNode.next;

				currentNode.next = newSucc;
				newSucc.previous = currentNode;
				newSucc.next = oldSucc;
				oldSucc.previous = newSucc;

				// check if the position is in the first half of the current node,
				// add the element to the current node
				if (pos <= currentPosition + splitPoint) {
					int offset = pos - currentPosition;
					currentNode.addItem(offset, item);
				}
				// check if the position is in the second half of the new successor node,
				// add the element to the new successor node
				else {
					int offset = pos - (currentPosition + splitPoint);
					newSucc.addItem(offset, item);
				}
			}

			size++;
			return;
		}
	}

	@Override
	public E remove(int pos) {
		// check if pos is a valid index within the list
		if (pos < 0 || pos >= size) {
			throw new IndexOutOfBoundsException();
		}

		NodeInfo nodeInfo = getNodeAtPosition(pos);
		E item = nodeInfo.getNode().data[nodeInfo.getOffset()];
		// remove the item from the node at the specified offset
		nodeInfo.getNode().removeItem(nodeInfo.getOffset());
		// check if the node has become empty after removing the item
		if (nodeInfo.getNode().count == 0) {
			Node previousNode = nodeInfo.getNode().previous;
			Node nextNode = nodeInfo.getNode().next;
			previousNode.next = nextNode;
			nextNode.previous = previousNode;
		}

		size--;

		return item;
	}

	/**
	 * get node at given position helper method for add and remove
	 * 
	 * @param pos
	 * @return
	 */
	private NodeInfo getNodeAtPosition(int pos) {
		if (pos < 0 || pos >= size) {
			throw new IndexOutOfBoundsException();
		}

		Node currentNode = head.next;
		int index = 0;

		while (currentNode != tail) {
			if (index + currentNode.count > pos) {
				int offset = pos - index;
				return new NodeInfo(currentNode, offset);
			}

			index += currentNode.count;
			currentNode = currentNode.next;
		}

		return null;
	}

	/**
	 * Sort all elements in the stout list in the NON-DECREASING order. You may do
	 * the following. Traverse the list and copy its elements into an array,
	 * deleting every visited node along the way. Then, sort the array by calling
	 * the insertionSort() method. (Note that sorting efficiency is not a concern
	 * for this project.) Finally, copy all elements from the array back to the
	 * stout list, creating new nodes for storage. After sorting, all nodes but
	 * (possibly) the last one must be full of elements.
	 * 
	 * Comparator<E> must have been implemented for calling insertionSort().
	 */
	public void sort() {

		E[] sortedList = (E[]) new Comparable[size];

		int tempIndex = 0;
		Node temp = head.next;
		while (temp != tail) {
			for (int i = 0; i < temp.count; i++) {
				sortedList[tempIndex] = temp.data[i];
				tempIndex++;
			}
			head.next = tail;
			tail.previous = head;
			temp = temp.next;
			size = 0;
		}

		insertionSort(sortedList);
		for (E element : sortedList) {
			add(element);
		}

	}

	/**
	 * Sort all elements in the stout list in the NON-INCREASING order. Call the
	 * bubbleSort() method. After sorting, all but (possibly) the last nodes must be
	 * filled with elements.
	 * 
	 * Comparable<? super E> must be implemented for calling bubbleSort().
	 */
	public void sortReverse() {
		E[] sortedList = (E[]) new Comparable[size];

		int tempIndex = 0;
		Node temp = head.next;
		while (temp != tail) {
			for (int i = 0; i < temp.count; i++) {
				sortedList[tempIndex] = temp.data[i];
				tempIndex++;
			}
			head.next = tail;
			tail.previous = head;
			temp = temp.next;
			size = 0;
		}

		bubbleSort(sortedList);
		for (E element : sortedList) {
			add(element);
		}

	}

	@Override
	public Iterator<E> iterator() {
		return new StoutListIterator();
	}

	@Override
	public ListIterator<E> listIterator() {
		return new StoutListIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new StoutListIterator(index);
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes.
	 */
	public String toStringInternal() {
		return toStringInternal(null);
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes and the position of the iterator.
	 *
	 * @param iter an iterator for this list
	 */
	public String toStringInternal(ListIterator<E> iter) {
		int count = 0;
		int position = -1;
		if (iter != null) {
			position = iter.nextIndex();
		}

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Node current = head.next;
		while (current != tail) {
			sb.append('(');
			E data = current.data[0];
			if (data == null) {
				sb.append("-");
			} else {
				if (position == count) {
					sb.append("| ");
					position = -1;
				}
				sb.append(data.toString());
				++count;
			}

			for (int i = 1; i < nodeSize; ++i) {
				sb.append(", ");
				data = current.data[i];
				if (data == null) {
					sb.append("-");
				} else {
					if (position == count) {
						sb.append("| ");
						position = -1;
					}
					sb.append(data.toString());
					++count;

					// iterator at end
					if (position == size && count == size) {
						sb.append(" |");
						position = -1;
					}
				}
			}
			sb.append(')');
			current = current.next;
			if (current != tail)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * @author omran 
	 * Node type for this list. Each node holds a maximum of nodeSize
	 *         elements in an array. Empty slots are null.
	 */
	private class Node {
		/**
		 * Array of actual data elements.
		 */
		// Unchecked warning unavoidable.
		public E[] data = (E[]) new Comparable[nodeSize];

		/**
		 * Link to next node.
		 */
		public Node next;

		/**
		 * Link to previous node;
		 */
		public Node previous;

		/**
		 * Index of the next available offset in this node, also equal to the number of
		 * elements in this node.
		 */
		public int count;

		/**
		 * Adds an item to this node at the first available offset. Precondition: count
		 * < nodeSize
		 * 
		 * @param item element to be added
		 */
		void addItem(E item) {
			if (count >= nodeSize) {
				return;
			}
			data[count++] = item;
			// useful for debugging
			// System.out.println("Added " + item.toString() + " at index " + count + " to
			// node " + Arrays.toString(data));
		}

		/**
		 * Adds an item to this node at the indicated offset, shifting elements to the
		 * right as necessary.
		 * 
		 * Precondition: count < nodeSize
		 * 
		 * @param offset array index at which to put the new element
		 * @param item   element to be added
		 */
		void addItem(int offset, E item) {
			if (count >= nodeSize) {
				return;
			}
			for (int i = count - 1; i >= offset; --i) {
				data[i + 1] = data[i];
			}
			++count;
			data[offset] = item;
			// useful for debugging
//      System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
		}

		/**
		 * Deletes an element from this node at the indicated offset, shifting elements
		 * left as necessary. Precondition: 0 <= offset < count
		 * 
		 * @param offset
		 */
		void removeItem(int offset) {
			E item = data[offset];
			for (int i = offset + 1; i < nodeSize; ++i) {
				data[i - 1] = data[i];
			}
			data[count - 1] = null;
			--count;
		}
	}

	/**
	 * @author omran
	 */
	private class NodeInfo {
		public Node node;
		public int offset;

		public NodeInfo(Node node, int offset) {
			this.node = node;
			this.offset = offset;
		}

		protected Node getNode() {
			return node;
		}

		protected int getOffset() {
			return offset;
		}
	}

	/**
	 * returns the node and offset for the given logical index
	 *
	 * @param pos
	 * @return
	 */
	private NodeInfo find(int pos) {
		Node temp = head.next; // start from the first node after the head
		int currPos = 0; // keep track of the current position in the list

		// traverse the list until the tail is reached or the position is found
		while (temp != tail && currPos + temp.count <= pos) {
			currPos += temp.count;
			temp = temp.next;
		}

		// check if the position is within the bounds of the list
		if (temp != tail) {
			NodeInfo nodeInfo = new NodeInfo(temp, pos - currPos);
			return nodeInfo;
		}

		// if the position is out of bounds, return null
		return null;
	}

	private NodeInfo add(Node n, int offset, E item) {
		if (offset == n.count) {
			// add element at the end of the node
			n.data[offset] = item;
			n.count++;
			return new NodeInfo(n, offset);
		}

		if (offset > n.count) {
			throw new IndexOutOfBoundsException("Invalid offset: " + offset);
		}

		if (n.count < n.data.length) {
			// Node has space, shift elements and insert the new element
			for (int i = n.count; i > offset; i--) {
				n.data[i] = n.data[i - 1];
			}
			n.data[offset] = item;
			n.count++;
			return new NodeInfo(n, offset);
		}

		// Node is full, split it into two nodes
		Node newNode = new Node();
		int mid = n.count / 2;

		// Move elements from the split position to the new node
		for (int i = mid; i < n.count; i++) {
			newNode.data[newNode.count++] = n.data[i];
		}

		// Insert the new element
		if (offset <= mid) {
			// Insert in the first half of the node
			for (int i = mid; i > offset; i--) {
				n.data[i] = n.data[i - 1];
			}
			n.data[offset] = item;
			n.count++;
		} else {
			// Insert in the second half of the node
			for (int i = newNode.count; i > (offset - mid); i--) {
				newNode.data[i] = newNode.data[i - 1];
			}
			newNode.data[offset - mid] = item;
			newNode.count++;
		}

		// Update node links
		newNode.next = n.next;
		newNode.previous = n;
		n.next.previous = newNode;
		n.next = newNode;

		return new NodeInfo(newNode, offset - mid);
	}

	/**
	 * @author omran
	 */
	private class StoutListIterator implements ListIterator<E> {
		/**
		 * current position in iteration
		 */
		public int currPos;
		/**
		 * used as temporary position to help keep track of iteration
		 */
		int tempPos;
		/**
		 * list keeping track of position
		 */
		private E[] iterationList;

		/**
		 * Default constructor
		 */
		@SuppressWarnings("unchecked")
		public StoutListIterator() {
			currPos = 0;
			tempPos = -1;

			iterationList = (E[]) new Comparable[size];
			int tempIndex = 0;
			Node temp = head.next;

			while (temp != tail) {
				System.arraycopy(temp.data, 0, iterationList, tempIndex, temp.count);
				tempIndex += temp.count;
				temp = temp.next;
			}
		}

		/**
		 * Constructor finds node at a given position.
		 * 
		 * @param pos
		 */
		@SuppressWarnings("unchecked")
		public StoutListIterator(int pos) {
			currPos = pos;
			tempPos = -1;

			iterationList = (E[]) new Comparable[size];
			int tempIndex = 0;
			Node temp = head.next;

			while (temp != tail) {
				System.arraycopy(temp.data, 0, iterationList, tempIndex, temp.count);
				tempIndex += temp.count;
				temp = temp.next;
			}
		}

		@Override
		public boolean hasNext() {
			return currPos < size;
		}

		@Override
		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			E item = iterationList[currPos++];
			tempPos = 1;

			return item;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void remove() {
			// if last position was next
			if (tempPos == 1) {
				// ensure there is a valid index
				if (currPos - 1 >= 0) {
					int removedIndex = currPos - 1;
					StoutList.this.remove(removedIndex);

					iterationList = (E[]) new Comparable[size];
					int tempIndex = 0;
					Node temp = head.next;

					while (temp != tail) {
						System.arraycopy(temp.data, 0, iterationList, tempIndex, temp.count);
						tempIndex += temp.count;
						temp = temp.next;
					}

					currPos--;

				} else {
					throw new IllegalStateException();
				}
				// check if last position was previous
			} else if (tempPos == 0) {
				StoutList.this.remove(currPos);
			} else {
				throw new IllegalStateException();
			}
			tempPos = -1; // reset tempPos
		}

		@Override
		public boolean hasPrevious() {
			return currPos > 0;

		}

		@Override
		public E previous() {
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}
			tempPos = -1;
			currPos--;
			return iterationList[currPos];

		}

		@Override
		public int nextIndex() {
			return currPos;
		}

		@Override
		public int previousIndex() {
			return currPos - 1;
		}

		@Override
		public void set(E e) {
			if (e == null) {
				throw new NullPointerException();
			}

			Node currentNode = head.next;
			while (currentNode != tail) {
				for (int i = 0; i < currentNode.count; i++) {
					if (currentNode.data[i].equals(e)) {
						currentNode.data[i] = e;
						return;
					}
				}
				currentNode = currentNode.next;
			}

		}

		@SuppressWarnings("unchecked")
		@Override
		public void add(E e) {
			// adds element if the given is not null
			if (e == null) {
				throw new NullPointerException();
			}
			// add to end of list
			StoutList.this.add(currPos, e);
			currPos++;

			iterationList = (E[]) new Comparable[size];
			int tempIndex = 0;
			Node temp = head.next;

			while (temp != tail) {
				System.arraycopy(temp.data, 0, iterationList, tempIndex, temp.count);
				tempIndex += temp.count;
				temp = temp.next;
			}

			tempPos--; // reset tempPos

		}

		// Other methods you may want to add or override that could possibly facilitate
		// other operations, for instance, addition, access to the previous element,
		// etc.
		//
		// ...
		//
	}

	/**
	 * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING
	 * order.
	 * 
	 * @param arr  array storing elements from the list
	 * @param comp comparator used in sorting
	 */
	private void insertionSort(E[] arr) {
		int n = arr.length;
		for (int i = 1; i < n; i++) {
			E key = arr[i];
			int j = i - 1;

			while (j >= 0 && arr[j].compareTo(key) > 0) {
				arr[j + 1] = arr[j];
				j--;
			}
			arr[j + 1] = key;

		}
	}

	/**
	 * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a
	 * description of bubble sort please refer to Section 6.1 in the project
	 * description. You must use the compareTo() method from an implementation of
	 * the Comparable interface by the class E or ? super E.
	 * 
	 * @param arr array holding elements from the list
	 */
	private void bubbleSort(E[] arr) {
		int n = arr.length;
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				if (arr[j].compareTo(arr[j + 1]) < 0) {
					E temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
				}
			}
		}
	}
}
