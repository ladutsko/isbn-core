package com.github.ladutsko.isbn.impl;

import com.github.ladutsko.isbn.impl.model.EANUCC;
import com.github.ladutsko.isbn.impl.model.Group;
import com.github.ladutsko.isbn.impl.model.ISBNRangeMessage;
import com.github.ladutsko.isbn.impl.model.Rule;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @author <a href="mailto:ladutsko@gmail.com">George Ladutsko</a>
 */
public class RangeMessageParser {

  private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newInstance();

  private Deque<Object> stack;
  private StringBuilder sb;
  private Object currentElement;

  public ISBNRangeMessage parse(InputStream is) throws XMLStreamException {
    stack = new LinkedList<>();
    sb = new StringBuilder();
    currentElement = null;

    XMLStreamReader reader = XML_INPUT_FACTORY.createXMLStreamReader(is);
    while (reader.hasNext()) {
      int event = reader.next();
      switch (event) {
        case XMLStreamConstants.START_ELEMENT:
          startElement(reader);
          break;

        case XMLStreamConstants.CHARACTERS:
          sb.append(reader.getText());
          break;

        case XMLStreamConstants.END_ELEMENT:
          endElement(reader);
          break;
      }
    }
    return (ISBNRangeMessage) currentElement;
  }

  private void startElement(XMLStreamReader reader) {
    sb.setLength(0);

    String name = reader.getLocalName();
    switch (name) {
      case "ISBNRangeMessage":
        currentElement = new ISBNRangeMessage();
        break;

      case "EAN.UCCPrefixes":
        ((ISBNRangeMessage) currentElement).eanuccPrefixes = new LinkedList<>();
        break;

      case "RegistrationGroups":
        ((ISBNRangeMessage) currentElement).registrationGroups = new LinkedList<>();
        break;

      case "Rules":
        if (currentElement instanceof EANUCC)
          ((EANUCC) currentElement).rules = new LinkedList<>();
        else if (currentElement instanceof Group)
          ((Group) currentElement).rules = new LinkedList<>();
        break;

      case "EAN.UCC":
        stack.push(currentElement);
        currentElement = new EANUCC();
        break;

      case "Group":
        stack.push(currentElement);
        currentElement = new Group();
        break;

      case "Rule":
        stack.push(currentElement);
        currentElement = new Rule();
        break;
    }
  }

  private void endElement(XMLStreamReader reader) {
    String value = sb.toString();
    Object element = currentElement;

    String name = reader.getLocalName();
    switch (name) {
      case "MessageSource":
        ((ISBNRangeMessage) currentElement).messageSource = value;
        break;

      case "MessageSerialNumber":
        ((ISBNRangeMessage) currentElement).messageSerialNumber = value;
        break;

      case "MessageDate":
        ((ISBNRangeMessage) currentElement).messageDate = value;
        break;

      case "EAN.UCC":
        currentElement = stack.pop();
        ((ISBNRangeMessage) currentElement).eanuccPrefixes.add((EANUCC) element);
        break;

      case "Group":
        currentElement = stack.pop();
        ((ISBNRangeMessage) currentElement).registrationGroups.add((Group) element);
        break;

      case "Range":
        ((Rule) currentElement).range = value;
        break;

      case "Length":
        ((Rule) currentElement).length = value;
        break;

      case "Prefix":
        if (currentElement instanceof EANUCC)
          ((EANUCC) currentElement).prefix = value;
        else if (currentElement instanceof Group)
          ((Group) currentElement).prefix = value;
        break;

      case "Agency":
        if (currentElement instanceof EANUCC)
          ((EANUCC) currentElement).agency = value;
        else if (currentElement instanceof Group)
          ((Group) currentElement).agency = value;
        break;

      case "Rule":
        currentElement = stack.pop();
        if (currentElement instanceof EANUCC)
          ((EANUCC) currentElement).rules.add((Rule) element);
        else if (currentElement instanceof Group)
          ((Group) currentElement).rules.add((Rule) element);
        break;
    }
  }
}
