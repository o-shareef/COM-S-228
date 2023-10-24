package edu.iastate.cs228.hw3;

import java.util.Stack;

/**
 * @author omran 
 * Class that has the ability to decode specific text files
 */
public class MsgTree {
	/**
	 * represents the character stored in the current node of the message tree.
	 */
	public char payloadChar;
	/**
	 * represents the left child of the current node in the message tree.
	 */
	public MsgTree left;
	/**
	 * represents the right child of the current node in the message tree.
	 */
	public MsgTree right;

	/**
	 * Constructor building the tree from a string
	 * 
	 * @param encodingString
	 */
	public MsgTree(String encodingString) {
		if (encodingString == null || encodingString.length() < 2) {
			return;
		}

		int index = 0;
		this.payloadChar = encodingString.charAt(index++);
		Stack<MsgTree> stack = new Stack<>();
		stack.push(this);
		MsgTree current = this;
		boolean insideSubtree = true;

		while (index < encodingString.length()) {
			MsgTree newNode = new MsgTree(encodingString.charAt(index++));

			if (insideSubtree) {
				current.left = newNode;
			} else {
				current.right = newNode;
			}

			if (newNode.payloadChar == '^') {
				current = stack.push(newNode);
				insideSubtree = true;
			} else {
				if (!stack.empty()) {
					current = stack.pop();
				}
				insideSubtree = false;
			}
		}
	}

	/**
	 * Constructor for a single node with null children
	 * 
	 * @param payloadChar
	 */
	public MsgTree(char payloadChar) {
		this.payloadChar = payloadChar;
		this.left = null;
		this.right = null;
	}

	/**
	 * performs recursive preorder traversal of the MsgTree and prints all the
	 * characters and their bit codes:
	 * 
	 * @param root
	 * @param code
	 */
	public static void printCodes(MsgTree root, String code) {
		System.out.println("\ncharacter code\n-------------------------");
		for (char ch : code.toCharArray()) {
			StringBuilder binCode = new StringBuilder();
			root.binaryCode(ch, binCode);
			String character = (ch == '\n') ? "\\n" : String.valueOf(ch);
			System.out.printf("    %s    %s%n", character, binCode.toString());
		}
		System.out.println();
	}

	/**
	 * decodes the given encoded message and prints the decoded message
	 * 
	 * @param encodedMessage
	 */
	public void decode(MsgTree codes, String msg) {
		System.out.println("MESSAGE:");
		StringBuilder decodedMessage = new StringBuilder();
		MsgTree currentNode = codes;
		int bitIndex = 0;

		while (bitIndex < msg.length()) {
			char bit = msg.charAt(bitIndex++);
			if (currentNode.left != null && bit == '0') {
				currentNode = currentNode.left;
			} else if (currentNode.right != null && bit == '1') {
				currentNode = currentNode.right;
			}

			if (currentNode.payloadChar != '^') {
				decodedMessage.append(currentNode.payloadChar);
				currentNode = codes;
			}
		}

		System.out.println("\n" + decodedMessage.toString());
		printStatistics(msg, decodedMessage.toString());
	}

	/**
	 * Helper method for recursively finding the binary code for a character in the
	 * message tree.
	 * 
	 * @param root
	 * @param ch
	 * @param path
	 * @param code
	 */
	private static void binaryCode(MsgTree root, char ch, StringBuilder path, String code) {
		if (root != null) {
			if (root.payloadChar == ch) {
				path.append(code);
				return;
			}
			binaryCode(root.left, ch, path, code + "0");
			binaryCode(root.right, ch, path, code + "1");
		}
	}

	/**
	 * Returns the binary code for the specified character.
	 * 
	 * @param ch
	 * @param path
	 */
	private void binaryCode(char ch, StringBuilder path) {
		binaryCode(this, ch, path, "");
	}

	/**
	 * Print message-specific (not just encoding) statistics after the rest of the
	 * program output. The space savings calculation assumes that an uncompressed
	 * character is encoded with 16 bits. It is defined as (1 –
	 * compressedBits/uncompressedBits)*100. compressedBits is the sum of all
	 * characters in the message multiplied by each character's individual bits.
	 * 
	 * @param encodedMessage
	 * @param decodedMessage
	 */
	private static void printStatistics(String encodedMessage, String decodedMessage) {
		System.out.println("STATISTICS:");
		System.out.printf("Avg bits/char:\t%.2f%n", (double) encodedMessage.length() / decodedMessage.length());
		System.out.println("Total Characters:\t" + decodedMessage.length());
		double spaceSavings = (1 - decodedMessage.length() / (double) encodedMessage.length()) * 100;
		System.out.printf("Space Saving:\t%.2f%%%n", spaceSavings);
	}
}
