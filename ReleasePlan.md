# IO-Tools release plan #

Release schedule for IO-Tools.
## Version 1.2.14 ##

  * Added the following readers: CloseOnceReader, DiagnosticReader, StatsReader
  * Fix [issue #36](https://code.google.com/p/io-tools/issues/detail?id=36)  that caused a thread lock under some circustances.
  * Fix [issue #35](https://code.google.com/p/io-tools/issues/detail?id=35)  in wazformat that can cause a wrong xml detection under java 7.

## Version 1.2 ##
  * Format identification using droid (70 formats identified).
  * ~~`RandomAccessInputStream` to read content of an `InputStream` multiple times.~~
  * ~~`InputStreamFromOutputStream` can now return a result other than the written stream.~~
  * Improved javadocs.

## Version 1.1 ##
_(Released on Dec 28, 2008)_

  * Publish release on repo1. [Issue#6](http://code.google.com/p/io-tools/issues/detail?id=6)

## Version 1.0.7 ##
_(Released on Nov 24, 2008)_
  * Package rename to shorten package names and rearrange classes (sorry for that)
  * Added `SizeReaderInputStream` and `SizeRecorderOutputStream`

## Version 1.0.6 ##
_(Releasd on Nov 18, 2008)_
  * [Issue#2](http://code.google.com/p/io-tools/issues/detail?id=2) Java 1.4 compatibility.
  * [Issue#5](http://code.google.com/p/io-tools/issues/detail?id=5) Formats part ( _wazstream_ ) separated from `InputStream` and `OutputStream` ( _easystream_ )

## Version 1.0 ##
_(released on nov 10, 2008)_

  * Tools for file format detect: Base64, XML, PKCS#7, ZIP, PDF
  * Recursive format detection
  * Junit and documentaton.

## Version 0.1 ##
_(released on oct 22, 2008)_
  * Initial project build with Eclipse and Maven
  * Tools for reading from an `InputStream` the data written to an `OutputStream`
  * Junit and documentation on wiki.

&lt;wiki:gadget url="http://www.ohloh.net/p/20446/widgets/project\_factoids\_stats.xml" height="270" border="1"/&gt;