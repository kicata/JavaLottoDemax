package eurobetKrystoslovicaCheck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class program {

	public static void main(String[] args) {
		final String DATA = "1000001,2301000001,2301000001,23B5TU8ZQY,2313275007593,834238609,3,"
				+ "Б,Щ,И,Ж,Д,Г,М,У,Х,Н,З,П,Ф,К,В,А,Р,Е,Й,Л,"
				+ "q,Ч,О,В,К,А,q,О,q,q,q,q,q,q,q,О,q,q,К,q,Л,q,a,q,Ж,q,Р,А,Л,О,q,Е,q,К,q,И,q,И,q,q,q,q,Й,q,В,З,В,О,Д,q,А,Ф,Е,К,Т,А,q,О,q,О,q,Н,q,q,А,q,Л,q,"
				+ "П,И,Р,А,Т,q,q,q,С,А,q,и,q,q,q,Р,q,Б,q,Л,Н,е,С,q,q,q,А,q,О,С,А,Г,q,q,К,А,С,К,Е,Т,q,М,q,q,q,q,q,q,Т,q,И,q,А,"
				+ "П,О,Л,Я,Н,А";
		ArrayList<String> letters = new ArrayList<>();
		ArrayList<String> matrixLetters = new ArrayList<>();
		StringBuilder bonus = new StringBuilder();

		List<String> words = new ArrayList<>();
		final int ROWS = 11;
		final int COLS = 11;
		String[][] matrix = new String[ROWS][COLS];
		
		String[] data = DATA.split(",");
		// populate
		for (int i = 0; i < data.length; i++) {
			if (i > 6 && i < 27) {
				letters.add(data[i]);

			} else if (i > 26 && i < 148) {
				matrixLetters.add(data[i]);

			} else if (i > 147) {
				bonus.append(data[i]);
			}
		}

		int index = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				matrix[i][j] = matrixLetters.get(index);
				index++;
			}
		}
		
		Collections.sort(letters);
		words = extractWords(matrix, ROWS);
		Collections.sort(words);
		System.out.println("Stop");
		
		boolean isBonusMatch = canFormBonusWord(bonus.toString(), letters);
		Integer[] matches = countWords(words, letters);
		int winCalc = calculateWin(matches[0], matches[1]);
		if (isBonusMatch) {
			winCalc += 20;
		}
		System.out.println(winCalc);
		

	}

	private static Integer calculateWin(int wordCount, int doubleCount) {
		int result = 0;
		switch (wordCount) {
		case 0:
			result += 0;
			break;
		case 1:
			result += 0;
			break;
		case 2:
			result += 3;
			break;
		case 3:
			result += 5;
			break;
		case 4:
			result += 10;
			break;
		case 5:
			result += 50;
			break;
		case 6:
			result += 100;
			break;
		case 7:
			result += 200;
			break;
		case 8:
			result += 1000;
			break;
		case 9:
			result += 10000;
			break;
		case 10:
			result += 250000;
			break;

		default:
			break;
		}

		if (doubleCount > 0) {
			for (int i = 0; i < doubleCount; i++) {
				result *= 2;
			}
		}

		return result;
	}

	private static boolean canFormBonusWord(String bonus, List<String> letters) {
		boolean canForm = false;
		int wordLength = bonus.length();
		int matchCounter = 0;
		for (int i = 0; i < wordLength; i++) {
			String letter = String.valueOf(bonus.charAt(i));
			if (isLetterFound(letter, letters)) {
				matchCounter++;
			}
			if (wordLength - matchCounter == 0) {
				canForm = true;
				
				break;
			}

		}

		return canForm;
	}

	private static Integer[] countWords(List<String> words, List<String> letters) {
		Integer[] result = new Integer[2];
		int wordCounter = 0;
		int doubleCounter = 0;
		for (String word : words) {
			boolean isContainStar = false;
			int matchCounter = 0;
			int wordLength = word.length();
			for (int i = 0; i < wordLength; i++) {
				String letter = String.valueOf(word.charAt(i));
				
				if (letter.equals(letter.toLowerCase())) {
					isContainStar = true;
				}
				if (isLetterFound(letter, letters)) {
					matchCounter++;
				}
				if ((wordLength - matchCounter == 1) && isContainStar) {
					wordCounter++;
					doubleCounter++;
					break;
				}

				if (wordLength - matchCounter == 0) {
					wordCounter++;
					break;
				}
			}

		}

		result[0] = wordCounter;
		result[1] = doubleCounter;
		return result;
	}
	
	private static boolean isLetterFound(String ch, List<String> letters){
		boolean isFound = false;
		for (String letter : letters) {
			if (letter.equals(ch)) {
				isFound = true;
				break;
			}
		}
		
		return isFound;

	}

	private static List<String> extractWords(String[][] matrix, int size) {
		ArrayList<String> curnWord = new ArrayList<>();
		List<String> extracted = new ArrayList<>();
		// horizontal
		for (int row = 0; row < matrix.length; row++) {
			curnWord.clear();
			for (int col = 0; col < matrix[row].length; col++) {

				String letter = matrix[row][col];
				if (!matrix[row][col].equals("q")) {

					curnWord.add(matrix[row][col]);

				} else {
					if (curnWord.size() > 2) {
						String word = constructWord(curnWord);
						extracted.add(word);
						curnWord.clear();
					} else {
						curnWord.clear();
					}
				}

			}
			if (curnWord.size() > 2) {
				String word = constructWord(curnWord);
				extracted.add(word);
			}

		}
		// vertical
		for (int col = 0; col < matrix.length; col++) {
			curnWord.clear();
			for (int row = 0; row < matrix[col].length; row++) {

				String letter = matrix[row][col];
				if (!matrix[row][col].equals("q")) {

					curnWord.add(matrix[row][col]);

				} else {
					if (curnWord.size() > 2) {
						String word = constructWord(curnWord);
						extracted.add(word);
						curnWord.clear();
					} else {
						curnWord.clear();
					}
				}

			}
			if (curnWord.size() > 2) {
				String word = constructWord(curnWord);
				extracted.add(word);
			}

		}

		return extracted;

	}

	private static String constructWord(List<String> letters) {
		StringBuilder sb = new StringBuilder();
		for (String str : letters) {
			sb.append(str);
		}

		return sb.toString();
	}

}
