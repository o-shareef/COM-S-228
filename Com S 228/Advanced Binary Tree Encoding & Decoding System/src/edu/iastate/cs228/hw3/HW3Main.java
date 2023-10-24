package edu.iastate.cs228.hw3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * @author omran 
 * Class that outputs a decoded message based on a given file, as
 * well as the character codes per bit codes and statistics
 */
public class HW3Main {
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter filename to decode:"); // prompt user to enter the filename
		String fileName = scanner.nextLine();
		scanner.close();

		String fileContent = readFileContent(fileName); // read the content of the file
		String pattern = extractPattern(fileContent); // extract the pattern from the file content
		String encodedMessage = extractEncodedMessage(fileContent); // extract the encoded message from the file content

		Set<Character> charSet = extractUniqueCharacters(pattern); // extract unique characters from the pattern
		String charDict = buildCharacterDictionary(charSet); // build character dictionary

		MsgTree root = new MsgTree(pattern); // create an instance of MsgTree with the pattern
		MsgTree.printCodes(root, charDict);// print characters per bit codes
		root.decode(root, encodedMessage); // decode the encoded message using the MsgTree

	}

	/*----------------HELPER METHODS------------------*/

	/**
	 * Read the content of the file.
	 *
	 * @param fileName The name of the file to read.
	 * @return The content of the file as a string.
	 * @throws IOException if an I/O error occurs while reading the file.
	 */
	private static String readFileContent(String fileName) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(fileName));
		return new String(bytes).trim();
	}

	/**
	 * Extract the pattern from the file content.
	 *
	 * @param fileContent The content of the file.
	 * @return The pattern extracted from the file content.
	 */
	private static String extractPattern(String fileContent) {
		int patternEndIndex = fileContent.lastIndexOf('\n');
		return fileContent.substring(0, patternEndIndex).trim();
	}

	/**
	 * Extract the encoded message from the file content.
	 *
	 * @param fileContent The content of the file.
	 * @return The encoded message extracted from the file content.
	 */
	private static String extractEncodedMessage(String fileContent) {
		int patternEndIndex = fileContent.lastIndexOf('\n');
		return fileContent.substring(patternEndIndex).trim();
	}

	/**
	 * Extract unique characters from the pattern.
	 *
	 * @param pattern The pattern.
	 * @return A set of unique characters extracted from the pattern.
	 */
	private static Set<Character> extractUniqueCharacters(String pattern) {
		Set<Character> charSet = new HashSet<>();
		for (char c : pattern.toCharArray()) {
			if (c != '^') {
				charSet.add(c);
			}
		}
		return charSet;
	}

	/**
	 * Build a character dictionary.
	 *
	 * @param charSet A set of unique characters.
	 * @return The character dictionary as a string.
	 */
	private static String buildCharacterDictionary(Set<Character> charSet) {
		StringBuilder builder = new StringBuilder();
		for (Character c : charSet) {
			builder.append(c);
		}
		return builder.toString();
	}
}
