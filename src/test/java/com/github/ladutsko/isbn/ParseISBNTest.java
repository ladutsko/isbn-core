/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 George Ladutsko
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

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

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
public class ParseISBNTest {
  
  private final String isbnStr;
  private final String resultIsbn13;
  private final String resultIsbn10;
  
  private ISBN isbn;
  
  public ParseISBNTest(final String isbnStr, final String resultIsbn13, final String resultIsbn10) {
    this.isbnStr = isbnStr;
    this.resultIsbn13 = resultIsbn13;
    this.resultIsbn10 = resultIsbn10;
  }
  
  @Before
  public void setUp() throws ISBNException {
    isbn = ISBN.parseIsbn(isbnStr);
  }
  
  @Test
  public void test() {
    assertThat(isbn.getIsbn13(), is(resultIsbn13));
    assertThat(isbn.getIsbn10(), is(resultIsbn10));
  }

  @Parameters
  public static Collection<Object[]> getParameters() {
    return Arrays.asList(new Object[][] {
      { "1849693080", "9781849693080", "1849693080" },
      { "1849682461", "9781849682466", "1849682461" },
      { "0470747722", "9780470747728", "0470747722" },
      { "0133066843", "9780133066845", "0133066843" },
      { "1111529124", "9781111529123", "1111529124" },
      { "1430219955", "9781430219958", "1430219955" },
      { "0470977426", "9780470977422", "0470977426" },
      { "0470886587", "9780470886588", "0470886587" },
      { "1568814348", "9781568814346", "1568814348" },
      { "1849691169", "9781849691161", "1849691169" },
      { "111800759X", "9781118007594", "111800759X" },
      
      { "1-84969-308-0", "9781849693080", "1849693080" },
      { "1-84968-246-1", "9781849682466", "1849682461" },
      { "0-470-74772-2", "9780470747728", "0470747722" },
      { "0-13-306684-3", "9780133066845", "0133066843" },
      { "1-111-52912-4", "9781111529123", "1111529124" },
      { "1-4302-1995-5", "9781430219958", "1430219955" },
      { "0-470-97742-6", "9780470977422", "0470977426" },
      { "0-470-88658-7", "9780470886588", "0470886587" },
      { "1-56881-434-8", "9781568814346", "1568814348" },
      { "1-84969-116-9", "9781849691161", "1849691169" },
      { "1-118-00759-X", "9781118007594", "111800759X" },
      
      { "1 84969 308 0", "9781849693080", "1849693080" },
      { "1 84968 246 1", "9781849682466", "1849682461" },
      { "0 470 74772 2", "9780470747728", "0470747722" },
      { "0 13 306684 3", "9780133066845", "0133066843" },
      { "1 111 52912 4", "9781111529123", "1111529124" },
      { "1 4302 1995 5", "9781430219958", "1430219955" },
      { "0 470 97742 6", "9780470977422", "0470977426" },
      { "0 470 88658 7", "9780470886588", "0470886587" },
      { "1 56881 434 8", "9781568814346", "1568814348" },
      { "1 84969 116 9", "9781849691161", "1849691169" },
      { "1 118 00759 X", "9781118007594", "111800759X" },
      
      { "9780321130020", "9780321130020", "0321130022" },
      { "9780321159601", "9780321159601", "0321159608" },
      { "9780131425422", "9780131425422", "0131425420" },
      { "9780321228963", "9780321228963", "0321228960" },
      { "9780672324024", "9780672324024", "0672324024" },
      { "9780201914665", "9780201914665", "0201914662" },
      { "9780201361216", "9780201361216", "0201361213" },
      { "9780321113597", "9780321113597", "0321113594" },
      { "9780201760408", "9780201760408", "0201760401" },
      { "9780201734959", "9780201734959", "0201734958" },
      
      { "9791090636071", "9791090636071", null },
      
      { "978-0-321-13002-0", "9780321130020", "0321130022" },
      { "978-0-321-15960-1", "9780321159601", "0321159608" },
      { "978-0-13-142542-2", "9780131425422", "0131425420" },
      { "978-0-321-22896-3", "9780321228963", "0321228960" },
      { "978-0-672-32402-4", "9780672324024", "0672324024" },
      { "978-0-201-91466-5", "9780201914665", "0201914662" },
      { "978-0-201-36121-6", "9780201361216", "0201361213" },
      { "978-0-321-11359-7", "9780321113597", "0321113594" },
      { "978-0-201-76040-8", "9780201760408", "0201760401" },
      { "978-0-201-73495-9", "9780201734959", "0201734958" },
      
      { "979-10-90636-07-1", "9791090636071", null },
      
      { "978 0 321 13002 0", "9780321130020", "0321130022" },
      { "978 0 321 15960 1", "9780321159601", "0321159608" },
      { "978 0 13 142542 2", "9780131425422", "0131425420" },
      { "978 0 321 22896 3", "9780321228963", "0321228960" },
      { "978 0 672 32402 4", "9780672324024", "0672324024" },
      { "978 0 201 91466 5", "9780201914665", "0201914662" },
      { "978 0 201 36121 6", "9780201361216", "0201361213" },
      { "978 0 321 11359 7", "9780321113597", "0321113594" },
      { "978 0 201 76040 8", "9780201760408", "0201760401" },
      { "978 0 201 73495 9", "9780201734959", "0201734958" },
      
      { "979 10 90636 07 1", "9791090636071", null }
    });
  }
}
