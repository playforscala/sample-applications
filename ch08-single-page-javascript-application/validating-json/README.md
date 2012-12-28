# 8.4 Validating JSON

Sample application from chapter 8 of [Play for Scala](http://bit.ly/playscala).

This sample is a JSON validation service. There are no views, just a single REST resource that you can use to validate a product in JSON format.

HTTP request: `POST /products`

Request body:

    {
      "name": "Blue Paper clips",
      "ean": "12345432123",
      "description": "Big box of paper clips",
      "pieces": 500,
      "manufacturer": {
        "name": "Paperclipfactory Inc.",
        "contact_details": {
          "email": "contact@paperclipfactory.example.com",
          "fax": null,
          "phone": "+12345654321"
        }
      },
      "tags": [
        "paperclip",
        "coated"
      ],
      "active": true
    }

From the command line:

    curl --include -X POST -H "Content-Type: application/json" --data-binary @@data/valid.json  "http://localhost:9000/products"
