Body parser sample application
==============================

This is a sample application from Chapter 10 of [Play for Scala](http://bit.ly/playscala), demonstrating body parsers.

*Note: It is currently broken. The code that worked for Play 2.0.3 fails in 2.0.4 and 2.1 and up. It looks like the HTTP client started adding a Transfer-Encoding: chunked header that S3 doesn't like.*
