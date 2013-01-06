Body parser sample application
==============================

This is a sample application from Chapter 10 of [Play for Scala](http://bit.ly/playscala), demonstrating body parsers.

*Note: It is currently broken. The code that worked for Play 2.0.3 fails in 2.0.4 and 2.1-RC1. The request to Amazon S3 never finishes. Probably a problem with the way we use AHC's FeedableBodyGenerator, or a bug in it.*
