/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013-2025 George Ladutsko
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

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;

import org.junit.Test;

/**
 * @author <a href="mailto:ladutsko@gmail.com">George Ladutsko</a>
 */
public class ISBNTest {

  @Test(expected=IllegalArgumentException.class)
  public void parseIsbnThrowIllegalArgumentExceptionForNull() throws Exception {
    ISBN.parseIsbn(null);
  }

  @Test(expected=IllegalArgumentException.class)
  public void parseIsbnThrowIllegalArgumentExceptionForEmpty() throws Exception {
    ISBN.parseIsbn("");
  }

  @Test(expected=ISBNException.class)
  public void parseIsbnThrowISBNExceptionForNotWelFormed() throws Exception {
    ISBN.parseIsbn("qwertyuiop");
  }

  @Test(expected=ISBNException.class)
  public void parseIsbnThrowISBNExceptionForWrongCheckDigitIsbn10() throws Exception {
    ISBN.parseIsbn("0123456780");
  }

  @Test(expected=ISBNException.class)
  public void parseIsbnThrowISBNExceptionForWrongCheckDigitIsbn13() throws Exception {
    ISBN.parseIsbn("9780123456780");
  }

  @Test
  public void equalsReturnTrueForTheSame() throws Exception {
    ISBN isbn = ISBN.parseIsbn("0123456789");

    assertThat(isbn.equals(isbn), is(true));
  }

  @Test
  public void equalsReturnFalseForNull() throws Exception {
    assertThat(ISBN.parseIsbn("0123456789").equals(null), is(false));
  }

  @Test
  public void equalsReturnFalseForOtherType() throws Exception {
    assertThat(ISBN.parseIsbn("0123456789").equals(new Object()), is(false));
  }

  @Test
  public void equalsReturnTrueForSimilar() throws Exception {
    assertThat(ISBN.parseIsbn("0123456789").equals(ISBN.parseIsbn("0123456789")), is(true));
  }

  @Test
  public void equalsReturnFalseForOther() throws Exception {
    assertThat(ISBN.parseIsbn("0470747722").equals(ISBN.parseIsbn("111800759X")), is(false));
  }

  @Test
  public void serializationTest() throws Exception {
    // construct test object
    ISBN isbn = ISBN.parseIsbn("0123456789");

    // serialize
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(out);
    oos.writeObject(isbn);
    oos.close();

    byte[] pickled = out.toByteArray();

    // deserialize
    InputStream in = new ByteArrayInputStream(pickled);
    ObjectInputStream ois = new ObjectInputStream(in);
    Object o = ois.readObject();
    ISBN copy = (ISBN) o;

    // test the result
    assertThat(isbn, is(not(sameInstance(copy))));
    assertThat(isbn.getIsbn13(), is(copy.getIsbn13()));
    assertThat(isbn.getIsbn10(), is(copy.getIsbn10()));
  }

  @Test
  public void toURITest() throws Exception {
    URI uri = ISBN.parseIsbn("0123456789").toURI();

    assertThat(uri.toString(), is("urn:isbn:9780123456786"));
  }

  @Test
  public void toStringTest() throws Exception {
    String s = ISBN.parseIsbn("0123456789").toString();

    assertThat(s, containsString(ISBN.class.getName()));
    assertThat(s, containsString("isbn13"));
    assertThat(s, containsString("isbn10"));
  }

  @Test
  public void hashCodeIsStable() throws Exception {
    int hashCode = ISBN.parseIsbn("0123456789").hashCode();

    assertThat(ISBN.parseIsbn("0123456789").hashCode(), is(hashCode));
    assertThat(ISBN.parseIsbn("0123456789").hashCode(), is(hashCode));
  }

  @Test
  public void hashCodeIsDifferent() throws Exception {
    int hashCode = ISBN.parseIsbn("0123456789").hashCode();

    assertThat(ISBN.parseIsbn("0470747722").hashCode(), is(not(hashCode)));
    assertThat(ISBN.parseIsbn("111800759X").hashCode(), is(not(hashCode)));
  }

  @Test
  public void isbn10ShouldBeAlwaysUpperCase() throws Exception {
    assertThat(ISBN.parseIsbn("111800759X").getIsbn10(), is("111800759X"));
    assertThat(ISBN.parseIsbn("111800759x").getIsbn10(), is("111800759X"));
  }

  @Test
  public void isIsbn10ReturnTrueForIsbn10() {
    assertThat(ISBN.isIsbn10("0123456789"), is(true));
  }

  @Test
  public void isIsbn10ReturnFalseForNull() {
    assertThat(ISBN.isIsbn10(null), is(false));
  }

  @Test
  public void isIsbn10ReturnFalseForIsbn13() {
    assertThat(ISBN.isIsbn10("9780123456786"), is(false));
  }

  @Test
  public void isIsbn10ReturnFalseForNonIsbnString() {
    assertThat(ISBN.isIsbn10("qwertyuiop"), is(false));
  }

  @Test
  public void isIsbn13ReturnTrueForIsbn10() {
    assertThat(ISBN.isIsbn13("9780123456786"), is(true));
  }

  @Test
  public void isIsbn13ReturnFalseForNull() {
    assertThat(ISBN.isIsbn13(null), is(false));
  }

  @Test
  public void isIsbn13ReturnFalseForIsbn13() {
    assertThat(ISBN.isIsbn13("0123456789"), is(false));
  }

  @Test
  public void isIsbn13ReturnFalseForNonIsbnString() {
    assertThat(ISBN.isIsbn13("qwertyuiop"), is(false));
  }

  @Test
  public void isValidReturnTrueForValidIsbn10() {
    assertThat(ISBN.isValid("0123456789"), is(true));
  }

  @Test
  public void isValidReturnTrueForValidIsbn13() {
    assertThat(ISBN.isValid("9780123456786"), is(true));
  }

  @Test
  public void isValidReturnFalseForNull() {
    assertThat(ISBN.isValid(null), is(false));
  }

  @Test
  public void isValidReturnFalseForWrongCheckDigitIsbn10() {
    assertThat(ISBN.isValid("0123456780"), is(false));
  }

  @Test
  public void isValidReturnFalseForWrongCheckDigitIsbn13() {
    assertThat(ISBN.isValid("9780123456780"), is(false));
  }

  @Test
  public void isValidReturnFalseForNonIsbnString() {
    assertThat(ISBN.isValid("qwertyuiop"), is(false));
  }

  @Test
  public void matcherReturnNullForNull() throws Exception {
    assertThat(ISBN.matcher(null), is(nullValue()));
  }

  @Test
  public void normalizeReturnNullForNull() {
    assertThat(ISBN.normalize(null), is(nullValue()));
  }
}
