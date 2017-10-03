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

/**
 * Utility class for String operations.
 * 
 * @author Thaedrik [thaedrik@codestorming.org]
 */
public class Strings {

	/**
	 * Removes all <strong>trim</strong> strings at the beginning and at the end of the
	 * given String <em>str</em>.
	 * <p>
	 * If {@code trim} is empty or if there is no {@code trim} string in {@code str},
	 * nothing happens.
	 * 
	 * @param str The string to trim.
	 * @param trim The string to remove from {@code str}.
	 * @return the trimmed string.
	 * @throws NullPointerException if {@code str} or {@code trim} are {@code null}.
	 */
	public static String trim(String str, String trim) {
		return trim(str, trim, -1);
	}

	/**
	 * Removes {@code number} <strong>trim</strong> strings at the beginning and at the
	 * end of the given String <em>str</em>.
	 * <p>
	 * If {@code trim} is empty or if there is no {@code trim} string in {@code str},
	 * nothing happens.
	 * 
	 * @param str The string to trim.
	 * @param trim The string to remove from {@code str}.
	 * @param number The number of {@code trim} strings to remove from the beginning and
	 *        the end of {@code str}.
	 * @return the trimmed string.
	 * @throws NullPointerException if {@code str} or {@code trim} are {@code null}.
	 */
	public static String trim(String str, String trim, int number) {
		if (str == null || trim == null) {
			throw new NullPointerException();
		}// else
		final int trimLength = trim.length();
		String trimmed = str;
		if (trimLength > 0) {
			final boolean infinite = number < 0;
			int start = 0;
			int n;

			// Removing the trim string at the beginning
			for (n = 0; trimmed.startsWith(trim, start) && (infinite || n < number); n++) {
				start += trimLength;
			}
			int end = trimmed.length();

			if (end > start) {
				// Removing the trim string at the end
				for (n = 0; endsWith(trimmed, trim, end) && (infinite || n < number); n++) {
					end -= trimLength;
				}
			}
			trimmed = trimmed.substring(start, end);
		}
		return trimmed;
	}

	/**
	 * Tests if the {@code str} string ends with the specified suffix at the given
	 * {@code length}.
	 * <p>
	 * Equivalent to do <code>str.substring(0, length).endsWith(suffix);</code>
	 * 
	 * @param str The string to test.
	 * @param suffix the suffix.
	 * @param length The length indicating the end of {@code str}.
	 * @return {@code true} if the {@code str} string ends with the specified suffix at
	 *         the given {@code length};<br>
	 *         {@code false} otherwise.
	 * @throws NullPointerException if {@code str} or {@code suffix} are {@code null}.
	 * @throws IndexOutOfBoundsException if {@code length} is larger than the length of
	 *         the {@code str} string.
	 */
	public static boolean endsWith(String str, String suffix, int length) {
		if (str == null || suffix == null) {
			throw new NullPointerException();
		}// else
		return str.substring(0, length).endsWith(suffix);
	}

	/**
	 * Tests if the given strings {@code a} and {@code b} are equal.
	 * <p>
	 * If {@code nullEqualsEmpty} is {@code true} then an empty string equals {@code null}.
	 * 
	 * @param a The first string to compare to {@code b}.
	 * @param b The second string to compare to {@code a}.
	 * @param nullEqualsEmpty Indicates if {@code null} is like an empty string.
	 * @return {@code true} if {@code a} equals {@code b};<br>
	 *         {@code false} otherwise.
	 */
	public static boolean equals(String a, String b, boolean nullEqualsEmpty) {
		if (a == null || b == null) {
			if (nullEqualsEmpty) {
				return a == b || (a != null ? a.length() == 0 : b.length() == 0);
			}// else
			return a == b;
		}// else
		return a.equals(b);
	}

	/**
	 * Set the first character of the given CharSequence in upper case.
	 * 
	 * @param str The character sequence.
	 * @return a new char sequence with its first character in upper case.
	 */
	public static CharSequence toUpperFirst(CharSequence str) {
		if (str.length() < 1) {
			return str;
		}// else
		if (str instanceof StringBuilder) {
			StringBuilder sb = (StringBuilder) str;
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
			return str;
		}// else
		return toUpperFirst(new StringBuilder(str));
	}

	/**
	 * Set the first character of the given CharSequence in lower case.
	 * 
	 * @param str The character sequence.
	 * @return a new char sequence with its first character in lower case.
	 */
	public static CharSequence toLowerFirst(CharSequence str) {
		if (str.length() < 1) {
			return str;
		}// else
		if (str instanceof StringBuilder) {
			StringBuilder sb = (StringBuilder) str;
			sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
			return str;
		}// else
		return toLowerFirst(new StringBuilder(str));
	}

	// Suppressing default constructor, ensuring non-instantiability
	private Strings() {}
}
