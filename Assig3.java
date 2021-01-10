import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Assig3 {
	// global variables
	private String dir;
	private StringBuilder currWord;
	private ArrayList<Integer> coords = new ArrayList<Integer>();
	private ArrayList<Integer> startingCoords = new ArrayList<Integer>();
	private ArrayList<Integer> finalCoords = new ArrayList<Integer>();
	private ArrayList<String> words, foundWords;
	private int xf = 0, yf = 0, x = 0, y = 0;
	// These booleans determine what for loops are used. There are two different for
	// loops for finding the word--one for the first word and one for the rest of
	// the words.
	private boolean firstWordFound, allWordsFound, abort;

	// Constructor
	public Assig3() {
		StringBuilder b = new StringBuilder("");
		startingCoords.add(0);
		startingCoords.add(0);
		allWordsFound = false;
		firstWordFound = false;
		abort = false;
		dir = " ";
		Scanner scanner = new Scanner(System.in);
		Scanner fileScanner;
		File file;
		String fString = "", phrase = "";
		words = new ArrayList<String>();
		foundWords = new ArrayList<String>();

		// Check file name!
		while (true) {
			try {
				System.out.println("Please enter grid filename:");
				fString = scanner.nextLine();
				file = new File(fString);
				fileScanner = new Scanner(file);

				break;
			} catch (IOException e) {
				System.out.println("Problem " + e);
			}
		}

		// Parse input file to create 2-d grid of characters
		String[] dims = (fileScanner.nextLine()).split(" ");
		int rows = Integer.parseInt(dims[0]);
		int cols = Integer.parseInt(dims[1]);

		char[][] theBoard = new char[rows][cols];

		for (int i = 0; i < rows; i++) {
			String rowString = fileScanner.nextLine();
			for (int j = 0; j < rowString.length(); j++) {
				theBoard[i][j] = Character.toLowerCase(rowString.charAt(j));
			}
		}
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				System.out.print(theBoard[i][j] + " ");
			}
			System.out.println();
		}
		while (true) {
			// Show user the grid
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					theBoard[i][j] = Character.toLowerCase(theBoard[i][j]);
				}
			}

			System.out.println("\nPlease enter phrase (sep. by single spaces): ");
			phrase = (scanner.nextLine()).toLowerCase();
			if (phrase.equals(""))
				System.exit(0);
			// Adds individual words into a list
			for (String s : phrase.split(" ")) {
				words.add(s);
			}

			System.out.println("Looking for: " + phrase);
			System.out.println("Containing " + words.size() + " words");

			// Main loop
			// ***NOTE*** Some of this code is taken from FindWord.java (provided by Dr.
			// Ramirez) so I kept the documentation.
			while (words.isEmpty() == false) {
				boolean found = false;

				// For first word
				if (firstWordFound == false) {
					int xt = startingCoords.get(0);
					int yt = startingCoords.get(1);
					for (int r = yt; (r < rows && !found); r++) // need to start at 0 everytime but if it
																// matches, check to see if adjacent to end
																// of word before it
					{
						for (int c = xt; (c < cols && !found); c++) {
							// Start search for each position at index 0 of the word
							currWord = new StringBuilder("");
							found = findPhrase(r, c, words.get(0), 0, theBoard);
							if (found) {
								x = r; // store starting indices of solution
								y = c;
								xf = coords.remove(0);
								yf = coords.remove(0);
							}
						}
					}
				}
				// Finding the rest of the words if first word is found
				else {
					int xv = finalCoords.get(finalCoords.size() - 1);
					int yv = finalCoords.get(finalCoords.size() - 2);
					for (int r = yv; (r < yv + 1 && !found); r++) {
						for (int c = xv; (c < xv + 1 && !found); c++) {
							currWord = new StringBuilder("");
							found = findPhrase(r, c, words.get(0), 0, theBoard);
							if (found) {
								if (dir == "right")
									c++;
								if (dir == "down")
									r++;
								if (dir == "left")
									c--;
								if (dir == "up")
									r--;
								x = r; // store starting indices of solution
								y = c;
								xf = coords.remove(0);
								yf = coords.remove(0);

							}
							if (abort && !found) {
								break;
							}
						}
					}
				}
				// If the word is found, it goes through the following if statements
				if (found) {

					if (words.size() == 1) // Checks to see if the found word is the last word. If so, it sets the
											// "allWordsFound" boolean to true
						allWordsFound = true;
					if (firstWordFound == false) // Checks to see if the found word is the first found word. If so, it
													// sets the "firstWordFound" boolean to true;
						firstWordFound = true;
					if (allWordsFound == false) { // If not all words are found, it "string" to a builder that will
													// displayed if all words are found. Also adds the coordinates to
													// various lists that may be used later.
						startingCoords.add(x);
						startingCoords.add(y);
						finalCoords.add(xf);
						finalCoords.add(yf);
						dir = " ";
						b.append(words.get(0) + " found from " + "(" + x + "," + y + ")" + " to " + "(" + xf + "," + yf
								+ ")\n");
						foundWords.add(words.remove(0));
						continue;

					} else { // When all words are found!
						b.append(words.get(0) + " found from " + "(" + x + "," + y + ")" + " to " + "(" + xf + "," + yf
								+ ")");
						System.out.println("\nThe phrase: " + '"' + phrase + '"' + " was found");
						System.out.println(b.toString());
						for (int i = 0; i < rows; i++) {
							for (int j = 0; j < cols; j++) {
								System.out.print(theBoard[i][j] + " ");
								theBoard[i][j] = Character.toLowerCase(theBoard[i][j]);
							}
							System.out.println();
						}
						foundWords.clear();
						words.clear();
						startingCoords.clear();
						coords.clear();
						finalCoords.clear();
						startingCoords.add(0);
						startingCoords.add(0);
						b.setLength(0);
						allWordsFound = false;
						firstWordFound = false;
						abort = false;
						dir = " ";
					}
				}
				// If backtracking is necessary, this if statement props and the loop tries to
				// find the word again.
				if (abort == true) {
					foundWords.clear();
					abort = false;
					b.setLength(0);
					continue;
				}
				// If the phrase is not found.
				if (!found) {
					System.out.println("Could not find the phrase: " + '\"' + phrase + '\"');
					words.clear();
					break;
				}
			}
		}
	}

	// Main
	public static void main(String[] args) {
		new Assig3();
	}

	private boolean findPhrase(int r, int c, String word, int loc, char[][] bo) {
		boolean answer = false;
		// Checks bounds and validity of letter
		if (r >= bo.length || r < 0 || c >= bo[0].length || c < 0)
			return false;
		if (bo[r][c] != word.charAt(loc) && firstWordFound == false) { // char does not match
			return false;
		}
		// The following runs if the first word has been found
		else if (firstWordFound == true && dir == " ") {
			// Checks to see if bounds of r + 1 or c + 1 are valid.
			if (((c + 1) < bo[0].length) && !answer) {
				// If bounds are valid, it checks to see if the letters are equal
				// The other if statements just check different directions
				if (bo[r][c + 1] == word.charAt(loc)) { // right
					c++;
					dir = "right";
					answer = true;
					answer = findPhrase(r, c, word, loc, bo);
				}
			}
			if (((r + 1) < bo.length) && !answer) {
				if (bo[r + 1][c] == word.charAt(loc)) { // down
					r++;

					dir = "down";
					answer = true;
					answer = findPhrase(r, c, word, loc, bo);
				}
			}
			if (((c - 1) >= 0) && !answer) {
				if (bo[r][c - 1] == word.charAt(loc)) { // left
					c--;
					dir = "left";
					answer = true;
					answer = findPhrase(r, c, word, loc, bo);
				}
			}
			if (((r - 1) >= 0) && !answer) {
				if (bo[r - 1][c] == word.charAt(loc)) { // up
					r--;
					dir = "up";
					answer = true;
					answer = findPhrase(r, c, word, loc, bo);
				}
			}
			// Backtrack all the way if nothing was found. It also resets the board to
			// lowercase letters.
			if (!answer) {
				for (int i = 0; i < bo.length; i++) {
					for (int j = 0; j < bo[0].length; j++) {
						bo[i][j] = Character.toLowerCase(bo[i][j]);
					}
				}
				dir = " ";
				startingCoords.set(0, finalCoords.remove(0));
				startingCoords.set(1, finalCoords.remove(0));
				Collections.reverse(foundWords);
				for (String s : foundWords) {
					words.add(0, s);
				}
				firstWordFound = false;
				abort = true;
			}
		}
		// If first word hasn't been found, the following executes instead
		else {
			// Checks to see if letters match
			if (bo[r][c] != word.charAt(loc))
				return false;
			bo[r][c] = Character.toUpperCase(bo[r][c]); // Change it to
			// upper case. This serves two purposes:
			// 1) It will no longer match a lower case char, so it will
			// prevent the same letter from being used twice
			// 2) It will show the word on the board when displayed
			currWord.append(bo[r][c]);
			// If the words are the same, then the word is found and answer is set to true.
			if (loc == word.length() - 1) {
				if (currWord.toString().equalsIgnoreCase(word)) {
					answer = true;
					coords.add(r);
					coords.add(c);
				}
			}
			// If no direction has been determined for the word, it checks all possible
			// directions.
			// If a direction works, that direction is locked in and can't change.
			// This code is a slightly modified version of the code from FindWord.java
			else if (dir == " ") {
				dir = "right";
				answer = findPhrase(r, c + 1, word, loc + 1, bo); // Right
				if (!answer) {
					dir = "down";
					answer = findPhrase(r + 1, c, word, loc + 1, bo); // Down
				}
				if (!answer) {
					dir = "left";
					answer = findPhrase(r, c - 1, word, loc + 1, bo); // Left
				}
				if (!answer) {
					dir = "up";
					answer = findPhrase(r - 1, c, word, loc + 1, bo); // Up

				}

				// If answer was not found, backtrack. Note that in order to
				// backtrack for this algorithm, we need to move back in the
				// board (r and c) and in the word index (loc) -- these are both
				// handled via the activation records, since after the current AR
				// is popped, we revert to the previous values of these variables.
				// However, we also need to explicitly change the character back
				// to lower case before backtracking.
				if (!answer) {
					bo[r][c] = Character.toLowerCase(bo[r][c]);
					currWord.deleteCharAt(currWord.length() - 1);
					dir = " ";
				}
			}
			// This else statement executes if the direction is decided and if the word is
			// not the first word--keeps the word in a straight line.
			else {
				if (dir == "up" && !answer)
					answer = findPhrase(r - 1, c, word, loc + 1, bo);
				if (dir == "down" && !answer)
					answer = findPhrase(r + 1, c, word, loc + 1, bo);
				if (dir == "left" && !answer)
					answer = findPhrase(r, c - 1, word, loc + 1, bo);
				if (dir == "right" && !answer)
					answer = findPhrase(r, c + 1, word, loc + 1, bo);
				if (!answer) {
					bo[r][c] = Character.toLowerCase(bo[r][c]);
					currWord.deleteCharAt(currWord.length() - 1);
					dir = " ";
				}

			}
		}
		return answer;
	}
}
