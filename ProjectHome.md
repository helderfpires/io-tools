## **N.B.** Due to the announced google code shutdown, due in August 2015, this project it is being migrated to: http://io-tools.sourceforge.net . ##


## [Stream Utilities (EasyStream)](Tutorial_EasyStream.md)  ##
<font size='x-small'> Latest release 1.2.14 </font> 

Small set of utilities for dealing with streams. You can use this library for :

  * 'Convert' an `OutputStream` into an `InputStream` and `Writer` into a `Reader`.
  * Gather some statistics on your streams: bit rate, total number of bytes read/written.
  * Stream buffering: `InputStream` are _read once_ classes. [Here](https://code.google.com/p/io-tools/wiki/Tutorial_EasyStream#Other_Stream_utilities) is a stream wrapper that allows you to _rewind_ the `Inputstream`.
  * Stream wiring: While reading the data from an `InputStream` "tee" it to supplied `OutputStream(s)` or write data to multiple `OutputStream(s)` at the same time.

EasyStream is a natural extension of Apache [commons-io](http://commons.apache.org/io/), providing advanced solutions to some common but not trivial problem. Focus is on performance and memory usage.

## [Format detection (WazFormat)](Tutorial_WazFormat.md) ##
<font size='x-small'> Latest release 1.2.14 </font> 

This library is a format identification framework that integrates a native format identification engine with [droid](http://droid.sourceforge.net/wiki/index.php/Introduction) and in future will integrate mime-utils and apache tika.

The main goal of this library is to perform a format identification on an `InputStream`, in a way that is possible to know its content type and "preserve" the data in it for further processing (the data is eventually buffered to disk or to memory in a transparent way).

  * Supports more than 60 [file formats](Formats.md).
  * Nested detection: it can detect what is inside a bzip2 stream or a PKCS#7 document.
  * Result of identification is an `Enum`. Most of identification libraries return a string that must be further parsed by the calling software.

_Due to a wrong choice of the inner detection library Wazformat has some serious  [performance issue](https://code.google.com/p/io-tools/issues/detail?id=28). Though it is currently used in production in many projects, users should try to limit the number of formats detected._


### What is next ? ###

  * Take the 5 minutes tutorials:
    * 'Convert' an `OutputStream` into an `InputStream` [introduction](OutputStream_to_InputStream.md).
    * [stream utilities](Tutorial_EasyStream.md) explained usage.
    * [format identification](Tutorial_WazFormat.md).
  * Subscribe to the [users forum](http://groups.google.it/group/io-tools). We will be glad to help you, and to answer your questions! The forum is moderated, and it has very few messages per year. (Please **don't contact project administrators** on private email, your doubts might be useful to other users.)
  * Check out the full API (Javadoc) at [easystream](http://io-tools.googlecode.com/svn/www/easystream/apidocs/index.html) and [wazformat](http://io-tools.googlecode.com/svn/www/wazformat/apidocs/index.html) .

### Installation ###
Download the zipped version (see the link on the left) or if you're a Maven user [add it](Installation.md) to your pom.xml (if you use Maven you'll always get the latest stable version).

Once again, please don't contact us on private email. There is a free for everybody, public place to get help. Requests on private emails will be redirected to the user forum. Thank you for understanding.


---


Any help is appreciated (comment, suggestion, new ideas and most of all bug reports). If you want to contribute please request for a membership.

