/*
 * Copyright (c) 2012-2017 Codestorming.org
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Codestorming - initial API and implementation
 */
package org.codestorming.strings;

import java.io.Serializable;

/**
 * {@code TextBuilder} is a modifiable {@link CharSequence}.
 *
 * @author Thaedrik [thaedrik@codestorming.org]
 */
public class TextBuilder implements CharSequence, Serializable {

	private static final long serialVersionUID = 6316060788814783981L;

	/**
	 * Default character used for indentation.
	 */
	private static final char DEFAULT_INDENT = '\t';

	/**
	 * Backing char array of this char sequence.
	 */
	private char[] chars;

	/**
	 * The length of the char sequence.
	 * <p>
	 * This length is lower or equal to the chars array's size.
	 */
	private int length;

	/**
	 * Current indentation.
	 * <p>
	 * The content of the indent {@code TextBuilder} will be added before each line of new
	 * added content.
	 */
	private TextBuilder indent;

	/**
	 * Creates a new {@code TextBuilder} with an initial capacity of {@code 10}.
	 */
	public TextBuilder() {
		this(10);
	}

	/**
	 * Creates a new {@code TextBuilder} with the specified initial capacity.
	 * 
	 * @param capacity The initial capacity.
	 */
	public TextBuilder(int capacity) {
		this(new char[capacity], 0, true);
	}

	private TextBuilder(int capacity, boolean indent) {
		this(new char[capacity], 0, indent);
	}

	private TextBuilder(char[] chars, int length, boolean indent) {
		this.chars = chars;
		this.length = length;
		if (indent) {
			this.indent = new TextBuilder(4, false);
		}
	}

	/**
	 * Returns the number of characters the current indentation has.
	 * 
	 * @return the number of characters the current indentation has.
	 */
	public int getIndentSize() {
		return indent.length;
	}

	/**
	 * Appends the given {@link CharSequence} to the end of this {@code TextBuilder}.
	 * 
	 * @param charSequence The {@link CharSequence} to append.
	 * @return this {@code TextBuilder}.
	 */
	public TextBuilder append(CharSequence charSequence) {
		if (charSequence instanceof TextBuilder) {
			TextBuilder tb = (TextBuilder) charSequence;
			append(tb.chars, tb.length);
		} else {
			char[] characters = charSequence.toString().toCharArray();
			append(characters, characters.length);
		}
		return this;
	}

	/**
	 * Appends the given {@code Object}'s string convertion to the end of this
	 * {@code TextBuilder}.
	 * <p>
	 * That is, the {@link Object#toString() toString()} method is called on the given
	 * object and the result is appended to this {@code TextBuilder}.
	 * <p>
	 * Does <strong>nothing</strong> if the given object is {@code null}.
	 * 
	 * @param obj The object to convert to a string and to append.
	 * @return this {@code TextBuilder}.
	 */
	public TextBuilder append(Object obj) {
		if (obj != null) {
			char[] characters = obj.toString().toCharArray();
			append(characters, characters.length);
		}
		return this;
	}

	/**
	 * Appends the given character to the end of this {@code TextBuilder}.
	 * <p>
	 * To create a new line, prefer using the {@link #newLine()} method.
	 * 
	 * @param c The character to append.
	 * @return this {@code TextBuilder}.
	 */
	public TextBuilder append(char c) {
		if (indent.length > 0 && isNewLine() && c != '\n') {
			// First line indent if it is not empty
			insert(length, indent.chars, indent.length);
		}
		return append0(c);
	}

	private TextBuilder append0(char c) {
		ensureCapacity(1);
		chars[length] = c;
		length++;
		return this;
	}

	/**
	 * Indicates if the current line is a new one. That is, if the last character is a
	 * line feed ({@code '\n'}).
	 * 
	 * @return {@code true} if the current line is a new one;<br>
	 *         {@code false} otherwise.
	 */
	public boolean isNewLine() {
		return length == 0 || chars[length - 1] == '\n';
	}

	private void append(char[] chars, int length) {
		final char lf = '\n';
		final char cr = '\r';
		if (indent.length > 0 && length > 0 && isNewLine() && chars[0] != lf && (chars[0] != cr || chars[1] != lf)) {
			// First line indent if it is not empty
			insert(this.length, indent.chars, indent.length);
		}
		ensureCapacity(length);
		System.arraycopy(chars, 0, this.chars, this.length, length);
		int offset = this.length;
		this.length += length;
		if (indent.length > 0 && length > 0) {
			// We do not indent if the line feed is the last character
			for (int i = 0; i < length - 1; i++) {
				if (chars[i] == lf) {
					insert(i + 1 + offset, indent.chars, indent.length);
					offset += indent.length;
				}
			}
		}
	}

	/**
	 * Inserts the given character at the specified {@code index}.
	 * 
	 * @param index The index where the character will be inserted.
	 * @param c The character to insert.
	 * @return this {@code TextBuilder}.
	 */
	public TextBuilder insert(int index, char c) {
		return insert(index, new char[] { c }, 1);
	}

	/**
	 * Inserts {@code length} number of the given characters into this {@code TextBuilder}
	 * at the specified {@code index}.
	 * 
	 * @param index The index where the characters will be inserted.
	 * @param chars The characters to insert.
	 * @param length The number of characters to insert.
	 * @return this {@code TextBuilder}.
	 */
	public TextBuilder insert(int index, char[] chars, int length) {
		if (index < 0 || index > this.length) {
			throw new IndexOutOfBoundsException(String.valueOf(index));
		}// else
		ensureCapacity(length);
		System.arraycopy(this.chars, index, this.chars, index + length, this.length - index);
		System.arraycopy(chars, 0, this.chars, index, length);
		this.length += length;
		return this;
	}

	/**
	 * Deletes the {@code length} number of characters from this {@code TextBuilder}
	 * starting at the given {@code start} index.
	 * 
	 * @param start The starting index of the characters to delete.
	 * @param length The number of characters to delete.
	 * @return this {@code TextBuilder}.
	 * @throws IndexOutOfBoundsException if {@code start >= this.length()}.
	 */
	public TextBuilder delete(int start, int length) {
		if (start < 0 || start >= this.length) {
			throw new IndexOutOfBoundsException(String.valueOf(start));
		}// else
		if (start + length >= this.length) {
			this.length = start;
		} else {
			System.arraycopy(chars, start + length, chars, start, this.length - (start + length));
			this.length -= length;
		}
		return this;
	}

	/**
	 * Deletes the characters from this {@code TextBuilder} starting at the given
	 * {@code index} to the end.
	 * 
	 * @param index The starting index of the characters to delete.
	 * @return this {@code TextBuilder}.
	 * @throws IndexOutOfBoundsException if {@code index >= this.length()}.
	 */
	public TextBuilder delete(int index) {
		if (index < 0 || index >= length) {
			throw new IndexOutOfBoundsException(String.valueOf(index));
		}// else
		System.arraycopy(chars, index + 1, chars, index, length - index - 1);
		length--;
		return this;
	}

	/**
	 * Deletes the first character of this {@code TextBuilder}.
	 * <p>
	 * Does nothing if this {@code TextBuilder} is empty.
	 * <p>
	 * ATTENTION : If the first character is a line feed or a tabulation, or any other
	 * special characters, it will be removed.
	 * 
	 * @return this {@code TextBuilder}.
	 */
	public TextBuilder deleteFirst() {
		if (length == 0) {
			return this;
		}// else
		return delete(0);
	}

	/**
	 * Deletes the last character of this {@code TextBuilder}.
	 * <p>
	 * Does nothing if this {@code TextBuilder} is empty.
	 * <p>
	 * ATTENTION : If the last character is a line feed or a tabulation, or any other
	 * special characters, it will be removed.
	 * 
	 * @return this {@code TextBuilder}.
	 */
	public TextBuilder deleteLast() {
		if (length == 0) {
			return this;
		}// else
		return delete(length - 1);
	}

	/**
	 * Increment the indentation size.
	 * <p>
	 * That is, the next time a line is created, the indentation will be inserted before
	 * the text of the line starts.
	 * 
	 * @return this {@code TextBuilder}.
	 * @see TextBuilder#decrementIndent()
	 */
	public TextBuilder incrementIndent() {
		indent.append0(DEFAULT_INDENT);
		return this;
	}

	/**
	 * Decrement the indentation size.
	 * <p>
	 * That is, the next time a line is created, the indentation will be inserted before
	 * the text of the line starts, unless the indentation size is {@code 0}.
	 * 
	 * @return this {@code TextBuilder}.
	 * @see TextBuilder#incrementIndent()
	 */
	public TextBuilder decrementIndent() {
		indent.deleteLast();
		return this;
	}

	/**
	 * Creates a new line at the end of this {@code TextBuilder}.
	 * 
	 * @return this {@code TextBuilder}.
	 */
	public TextBuilder newLine() {
		return append0('\n');
	}

	/**
	 * Creates a whiteline at the end of this {@code TextBuilder}.
	 * <p>
	 * This means, there must be two line feed at the end.
	 * <p>
	 * If the last line is considered a white line, this method does nothing.
	 * 
	 * @return this {@code TextBuilder}.
	 */
	public TextBuilder whiteLine() {
		if (chars[length - 1] != '\n') {
			append0('\n').append0('\n');
		} else if (chars[length - 2] != '\n' && (chars[length - 2] != '\r' || chars[length - 3] != '\n')) {
			append0('\n');
		}
		return this;
	}

	@Override
	public int length() {
		return length;
	}

	@Override
	public char charAt(int index) {
		if (index < 0 || index >= length) {
			throw new IndexOutOfBoundsException(String.valueOf(index));
		}// else
		return chars[index];
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		if (start < 0 || end > length || start > end) {
			throw new IndexOutOfBoundsException("start : " + start + ", end : " + end + ", actual length = " + length);
		}// else
		int len = end - start;
		char[] newChars = new char[len];
		System.arraycopy(chars, start, newChars, 0, len);
		return new TextBuilder(newChars, len, true);
	}

	private void ensureCapacity(int addition) {
		final int minCapacity = length + addition;
		if (minCapacity > chars.length) {
			int newCapacity = (int) (1.75 * chars.length) + 1;
			if (newCapacity < minCapacity) {
				newCapacity = minCapacity;
			}
			char[] newChars = new char[newCapacity];
			System.arraycopy(chars, 0, newChars, 0, length);
			chars = newChars;
		}
	}

	@Override
	public String toString() {
		return new String(chars, 0, length);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}// else
		if (!(obj instanceof CharSequence)) {
			return false;
		}// else
		CharSequence sequence = (CharSequence) obj;
		if (length != sequence.length()) {
			return false;
		}// else
		if (length == 0) {
			return true;
		}// else
		if (obj instanceof TextBuilder) {
			char[] chars0 = chars;
			char[] chars1 = ((TextBuilder) obj).chars;
			int i, n;
			for (i = 0, n = length; i < n && chars0[i] == chars1[i]; i++);
			return i == n;
		}// else
		int i, n;
		for (i = 0, n = sequence.length(); i < n && chars[i] == sequence.charAt(i); i++);
		return i == n;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		int len = length;
		if (len > 0) {
			char val[] = chars;
			for (int i = 0; i < len; i++) {
				hash = 31 * hash + val[i];
			}
		}
		return hash;
	}
}
