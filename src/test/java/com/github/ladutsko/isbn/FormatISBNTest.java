/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 George Ladutsko
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

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author <a href="mailto:ladutsko@gmail.com">George Ladutsko</a>
 */
@RunWith(Parameterized.class)
public class FormatISBNTest {

  private final String input;
  private final String groupSeparator;
  private final String result;

  private ISBNFormat formatter;

  public FormatISBNTest(final String input, final String groupSeparator, final String result) {
    this.input = input;
    this.groupSeparator = groupSeparator;
    this.result = result;
  }

  @Before
  public void setUp() {
    formatter = new ISBNFormat(groupSeparator);
  }

  @Test
  public void forma() throws ISBNException {
    assertThat(formatter.format(input), is(result));
  }

  @Parameters
  public static Collection<Object[]> getParameters() {
    return Arrays.asList(new Object[][] {
      { "9780321130020", null, "978-0-321-13002-0" },
      {    "0321130022", null,     "0-321-13002-2" },
      { "9791090636071", null, "979-10-90636-07-1" },
      { "9789999999999", null,   "978-999999999-9" },
      {    "9999999999", null,       "999999999-9" },

      { "9780321130020", ISBNFormat.HYPHEN_GROUP_SEPARATOR, "978-0-321-13002-0" },
      {    "0321130022", ISBNFormat.HYPHEN_GROUP_SEPARATOR,     "0-321-13002-2" },
      { "9791090636071", ISBNFormat.HYPHEN_GROUP_SEPARATOR, "979-10-90636-07-1" },
      { "9789999999999", ISBNFormat.HYPHEN_GROUP_SEPARATOR,   "978-999999999-9" },
      {    "9999999999", ISBNFormat.HYPHEN_GROUP_SEPARATOR,       "999999999-9" },

      { "9780321130020", ISBNFormat.SPACE_GROUP_SEPARATOR, "978 0 321 13002 0" },
      {    "0321130022", ISBNFormat.SPACE_GROUP_SEPARATOR,     "0 321 13002 2" },
      { "9791090636071", ISBNFormat.SPACE_GROUP_SEPARATOR, "979 10 90636 07 1" },
      { "9789999999999", ISBNFormat.SPACE_GROUP_SEPARATOR,   "978 999999999 9" },
      {    "9999999999", ISBNFormat.SPACE_GROUP_SEPARATOR,       "999999999 9" }
    });
  }
}
