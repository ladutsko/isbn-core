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

  private final ISBNFormat formatter = new ISBNFormat();

  private final String input;
  private final String groupSeparator;
  private final String result;

  public FormatISBNTest(final String input, final String groupSeparator, final String result) {
    this.input = input;
    this.groupSeparator = groupSeparator;
    this.result = result;
  }

  @Before
  public void setUp() {
    formatter.setGroupSeparator(groupSeparator);
  }

  @Test
  public void forma() throws ISBNException {
    assertThat(formatter.format(input), is(result));
  }

  @Parameters
  public static Collection<Object[]> getParameters() {
    return Arrays.asList(new Object[][] {
      // Prefixes of length 1
      {    "0330284983", null,     "0-330-28498-3" }, // English
      {    "1581820089", null,     "1-58182-008-9" }, // English
      {    "2226052577", null,     "2-226-05257-7" }, // French
      {    "3796519008", null,     "3-7965-1900-8" }, // German
      {    "4198301271", null,     "4-19-830127-1" }, // Japan
      {    "5852700010", null,     "5-85270-001-0" }, // former USSR
      {    "7301102992", null,     "7-301-10299-2" }, // China
      // Prefixes of length 2
      { "9786555250053", null, "978-65-5525-005-3" }, // Brazil
      {    "8085983443", null,     "80-85983-44-3" }, // former Czechoslovakia
      {    "8172153996", null,     "81-7215-399-6" }, // India
      {    "8253009836", null,     "82-530-0983-6" }, // Norway
      {    "8308015875", null,     "83-08-01587-5" }, // Poland
      {    "8486546087", null,     "84-86546-08-7" }, // Spain
      {    "8575310151", null,     "85-7531-015-1" }, // Brazil
      {    "8634108465", null,     "86-341-0846-5" }, // former Yugoslavia
      {    "8759522771", null,     "87-595-2277-1" }, // Denmark
      {    "8804473282", null,     "88-04-47328-2" }, // Italy
      {    "8904020034", null,     "89-04-02003-4" }, // South Korea
      {    "9056911872", null,     "90-5691-187-2" }, // Netherlands
      {    "9118116922", null,     "91-1-811692-2" }, // Sweden
      {    "9267103709", null,     "92-67-10370-9" }, // International NGO Publishers and EC Organizations
      {    "9350252147", null,     "93-5025-214-7" }, // India
      { "9789462650114", null, "978-94-6265-011-4" }, // Netherlands
      // Prefixes of length 3
      { "9786001191251", null, "978-600-119-125-1"}, // Iran
      { "9786017151133", null, "978-601-7151-13-3"}, // Kazakhstan
      { "9786028328227", null, "978-602-8328-22-7"}, // Indonesia
      { "9786035000451", null, "978-603-500-045-1"}, // Saudi Arabia
      {    "6046945100", null,     "604-69-4510-0"}, // Vietnam
      {    "6053840572", null,     "605-384-057-2"}, // Turkey
      { "9786068126357", null, "978-606-8126-35-7"}, // Romania
      { "9786074550351", null, "978-607-455-035-1"}, // Mexico
      { "9786082030234", null, "978-608-203-023-4"}, // North Macedonia
      {    "6090112488", null,     "609-01-1248-8"}, // Lithuania
      {    "6115430097", null,      "611-543009-7"}, // Thailand
      { "9786124516597", null, "978-612-45165-9-7"}, // Peru
      { "9786131574375", null, "978-613-1-57437-5"}, // Mauritius
      { "9786144040188", null, "978-614-404-018-8"}, // Lebanon
      { "9786155014994", null, "978-615-5014-99-4"}, // Hungary
      { "9786169039334", null, "978-616-90393-3-4"}, // Thailand
      { "9786175811160", null, "978-617-581-116-0"}, // Ukraine
      { "9786180207897", null, "978-618-02-0789-7"}, // Greece
      { "9786199056844", null, "978-619-90568-4-4"}, // Bulgaria
      { "9786200004574", null, "978-620-0-00457-4"}, // Mauritius
      { "9786219619028", null, "978-621-96190-2-8"}, // Philippines
      { "9786226011013", null, "978-622-6011-01-3"}, // Iran
      { "9786239163105", null, "978-623-91631-0-5"}, // Indonesia
      { "9786245375004", null, "978-624-5375-00-4"}, // Sri Lanka
      { "9786257677790", null, "978-625-7677-79-0"}, // Turkey
      { "9786267002469", null, "978-626-7002-46-9"}, // Taiwan
      { "9786275110293", null, "978-627-511-029-3"}, // Pakistan
      { "9786287510067", null, "978-628-7510-06-7"}, // Colombia
      {    "9500404427", null,     "950-04-0442-7"}, // Argentina
      {    "9510113697", null,     "951-0-11369-7"}, // Finland
      {    "9524712946", null,     "952-471-294-6"}, // Finland
      {    "9531571058", null,     "953-157-105-8"}, // Croatia
      {    "954430603X", null,     "954-430-603-X"}, // Bulgaria
      {    "955203051X", null,     "955-20-3051-X"}, // Sri Lanka
      {    "9567291489", null,     "956-7291-48-9"}, // Chile
      {    "9570174293", null,     "957-01-7429-3"}, // Taiwan
      {    "958046278X", null,     "958-04-6278-X"}, // Colombia
      {    "9591003633", null,     "959-10-0363-3"}, // Cuba
      { "9789609962674", null, "978-960-99626-7-4"}, // Greece
      {    "9616403230", null,     "961-6403-23-0"}, // Slovenia
      {    "9620401956", null,     "962-04-0195-6"}, // Hong Kong
      {    "9637971513", null,     "963-7971-51-3"}, // Hungary
      {    "9646194702", null,     "964-6194-70-2"}, // Iran
      {    "9653590022", null,     "965-359-002-2"}, // Israel
      {    "966954405X", null,     "966-95440-5-X"}, // Ukraine
      {    "9679787532", null,     "967-978-753-2"}, // Malaysia
      {    "9686031022", null,     "968-6031-02-2"}, // Mexico
      { "9789693520200", null, "978-969-35-2020-0"}, // Pakistan
      {    "9702002427", null,     "970-20-0242-7"}, // Mexico
      {    "9718845100", null,     "971-8845-10-0"}, // Philippines
      {    "9723702746", null,     "972-37-0274-6"}, // Portugal
      {    "9734301799", null,     "973-43-0179-9"}, // Romania
      {    "9748585476", null,     "974-85854-7-6"}, // Thailand
      {    "9752933815", null,     "975-293-381-5"}, // Turkey
      {    "9766401403", null,     "976-640-140-3"}, // Caribbean Community
      {    "9777345208", null,     "977-734-520-8"}, // Egypt
      {    "9783718622", null,     "978-37186-2-2"}, // Nigeria
      {    "9795534831", null,     "979-553-483-1"}, // Indonesia
      {    "9800101942", null,     "980-01-0194-2"}, // Venezuela
      {    "9813018399", null,     "981-3018-39-9"}, // Singapore
      {    "9823010013", null,     "982-301-001-3"}, // South Pacific
      {    "9835201579", null,     "983-52-0157-9"}, // Malaysia
      {    "9844580897", null,     "984-458-089-7"}, // Bangladesh
      { "9789856740483", null, "978-985-6740-48-3"}, // Belarus
      {    "9864171917", null,     "986-417-191-7"}, // Taiwan
      {    "9879818423", null,     "987-98184-2-3"}, // Argentina
      { "9789880038273", null, "978-988-00-3827-3"}, // Hong Kong
      { "9789897582462", null, "978-989-758-246-2"}, // Portugal
      // Prefixes of length 4
      { "9789913600002", null, "978-9913-600-00-2" }, // Uganda
      { "9789914700824", null, "978-9914-700-82-4" }, // Kenya
      { "9789915936079", null, "978-9915-9360-7-9" }, // Uruguay
      { "9789916956502", null, "978-9916-9565-0-2" }, // Estonia
      { "9789917605003", null, "978-9917-605-00-3" }, // Bolivia
      { "9789918210640", null, "978-9918-21-064-0" }, // Malta
      { "9789928400529", null, "978-9928-4005-2-9" }, // Albania
      { "9789929801646", null, "978-9929-8016-4-6" }, // Guatemala
      { "9789930943106", null, "978-9930-9431-0-6" }, // Costa Rica
      { "9789933101473", null, "978-9933-10-147-3" }, // Syria
      { "9789934015960", null, "978-9934-0-1596-0" }, // Latvia
      { "9789936804456", null, "978-9936-8044-5-6" }, // Afghanistan
      { "9789938011227", null, "978-9938-01-122-7" }, // Tunisia
      { "9789946010601", null, "978-9946-0-1060-1" }, // North Korea
      { "9789950974401", null, "978-9950-9744-0-1" }, // Palestine
      {    "9963645062", null,     "9963-645-06-2" }, // Cyprus
      { "9789964701536", null, "978-9964-70-153-6" }, // Ghana
      { "9789965854507", null, "978-9965-854-50-7" }, // Kazakhstan
      { "9789966003157", null, "978-9966-003-15-7" }, // Kenya
      { "9789980911896", null, "978-9980-911-89-6" }, // Papua New Guinea
      {    "9986163293", null,     "9986-16-329-3" }, // Lithuania
      { "9789989218231", null, "978-9989-2182-3-1" }, // North Macedonia
      // Prefixes of length 5
      { "9789990330007", null, "978-99903-30-00-7" }, // Mauritius
      { "9789994576517", null, "978-99945-76-51-7" }, // Namibia
      { "9789991865515", null, "978-99918-65-51-5" }, // Faroe Islands
      { "9789992020005", null, "978-99920-2-000-5" }, // Andorra
      { "9789993710561", null, "978-99937-1-056-1" }, // Macau
      { "9789996520471", null, "978-99965-2-047-1" }, // Macau
      // With 979- prefix
      { "9791090636071", null, "979-10-90636-07-1" }, // France
      { "9791186178140", null, "979-11-86178-14-0" }, // South Korea
      { "9791220008525", null, "979-12-200-0852-5" }, // Italy
      { "9798602405453", null, "979-8-6024-0545-3" }, // USA
      // Unallocated
      {    "6100000008", null,       "610000000-8" },
      {    "9900000005", null,       "990000000-5" },
      // With hyphen group separator
      { "9780321130020", ISBNFormat.HYPHEN_GROUP_SEPARATOR, "978-0-321-13002-0" },
      {    "0321130022", ISBNFormat.HYPHEN_GROUP_SEPARATOR,     "0-321-13002-2" },
      { "9791090636071", ISBNFormat.HYPHEN_GROUP_SEPARATOR, "979-10-90636-07-1" },
      { "9789999999999", ISBNFormat.HYPHEN_GROUP_SEPARATOR,   "978-999999999-9" },
      {    "9999999999", ISBNFormat.HYPHEN_GROUP_SEPARATOR,       "999999999-9" },
      // With space group separator
      { "9780321130020", ISBNFormat.SPACE_GROUP_SEPARATOR, "978 0 321 13002 0" },
      {    "0321130022", ISBNFormat.SPACE_GROUP_SEPARATOR,     "0 321 13002 2" },
      { "9791090636071", ISBNFormat.SPACE_GROUP_SEPARATOR, "979 10 90636 07 1" },
      { "9789999999999", ISBNFormat.SPACE_GROUP_SEPARATOR,   "978 999999999 9" },
      {    "9999999999", ISBNFormat.SPACE_GROUP_SEPARATOR,       "999999999 9" }
    });
  }
}
