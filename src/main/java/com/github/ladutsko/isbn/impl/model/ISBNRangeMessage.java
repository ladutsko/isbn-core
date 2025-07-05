package com.github.ladutsko.isbn.impl.model;

import java.util.List;

/**
 * @author <a href="mailto:ladutsko@gmail.com">George Ladutsko</a>
 */
public class ISBNRangeMessage {

  public String messageSource;
  public String messageSerialNumber;
  public String messageDate;
  public List<EANUCC> eanuccPrefixes;
  public List<Group> registrationGroups;

}
