# 8.1 - 8.3 Building a single-page JavaScript application with JSON

Sample application from chapter 8 of [Play for Scala](http://bit.ly/playscala).

This sample is a single-page JavaScript application that uses a client written in CoffeeScript to interact with a Play application that provides a RESTful web service.

test link:
http://localhost:9000/
http://localhost:9000/products
-> [5010255079763,5018206244611,5018206244666,5018306312913,5018306332812]
http://localhost:9000/products/5010255079763
-> {"ean":5010255079763,"name":"Paperclips Large","description":"Large Plain Pack of 1000"}
http://localhost:9000/products/5010255079763/combinators
-> same as above
