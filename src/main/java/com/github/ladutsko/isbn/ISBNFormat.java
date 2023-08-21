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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;

import org.isbn.prefix.ranges.model.Group;
import org.isbn.prefix.ranges.model.ISBNRangeMessage;
import org.isbn.prefix.ranges.model.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ladutsko.isbn.util.RangeMessageLoader;

/**
 * ISBN format
 *
 * @author <a href="mailto:ladutsko@gmail.com">George Ladutsko</a>
 */
public class ISBNFormat {

  private static final Logger LOGGER = LoggerFactory.getLogger(ISBNFormat.class);

  public static final String HYPHEN_GROUP_SEPARATOR = "-";
  public static final String SPACE_GROUP_SEPARATOR = " ";

  private static final String RANGE_MESSAGE_RESOURCE_NAME = "RangeMessage.xml";

  private static final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
  private static Map<String, List<Range>> globalRangeMap;

  private String groupSeparator;

  public ISBNFormat() {
  }

  public ISBNFormat(final String groupSeparator) {
    setGroupSeparator(groupSeparator);
  }

  /**
   * @return groupSeparator
   */
  public String getGroupSeparator() {
    return groupSeparator;
  }

  /**
   * @param groupSeparator groupSeparator
   */
  public void setGroupSeparator(final String groupSeparator) {
    this.groupSeparator = groupSeparator;
  }

  /**
   * Format ISBN
   *
   * @param input character sequence  which contains ISBN
   * @return formatted ISBN or null if input is null
   * @throws ISBNException if something is wrong
   */
  public String format(final CharSequence input) throws ISBNException {
    return format(input, getGroupSeparator());
  }

  /**
   * Format ISBN
   *
   * @param input character sequence  which contains ISBN
   * @param groupSeparator groupSeparator
   * @return formatted ISBN or null if input is null
   * @throws ISBNException if something is wrong
   */
  public String format(final CharSequence input, final String groupSeparator) throws ISBNException {
    if (null == input)
      return null;

    Matcher m = ISBN.matcher(input);

    if (null != m.group(1))
      return format(ISBN.normalize(input), (null == groupSeparator ? HYPHEN_GROUP_SEPARATOR : groupSeparator), 3);

    if (null != m.group(2))
      return format(ISBN.normalize(input), (null == groupSeparator ? HYPHEN_GROUP_SEPARATOR : groupSeparator), 0);

    return input.toString();
  }

  protected String format(final String input, final String groupSeparator, final int beginIndex) {
    Map<String, List<Range>> rangeMap = getRangeMap();
    StringBuilder sb = new StringBuilder(17);
    if (0 < beginIndex)
      sb.append(input.substring(0, 3)).append(groupSeparator);
    int start = beginIndex + 1;
    int end = beginIndex + 7;
    for (int i = start; end >= i; ++i) {
      String prefix = (0 == beginIndex ? ISBN.DEFAULT_PREFIX + input.substring(0, i) : input.substring(0, i)).intern();
      List<Range> rangeList = rangeMap.get(prefix);
      if (LOGGER.isDebugEnabled())
        LOGGER.debug("Prefix {} contains {} range(s)", prefix, (null == rangeList ? 0 : rangeList.size()));
      if (null == rangeList) {
        continue;
      }
      sb.append(input.substring(beginIndex, i)).append(groupSeparator);
      if (rangeList.isEmpty()) {
        return sb.append(input.substring(i, beginIndex + 9)).append(groupSeparator).append(input.substring(beginIndex + 9)).toString();
      } else {
        for (Range range : rangeList) {
          String pubIdStr = input.substring(i, i + range.length);
          int pubId = Integer.parseInt(pubIdStr);
          if (range.min <= pubId && range.max >= pubId) {
            if (LOGGER.isDebugEnabled())
              LOGGER.debug("Found publisher range {}-{}. Return formatted isbn.", range.min, range.max);
            return sb.append(pubIdStr).append(groupSeparator).append(input.substring(i + range.length, beginIndex + 9)).append(groupSeparator).append(input.substring(beginIndex + 9)).toString();
          }
        }
        break;
      }
    }

    if (LOGGER.isDebugEnabled())
      LOGGER.debug("Not matched. Return simple formatted isbn.");

    sb.setLength(0);
    if (0 < beginIndex)
      sb.append(input.substring(0, 3)).append(groupSeparator);
    return sb.append(input.substring(beginIndex, beginIndex + 9)).append(groupSeparator).append(input.substring(beginIndex + 9)).toString();
  }

  protected Map<String, List<Range>> getRangeMap() {
    if (LOGGER.isTraceEnabled())
      LOGGER.trace("Start getRangeMap ...");
    rwl.readLock().lock();
    if (null == globalRangeMap) {
       // Must release read lock before acquiring write lock
       rwl.readLock().unlock();
       rwl.writeLock().lock();
       try {
         // Recheck state because another thread might have
         // acquired write lock and changed state before we did.
         if (null == globalRangeMap)
           initialize();
         // Downgrade by acquiring read lock before releasing write lock
         rwl.readLock().lock();
       } finally {
         rwl.writeLock().unlock(); // Unlock write, still hold read
       }
    }
    rwl.readLock().unlock();
    return globalRangeMap;
  }

  private void initialize() {
    if (LOGGER.isTraceEnabled())
      LOGGER.trace("Start initialize ...");
    try {
      ISBNRangeMessage isbnRangeMessage = new RangeMessageLoader(getClass().getResource(RANGE_MESSAGE_RESOURCE_NAME).toString()).load();
      Map<String, List<Range>> rangeMap = new TreeMap<String, List<Range>>();
      for (Group group : isbnRangeMessage.getRegistrationGroups().getGroup()) {
        List<Rule> ruleList = group.getRules().getRule();
        List<Range> rangeList = new ArrayList<Range>(ruleList.size());
        for (Rule rule : ruleList) {
          int length = Integer.parseInt(rule.getLength());
          if (0 == length)
            continue;

          String rangeStr = rule.getRange();
          int p = rangeStr.indexOf('-');

          Range range = new Range();
          range.length = length;
          range.min = Integer.parseInt(rangeStr.substring(0, length));
          range.max = Integer.parseInt(rangeStr.substring(p+1, p+1+length));

          rangeList.add(range);
        }

        String prefix = group.getPrefix().replace("-", "").intern();
        if (LOGGER.isDebugEnabled())
          LOGGER.debug("Put {} range(s) for prefix {}", rangeList.size(), prefix);
        rangeMap.put(prefix, rangeList);
      }
      globalRangeMap = Collections.unmodifiableMap(rangeMap);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  protected static class Range {
    public int length;
    public int min;
    public int max;
  }
}
