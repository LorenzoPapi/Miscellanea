package com.github.lorenzopapi.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteForce {

	private static final List<String> letters = new ArrayList<>();
	private static final List<String> coolWords = new ArrayList<>();

	public static void main(String[] args) {
		letters.add("A");
		letters.add("B");
		letters.add("C");
		letters.add("D");
		letters.add("E");
		letters.add("F");
		letters.add("G");
		letters.add("I");
		letters.add("L");
		letters.add("M");
		letters.add("N");
		letters.add("O");
		letters.add("P");
		letters.add("R");
		letters.add("S");
		letters.add("T");
		letters.add("U");
		letters.add("V");
		letters.add("Z");
		getAllLists(letters, 5).forEach((s) -> {
			if (!isVocal(valueOf(at(s,0)))) {
				if (isVocal(valueOf(at(s, 1)))) {
					if (!isVocal(valueOf(at(s, 2)))) {
						if (!isVocal(valueOf(at(s, 3)))) {
							if (isVocal(valueOf(at(s, 4)))) {
								if (valueOf(at(s, 2)).equals(valueOf(at(s, 3)))) {
									if (exists(s)) {
										if (!valueOf(at(s, 0)).equals(valueOf(at(s, 3)))) {
											if (!valueOf(at(s, 1)).equals(valueOf(at(s, 4)))) {
												if (letters.indexOf(valueOf(at(s, 1))) < letters.indexOf(valueOf(at(s, 0)))) {
													if (letters.indexOf(valueOf(at(s, 0))) < letters.indexOf(valueOf(at(s, 2)))) {
														if (letters.indexOf(valueOf(at(s, 0))) < letters.indexOf(valueOf(at(s, 4)))) {
															if (letters.indexOf(valueOf(at(s, 4))) < letters.indexOf(valueOf(at(s, 2))))
																coolWords.add(s);
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		});
		
		System.out.println(coolWords);
		System.out.println(coolWords.size());
	}

	private static char at(String s, int index) {
		return s.charAt(index);
	}

	private static String valueOf(char c) {
		return String.valueOf(c);
	}

	public static List<String> getAllLists(List<String> elements, int lengthOfList)
	{
		//initialize our returned list with the number of elements calculated above
		List<String> allLists = Arrays.asList(new String[(int) Math.pow(elements.size(), lengthOfList)]);

		//lists of length 1 are just the original elements
		if (lengthOfList == 1) return elements;
		else {
			//the recursion--get all lists of length 3, length 2, all the way up to 1
			List<String> allSublists = getAllLists(elements, lengthOfList - 1);

			//append the sublists to each element
			int arrayIndex = 0;

			for (String element : elements) {
				for (String allSublist : allSublists) {
					//add the newly appended combination to the list
					allLists.set(arrayIndex, element + allSublist);
					arrayIndex++;
				}
			}

			return allLists;
		}
	}

	private static boolean isVocal(String l) {
		return l.equals("A") || l.equals("E") || l.equals("I") || l.equals("O") || l.equals("U");
	}

	private static boolean exists(String l) {
		return valueOf(at(l, 1)).equals("A") && valueOf(at(l, 4)).equals("O") && !l.startsWith("M") && !l.startsWith("L") && !valueOf(at(l, 2)).equals("V");
	}
}
