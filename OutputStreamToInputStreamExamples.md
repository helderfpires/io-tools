# Byte Oriented I/O #

## Convert `OutputStream` to an `InputStream` ##
Here i'm assuming you know it's not an easy problem, and you already are prepared to go for the hard way because the easy solutions doesn't fit your needs for performance and memory usage. If you came here at first and you don't understand why this is so complicated you can have a look at this [preamble](ConvertOutputStreamInputStream.md).

### InputStreamFromOutputStream ###

Users must extend this abstract class and implement the abstract metod `produce(OutputStream)`.
This function is called back by the library when the `InputStreamFromOutputStream` is used. The library pass an `OutputStream` as a parameter and the user should produce its data inside the `produce()` method, and write it to the supplied `OutputStream`. The data is then available for reading through the `InputStreamFromOutputStream` class (that is an `InputSream` subclass).

This is the preferred way to convert data written from an `OutputStream` into an `InputStream`, because the behaviour of the code tends to be more clear.

Sample usage:

```
final String dataId=//id of some data.
final InputStreamFromOutputStream<String> isos = new InputStreamFromOutputStream<String>() {
   @Override
   public String produce(final OutputStream dataSink) throws Exception {
      /*
       * call your application function who produces the data here
       * WARNING: we're in another thread here, so this method shouldn't 
       * write any class field or make assumptions on the state of the class. 
       */
      return produceMydata(dataId,dataSink)
   }
 };
 try {
  //now you can read from the InputStream the data that was written to the 
  //dataSink OutputStream
  byte[] readed=IOUtils.toByteArray(isos);
  //Use data here
 } catch (final IOException e) {
  //Handle exception here
 } finally {
  isos.close();
 }
  //You can get the result of produceMyData after the stream has been closed.
  String resultOfProduction = isos.getResult();
}
```

Some programmers might be unfamiliar with the syntax of this example. It `isos` is an anonymous inner class that overrides the method `produce(OutputStream)`. You can still use standard inheritance if you prefer.

For further information read the [api javadoc](http://jsignature.sourceforge.net/io-tools/easystream/apidocs/com/gc/iotools/stream/is/InputStreamFromOutputStream.html).

### OutputStreamToInputStream ###
Another class that you can use if you need to write to an OutputStream and read the data written from an InputStream is `OutputStreamToInputStream` . It works opposite way than the previous one and allow some result to be returned after the processing of the InputStream.

Sample usage:
```
final OutputStreamToInputStream<String> oStream2IStream = new OutputStreamToInputStream<String>() {
    @Override
    protected String doRead(final InputStream istream) throws Exception {
        /*
         * read the data from the InputStream. All the operations that need to read 
         * from an InputStream should be placed in this method. The data you read here
         * is the one supplied in "oStream2IStream.write()". 
         * Any exception eventually threw here is propagated to the enclosing OutputStream
         */
        final String result = IOUtils.toString(istream);
              return result + " was processed.";
        }
    };

try {   
     /*
     * some data is written to the OutputStream, will be passed to the method
     * doRead(InputStream i) above and after close() is called the results 
     * will be available through the getResults() method.
     */
     oStream2IStream.write("test".getBytes());
} finally {
     // don't miss the close (or a thread would not terminate correctly).
     oStream2IStream.close();
}
String result = oStream2IStream.getResults();
//result now contains the string "test was processed."
```

This class propagates the `Exception` from the internal `doRead()` to the external `OutputStream`, and closing the `oStream2IStream` `OutputStream` will cause an EOF on the `InputStream` passed as `doRead()` parameter.

For further information see the [api javadoc](http://jsignature.sourceforge.net/io-tools/easystream/apidocs/com/gc/iotools/stream/os/OutputStreamToInputStream.html).

# Character Oriented I/O #

## Convert `Writer` into a `Reader` ##


### ReaderFromWriter ###

### WriterToReader ###