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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DictionaryStore {

	// no of bits used to store each char (a-z)
	private static final int CHAR_SIZE = 5;

	// ascii code of 'a' is 97, offset for char's
	private static final int ASCII_OFFSET = 96;

	//store words as long 
	private ArrayList<Long> longDictionary;
	
	// dictionary words 
	// used word file from https://github.com/dwyl/english-words
	String wordFile;

	public DictionaryStore(String wordFile) {
		this.wordFile = wordFile;
	}
	/**
	 * Loads all the dictionary words as long
	 * as we are storing words which are less than 13 characters
	 * words more than 12 letters/alphabets can not be stored in a long
	 * we need to use 2 longs to store long words. 
	 * @throws URISyntaxException
	 */
	public void loadDictionary() throws URISyntaxException {
		try (Stream<String> stream = Files.lines(Paths.get(ClassLoader.getSystemResource(wordFile).toURI()), Charset.defaultCharset())) {
			longDictionary = (ArrayList<Long>) stream.filter(line -> line.length() < 13).map(String::trim)
					.map(this::encodeWordInLong).collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		Collections.sort(longDictionary);
	}

	public static void main(String[] args) throws URISyntaxException {
		String wordFile = "dictionary.txt";
		DictionaryStore dictionary = new DictionaryStore(wordFile);
		dictionary.loadDictionary();
		String searchWord = "developer";
		dictionary.lookUpWord(searchWord);
	}

	public boolean lookUpWord(String searchWord) {
		long encodedWord = encodeWordInLong(searchWord);
		int found = Arrays.binarySearch(longDictionary.toArray(), encodedWord);
		if (found > 0) {
			System.out.println("Valid Word");
			return true;
		} else {
			System.out.println("Invalid word ");
			return false;
		}
	}

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
	 * converts string to long, it can handle words of size 
	 * equal or less than 12 characters
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
