# ISBN core ![Main CI](https://github.com/ladutsko/isbn-core/actions/workflows/main.yml/badge.svg) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ladutsko/isbn-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ladutsko/isbn-core) [![javadoc](https://javadoc.io/badge2/com.github.ladutsko/isbn-core/javadoc.svg)](https://javadoc.io/doc/com.github.ladutsko/isbn-core)

`ISBN core` is a small library that contains a representation object of ISBN-10 and ISBN-13 and
tools to parse, validate and format one.
 
* Java 1.5+
* OSGi compatible
* GraalVM compatible

```xml
<dependency>
    <groupId>com.github.ladutsko</groupId>
    <artifactId>isbn-core</artifactId>
    <version>1.5.3</version>
</dependency>
```

```groovy
implementation 'com.github.ladutsko:isbn-core:1.5.3'
```

## Quick start

```java
import com.github.ladutsko.isbn.ISBN;
import com.github.ladutsko.isbn.ISBNException;
import com.github.ladutsko.isbn.ISBNFormat;

class HelloWorld {
    public static void main(String[] args) {
        try {
            ISBN isbn = ISBN.parseIsbn("0131872486"); // or 978-0131872486
            // Valid isbn string
            ISBNFormat format = new ISBNFormat();
            System.out.println(format.format(isbn.getIsbn10())); // output: 0-13-187248-6
            System.out.println(format.format(isbn.getIsbn13())); // output: 978-0-13-187248-6
        } catch (ISBNException e) {
            // Invalid isbn string
            e.printStackTrace(); // Reason
        }
    }
}
```
