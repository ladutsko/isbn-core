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

package com.github.ladutsko.isbn.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.isbn.prefix.ranges.model.ISBNRangeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RangeMessage loader
 *
 * @author <a href="mailto:ladutsko@gmail.com">George Ladutsko</a>
 */
public class RangeMessageLoader {

  private static final String RANGE_MESSAGE_PACKAGE = "org.isbn.prefix.ranges.model";

  private static final Logger LOGGER = LoggerFactory.getLogger(RangeMessageLoader.class);

  private String rangeMessageUrl;

  public RangeMessageLoader() {
  }

  public RangeMessageLoader(final String rangeMessageUrl) {
    setRangeMessageUrl(rangeMessageUrl);
  }

  /**
   * @return rangeMessageUrl
   */
  public String getRangeMessageUrl() {
    return rangeMessageUrl;
  }

  /**
   * @param rangeMessageUrl rangeMessageUrl
   */
  public void setRangeMessageUrl(final String rangeMessageUrl) {
    this.rangeMessageUrl = rangeMessageUrl;
  }

  /**
   * Load RangeMessage.xml and return model
   *
   * @return model
   * @throws RangeMessageException if something is wrong
   */
  public ISBNRangeMessage load() throws RangeMessageException {
    if (LOGGER.isTraceEnabled())
      LOGGER.trace("Start loadRangeMessage ...");
    try {
      JAXBContext context = JAXBContext.newInstance(RANGE_MESSAGE_PACKAGE);
      Unmarshaller unmarshaller = context.createUnmarshaller();

      String rangeMessageUrl = getRangeMessageUrl();
      if (LOGGER.isDebugEnabled())
        LOGGER.debug("Open resource as stream: {}", rangeMessageUrl);
      InputStream in = new URL(rangeMessageUrl).openStream();

      ISBNRangeMessage isbnRangeMessage;
      try {
        isbnRangeMessage = (ISBNRangeMessage) unmarshaller.unmarshal(in);
      } finally {
        try {
          in.close();
        } catch (IOException e) {
          if (LOGGER.isErrorEnabled())
            LOGGER.error(e.getMessage(), e);
        }
      }

      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("MessageSource: {}", isbnRangeMessage.getMessageSource());
        LOGGER.debug("MessageSerialNumber: {}", isbnRangeMessage.getMessageSerialNumber());
        LOGGER.debug("MessageDate: {}", isbnRangeMessage.getMessageDate());
        LOGGER.debug("RegistrationGroups size: {}", isbnRangeMessage.getRegistrationGroups().getGroup().size());
      }

      return isbnRangeMessage;
    } catch (Exception e) {
      throw new RangeMessageException(e.getMessage(), e);
    }
  }
}
