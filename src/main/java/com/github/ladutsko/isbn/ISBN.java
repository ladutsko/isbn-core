/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023 George Ladutsko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.github.ladutsko.isbn;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ISBN
 *
 * @author <a href="mailto:ladutsko@gmail.com">George Ladutsko</a>
 */
public class ISBN implements Serializable {

  private static final long serialVersionUID = 174660743699030960L;

  private static final Logger LOGGER = LoggerFactory.getLogger(ISBN.class);

  public static final Pattern PATTERN = Pattern.compile("\\b(?:(97[89](?:\\s*[^\\s\\dXx]?\\s*\\d){10})|((?:\\d\\s*[^\\s\\dXx]?\\s*){9}[\\dXx]))\\b");

  private static final Pattern PATTERN_WITHOUT_CHECK_DIGIT = Pattern.compile("\\b(?:(97[89](?:\\s*[^\\s\\dXx]?\\s*\\d){9,10})|((?:\\d\\s*[^\\s\\dXx]?\\s*){9}[\\dXx]?))\\b");

  private static final Pattern GROUP_SEPARATOR_PATTERN = Pattern.compile("[^\\dXx]+");

  private static final String URI_PREFIX = "urn:isbn:";

  static final String DEFAULT_PREFIX = "978";

  private final String isbn13;
  private transient String isbn10;

  ISBN(final String isbn13, final String isbn10) {
    this.isbn13 = isbn13;
    this.isbn10 = isbn10;
  }

  /**
   * @return normalized ISBN-13 string
   */
  public String getIsbn13() {
    return isbn13;
  }

  /**
   * @return normalized ISBN-10 string
   */
  public String getIsbn10() {
    return isbn10;
  }

  /**
   * @return urn
   */
  public URI toURI() {
    return URI.create(URI_PREFIX + isbn13);
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object.
   */
  @Override
  public String toString() {
    return String.format("%s[isbn13=%s,isbn10=%s]", getClass().getName(), isbn13, (null == isbn10 ? "nonexistent" : isbn10));
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for this object.
   */
  @Override
  public int hashCode() {
    return isbn13.hashCode();
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @return {@code true} if this object is the same as the obj argument;
   *         {@code false} otherwise.
   */
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (obj == null)
      return false;

    if (!(obj instanceof ISBN))
      return false;

    final ISBN other = (ISBN) obj;
    return isbn13.equals(other.isbn13);
  }

  /**
   * Parse ISBN
   *
   * @param input character sequence which contains ISBN
   * @return ISBN
   * @throws ISBNException if something is wrong
   */
  public static ISBN parseIsbn(final CharSequence input) throws ISBNException {
    LOGGER.debug("Start parseIsbn with params [input = {}]", input);

    if (null == input || 0 == input.length())
      throw new IllegalArgumentException("isbn = " + input);

    Matcher m = matcher(input, PATTERN);
    LOGGER.debug("Matcher: {}", m);

    assert 2 == m.groupCount() : "Unexpected groups count: " + m.groupCount();

    if (null != m.group(1)) {
      String isbn13 = normalize(m.group());
      char checkDigit = calculateCheckDigit13(isbn13);
      if (checkDigit != isbn13.charAt(12))
        throw new ISBNException("Suspect check digit " + checkDigit + ": " + input);

      ISBN isbn = new ISBN(isbn13, toIsbn10(isbn13));
      LOGGER.debug("Return: {}", isbn);
      return isbn;
    } else {
      String isbn10 = normalize(m.group()).toUpperCase();
      char checkDigit = calculateCheckDigit10(isbn10);
      if (checkDigit != isbn10.charAt(9))
        throw new ISBNException("Suspect check digit " + checkDigit + ": " + input);

      ISBN isbn = new ISBN(toIsbn13(isbn10), isbn10);
      LOGGER.debug("Return: {}", isbn);
      return isbn;
    }
  }

  /**
   * Creates a matcher that will match the given input against ISBN pattern
   *
   * @param input character sequence which contains ISBN
   * @return new matcher for ISBN pattern or null if input is null
   * @throws ISBNException if something is wrong
   */
  public static Matcher matcher(final CharSequence input) throws ISBNException {
    if (null == input)
      return null;

    return matcher(input, PATTERN);
  }

  /**
   * Normalize ISBN
   *
   * @param input character sequence which contains ISBN
   * @return normalize ISBN string or null if input is null
   */
  public static String normalize(final CharSequence input) {
    if (null == input)
      return null;

    return GROUP_SEPARATOR_PATTERN.matcher(input).replaceAll("");
  }

  /**
   * Calculate ISBN check digit
   *
   * @param input character sequence which contains ISBN
   * @return check digit or null if input is null
   * @throws ISBNException if something is wrong
   */
  public static String calculateCheckDigit(final CharSequence input) throws ISBNException {
    if (null == input)
      return null;

    Matcher m = matcher(input, PATTERN_WITHOUT_CHECK_DIGIT);
    LOGGER.debug("Matcher: {}", m);

    assert 2 == m.groupCount() : "Unexpected groups count: " + m.groupCount();

    if (null != m.group(1)) {
      return String.valueOf(calculateCheckDigit13(normalize(m.group())));
    } else {
      return String.valueOf(calculateCheckDigit10(normalize(m.group())));
    }
  }

  /**
   * Validate ISBN
   *
   * @param input character sequence which contains ISBN
   * @return true if input contains valid ISBN
   */
  public static boolean isValid(final CharSequence input) {
    try {
      String checkDigit = calculateCheckDigit(input);
      if (null == checkDigit)
        return false;

      return (checkDigit.charAt(0) == Character.toUpperCase(input.charAt(input.length()-1)));
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Validate ISBN-13
   *
   * @param input character sequence which contains ISBN
   * @return true if input is ISBN-13
   */
  public static boolean isIsbn13(final CharSequence input) {
    if (null == input)
      return false;

    try {
      return null != matcher(input, PATTERN).group(1);
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Validate ISBN-10
   *
   * @param input character sequence which contains ISBN
   * @return true if input is ISBN-10
   */
  public static boolean isIsbn10(final CharSequence input) {
    if (null == input)
      return false;

    try {
      return null != matcher(input, PATTERN).group(2);
    } catch (Exception e) {
      return false;
    }
  }

  protected static Matcher matcher(final CharSequence input, final Pattern pattern) throws ISBNException {
    Matcher m = pattern.matcher(input);
    if (!m.matches())
      throw new ISBNException("ISBN is not well-formed: " + input);

    return m;
  }

  protected static char calculateCheckDigit13(final CharSequence input) {
    int sum = 0;
    for (int i = 12; 0 <= --i; )
      sum += (0 == i % 2 ? 1 : 3) * (input.charAt(i) - 48);
    int checkDigit = (10 - sum % 10) % 10;
    return (char) (checkDigit + 48);
  }

  protected static char calculateCheckDigit10(final CharSequence input) {
    int sum = 0;
    for (int i = 9; 0 <= --i; )
      sum += (i + 1) * (input.charAt(i) - 48);
    int checkDigit = sum % 11;
    return (10 == checkDigit ? 'X' : (char) (checkDigit + 48));
  }

  protected static String toIsbn10(final String input) {
    if (input.startsWith(DEFAULT_PREFIX)) {
      StringBuilder sb = new StringBuilder(10);
      sb.append(input, 3, 12);
      sb.append(calculateCheckDigit10(sb));
      return sb.toString();
    }

    return null;
  }

  protected static String toIsbn13(final String input) {
    StringBuilder sb = new StringBuilder(13);
    sb.append(DEFAULT_PREFIX).append(input, 0, 9);
    sb.append(calculateCheckDigit13(sb));
    return sb.toString();
  }

  private void writeObject(final ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
  }

  private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();

    isbn10 = toIsbn10(isbn13);
  }
}
