# Demonstrations

## Chapter 3 - overview

* 3.2 Configuration: `conf/application.conf`
  * 3.2.4 Configuration API—programmatic access: `app/views/debug.scala.html`

## Chapter 4 - HTTP

* 4.2 Controllers
   * 4.2.1 Controller classes and action methods: `app/controllers/Products.scala`
* 4.3 Routing: `conf/routes`
* 4.4 Binding: `controllers.Products.show`
* 4.5 Reverse routing: `controllers.Products.save`
* 4.6 Response
  * 4.6.2 Response body: HTML: `app/controllers/Products.scala`
  * 4.6.2 Response body - binary: `app/controllers/Barcodes.scala`

## Chapter 5 - persistence

* 5.2 Anorm
  * 5.2.1 Defining your model: `app/models/Product.scala`

## Chapter 6 - templates

* 6.3 Template basics
  * 6.3.2 Expressions
  * 6.3.3 Displaying collections
* 6.4 Template composition
  * 6.4.1 Includes: `app/views/main.scala.html <footer>`
  * 6.4.2 Layouts: `app/views/main.scala.html`
  * 6.4.3 Tags: `app/tags/`
* 6.7 Internationalization: configuration, messages, controllers, templates

## Chapter 7 - validation and forms

* 7.2 Form basics
  * 7.2.5 Object mappings: `controllers.Products.productForm`


# To do

## Not yet in the book

* Chapter 3: Test data - YAML fixture

## Chapter 3 - overview

* 3.2 Configuration
  * 3.2.2 Configuration file format: includes, JSON format
  * 3.2.5 Custom application configuration
* 3.7 Jobs _(calculate a product ranking or regenerate barcodes?)_
  * 3.7.1 Asynchronous jobs
  * 3.7.2 Scheduled jobs
* 3.8 Modules
  * 3.8.1 Third-party modules _(PDF module output)_
  * 3.8.2 Extracting custom modules _(barcode module)_

## Chapter 4 - HTTP

* 4.2 Controllers
  * 4.2.3 Action composition _(logging/timing?)_
* 4.3 Routing
  * 4.3.3 Constraining URL path parameters with regular expressions _(/product/$ean<\d{13}> vs /product/:alias)_
* 4.4 Binding
* 4.5 Reverse routing
* 4.6 Response
  * 4.6.2 Response body - JSON
  * 4.6.3 HTTP status codes
  * 4.6.4 Response headers: content type, session, flash, cookies
  * 4.6.5 Static content: assets, caching, gzip compression

## Chapter 5 - persistence

* 5.1 Database
  * 5.1.3 Configuring your database
* 5.2 Anorm
  * 5.2.1 Defining your model
  * 5.2.2 Creating the schema
  * 5.2.6 Inserting, updating and deleting data
* 5.4 Caching data _(cache barcode image)_

## Chapter 6 - templates

* 6.3 Template basics
  * 6.3.4 Security and escaping: escaping, raw HTML
* 6.5 Implicit parameters
* 6.6 Asset pipeline
  * 6.6.1 LESS
  * 6.6.2 CoffeeScript

## Chapter 7 - validation and forms

* 7.2 Form basics
  * 7.2.1 Mappings
  * 7.2.2 Data
  * 7.2.3 Tuple mappings
  * 7.2.4 Validating data
  * 7.2.5 Object mappings
* 7.3 HTML forms
* 7.4 Validation _(validate EAN code format and checksum)_
* 7.5 JSON
