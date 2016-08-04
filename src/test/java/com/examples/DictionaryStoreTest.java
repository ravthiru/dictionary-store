package com.examples;

import org.junit.Test;
import static org.junit.Assert.*;


public class DictionaryStoreTest {

	@Test
	public void testEncodeWordInLong() {
		String word = "world";
		DictionaryStore ds = new DictionaryStore("");
		long longWord = ds.encodeWordInLong(word);
		assertTrue(longWord>0L);
	}
	
	@Test
	public void testEncodeAndDecode() {
		String word = "hello";
		DictionaryStore ds = new DictionaryStore("");
		long longWord = ds.encodeWordInLong(word);
		String decodedWord = ds.decodeWordfromLong(longWord);
		assertEquals(word, decodedWord);
	}
	
	@Test
	public void testEncodeAndDecodeWith1Char1() {
		String word = "a";
		DictionaryStore ds = new DictionaryStore("");
		long longWord = ds.encodeWordInLong(word);
		String decodedWord = ds.decodeWordfromLong(longWord);
		assertEquals(word, decodedWord);
	}
	
	@Test
	public void testEncodeAndDecodeWith1Char2() {
		String word = "z";
		DictionaryStore ds = new DictionaryStore("");
		long longWord = ds.encodeWordInLong(word);
		String decodedWord = ds.decodeWordfromLong(longWord);
		assertEquals(word, decodedWord);
	}
	
	@Test
	public void testEncodeAndDecodeWith12Char() {
		String word = "abcdefghijkl";
		DictionaryStore ds = new DictionaryStore("");
		long longWord = ds.encodeWordInLong(word);
		String decodedWord = ds.decodeWordfromLong(longWord);
		assertEquals(word, decodedWord);
	}
	
	@Test
	public void testDecodeWordfromLong() {
		long world = 4606455L; // "world" encoded as long
		DictionaryStore ds = new DictionaryStore("");
		String decodedWord = ds.decodeWordfromLong(world);
		assertEquals("world", decodedWord);
	}
	
}
