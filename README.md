# ISBN core [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ladutsko/isbn-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ladutsko/isbn-core)

`ISBN core` is a small library that contains a representation object of ISBN-10 and ISBN-13 and
tools to parse, validate and format one.
 
* Java 1.5+
* OSGi compatible

```xml
<dependency>
    <groupId>com.github.ladutsko</groupId>
    <artifactId>isbn-core</artifactId>
    <version>1.1.0</version>
</dependency>
```

```groovy
implementation 'com.github.ladutsko:isbn-core:1.1.0'
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

## Changelog

### 1.1.0

* ISBN ranges by Sat, 1 Oct 2022 21:33:12 BST
* Expand ISBN pattern to handle complex cases
* Fix ISBN-10 case bug

### 1.0.17
 
* ISBN ranges by Sat, 23 Jul 2022 20:53:27 BST

### 1.0.16
 
* ISBN ranges by Sun, 24 Apr 2022 13:43:39 BST

### 1.0.15
 
* ISBN ranges by Thu, 6 Jan 2022 10:15:21 GMT

### 1.0.14
 
* ISBN ranges by Wed, 5 May 2021 19:17:55 BST

### 1.0.13
 
* ISBN ranges by Fri, 18 Dec 2020 16:47:44 GMT

### 1.0.12
 
* ISBN ranges by Fri, 7 Aug 2020 13:02:30 CEST

### 1.0.11
 
* ISBN ranges by Tue, 10 Dec 2019 12:58:24 CET

### 1.0.10
 
* ISBN ranges by Wed, 17 Apr 2019 13:20:03 CEST

### 1.0.9
 
* ISBN ranges by Tue, 18 Dec 2018 12:56:02 CET

### 1.0.8
 
* ISBN ranges by Sun, 29 Jul 2018 16:00:14 CEST

### 1.0.7
 
* ISBN ranges by Tue, 12 Jun 2018 10:39:58 CEST

### 1.0.6
 
* ISBN ranges by Sun, 7 Jan 2018 11:00:13 CET

### 1.0.5
 
* ISBN ranges by Mon, 4 Sep 2017 17:16:55 CEST

### 1.0.4
 
* ISBN ranges by Mon, 13 Jul 2015 12:47:22 CEST

### 1.0.3
 
* ISBN ranges by Wed, 10 Sep 2014 17:55:37 CEST

### 1.0.2
 
* ISBN ranges by Thu, 15 May 2014 12:12:06 CEST

### 1.0.1
 
* ISBN ranges by Thu, 06 Feb 2014 13:16:35 GMT

### 1.0.0
 
* ISBN ranges by Mon, 25 Nov 2013 09:51:28 GMT
