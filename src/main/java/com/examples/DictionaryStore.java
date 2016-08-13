/*
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Ravi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */

package com.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;

public class DictionaryStore {

	// no of bits used to store each char (a-z)
	private static final int CHAR_SIZE = 5;

	// ascii code of 'a' is 97, offset for char's
	private static final int ASCII_OFFSET = 96;

	// store for 12 letter words
	private ArrayList<Long> longDictionary;

	// store for 24 letter words, 12 letter in each arrayList
	private ArrayList<Long> leftLongDictionary;
	private ArrayList<Long> rightLongDictionary;

	// store for 6 letter words
	private ArrayList<Integer> intDictionary;

	// store for 3 letter words
	private ArrayList<Short> shotDictionary;

	// dictionary words
	// used word file from https://github.com/dwyl/english-words
	String wordFile;
	BitSet set;
	int i = Integer.MAX_VALUE;

	public DictionaryStore(String wordFile) {
		this.wordFile = wordFile;
	}

	/**
	 * Loads all the dictionary words as long, storing words which are less than
	 * 13 characters words more than 12 letters/alphabets can not be stored in a
	 * long two longs will be use to store long words.
	 * 
	 * @throws URISyntaxException
	 */
	public void loadDictionary() throws URISyntaxException {
		longDictionary = new ArrayList<Long>();
		leftLongDictionary = new ArrayList<Long>();
		rightLongDictionary = new ArrayList<Long>();

		try (BufferedReader reader = Files
				.newBufferedReader(Paths.get(ClassLoader.getSystemResource(wordFile).toURI()))) {
			String word;
			while ((word = reader.readLine()) != null) {
				word = word.trim();
				if (word.length() <= 12) {
					// words less than 12 letters are stored in long
					longDictionary.add(encodeWordInLong(word));
				} else if (word.length() > 12 && word.length() <= 24) {
					// words greater than 12 letters and less than 24 letters
					// are stored in two longs
					// if word is between 12 to 24 chars, first 12 chars will be
					// in left dictionary
					leftLongDictionary.add(encodeWordInLong(word.substring(0, 12)));
					// if word is between 12 to 24 chars, chars from 12-24 will
					// be in right dictionary
					rightLongDictionary.add(encodeWordInLong(word.substring(12)));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Collections.sort(longDictionary);
		Collections.sort(leftLongDictionary);
		Collections.sort(rightLongDictionary);
	}

	public static void main(String[] args) throws URISyntaxException {
		String wordFile = "dictionary.txt";
		DictionaryStore dictionary = new DictionaryStore(wordFile);
		dictionary.loadDictionary();
		String searchWord = "developer";
		System.out.println("Found "+searchWord+" "+dictionary.lookUpWord(searchWord));
		dictionary.lookUpWord(searchWord);
		searchWord = "anteconsonantal";
		System.out.println("Found "+searchWord+" "+dictionary.lookUpWord(searchWord));
	}

	public boolean lookUpWord(String searchWord) {
		if (searchWord.length() > 12 && searchWord.length() <= 24) {
			return lookUpLargerThan12charsWord(searchWord);
		}
		long encodedWord = encodeWordInLong(searchWord);
		int found = Arrays.binarySearch(longDictionary.toArray(), encodedWord);
		if (found > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean lookUpLargerThan12charsWord(String searchWord) {
		String leftSearchWord = searchWord.substring(0, 12);
		String rightSearchWord = searchWord.substring(12);
		long leftLong = encodeWordInLong(leftSearchWord);
		int leftFound = Arrays.binarySearch(leftLongDictionary.toArray(), leftLong);
		if (leftFound > 0) {
			// now search in right long
			long rightLong = encodeWordInLong(rightSearchWord);
			int rightFound = Arrays.binarySearch(rightLongDictionary.toArray(), rightLong);
			if (rightFound > 0) {
				return true;
			}
		}

		return false;

	}

	/**
	 * Convert word encoded as long to String
	 * 
	 * @param value
	 * @return
	 */
	public String decodeWordfromLong(long value) {
		StringBuffer word = new StringBuffer();
		while (value != 0) {
			// take out bits needed to decode each char
			int tmp = (int) value & 0x1F;
			// shift processed bits right
			value = value >> CHAR_SIZE;
			// add offset to form char
			tmp = tmp + ASCII_OFFSET;
			word.append((char) tmp);
		}
		return word.toString();
	}

	/**
	 * converts string to long, it can handle words of size equal or less than
	 * 12 characters
	 * 
	 * @param word
	 * @return
	 */
	public long encodeWordInLong(String word) {
		long result = 0L;

		char letters[] = word.toCharArray();
		for (int i = 0; i < letters.length; i++) {
			long val = letters[i] - ASCII_OFFSET;
			int shift = i * CHAR_SIZE;
			val = val << shift;
			result = result | val;
		}
		return result;
	}
}
