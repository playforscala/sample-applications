import models.Product
import play.api.libs.json.{JsArray, JsObject, JsString, Json}

import scala.util.{Failure, Success, Try}

class SimpleTest extends org.scalatest.FunSuite {
  test("SimpleTest") {
    println("SimpleTest")
    assert(1 + 1 === 2)
  }
  test("simple json test") {
    val empty = Json.toJson("")
    println(empty) // ""
    val emptyObj = Json.obj()
    println(emptyObj) // {}
    println(emptyObj.equals(JsObject.empty)) // true


    val jsString = JsString("someString")
    println(jsString)

    val pojoString = "someString"
    val toJson = Json.toJson(pojoString) // toJson() returns JsValue
    println(toJson)

    val stringList = Seq("str1", "str2")
    println(Json.toJson(stringList)) // ["str1","str2"]


    val jsObj = Json.obj("key" -> "value") // JsObject
    println(jsObj)
    println(jsObj.toString) // {"key":"value"}

    val jsObj1 = Json.obj("key1" -> "value1")
    val jsObj2 = jsObj ++ jsObj1
    println(jsObj2) // {"key":"value","key1":"value1"}

    val jsObj3 = JsObject(Map("key1" -> jsObj, "key2" -> jsObj))
    println(jsObj3) // {"key1":{"key":"value"},"key2":{"key":"value"}}
    val jsObj4 = Json.obj("key1" -> jsObj, "key2" -> jsObj)
    println(jsObj4) // SAME

    // extracting from json
    val value = jsObj2 \ "key"
    println(value) // JsDefined("value")
    val valueForNoKey = jsObj2 \ "foo"
    println(valueForNoKey) // JsUndefined('foo' is undefined on object: {"key":"value","key1":"value1"})

    val value1 = Try {
      (jsObj2 \ "key1").as[String]
    } match {
      case Success("value") => "matched value" // "matched value"
      case Success(value) => value // "value1"
      case Failure(exception) => println("failure:" + exception)
    }
    println(value1) // value

    val valueForNoKey1 = Try {
      (jsObj2 \ "foo").as[String]
    } match {
      case Success(value) => value
      case Failure(exception) => println("failure:" + exception)
        // failure:play.api.libs.json.JsResultException: JsResultException(errors:
        // List((,List(JsonValidationError(List('foo' is undefined on object: {"key":"value","key1":"value1"}),ArraySeq())))))
    }
    println(valueForNoKey1) // () ???
    // val valueForNoKey1 = (jsObj2 \ "foo").as[String]
    // JsResultException

  }
  test("Product to json test") {
    val p = Product(5010255079763L, "Paperclips Large", "Large Plain Pack of 1000")

    implicit val productFormat = Json.format[Product] // No Json serializer found for type models.Product. Try to implement an implicit Writes or Format for this type.
    val toJson = Json.toJson(p)
    println(toJson) // {"ean":5010255079763,"name":"Paperclips Large","description":"Large Plain Pack of 1000"}
  }
  test("json to Product test") {
    val p = Product(5010255079763L, "Paperclips Large", "Large Plain Pack of 1000")
    val json =
      """
        |{"ean":5010255079763,"name":"Paperclips Large","description":"Large Plain Pack of 1000"}
        |""".stripMargin

    implicit val productFormat = Json.format[Product] // No Json serializer found for type models.Product. Try to implement an implicit Writes or Format for this type.
    val result = Json.parse(json).as[Product] // parse() returns JsValue
    println(result) // Product(5010255079763,Paperclips Large,Large Plain Pack of 1000)
    println(result.equals(p))
  }
  test("jsArray") {
    val p = Product(5010255079763L, "Paperclips Large", "Large Plain Pack of 1000")
    val json =
      """
        |{"ean":5010255079763,"name":"Paperclips Large","description":"Large Plain Pack of 1000"}
        |""".stripMargin

    implicit val productFormat = Json.format[Product] // No Json serializer found for type models.Product. Try to implement an implicit Writes or Format for this type.
    val jsValue = Json.parse(json) // parse() returns JsValue
    println(jsValue.getClass) // class play.api.libs.json.JsObject
    println(jsValue) // {"ean":5010255079763,"name":"Paperclips Large","description":"Large Plain Pack of 1000"}
    val jsValue1 = Json.parse(json)

    val jsArray0 = JsArray()
    println(jsArray0) // []
    val jsArray1 = Json.arr(jsValue)
    println(jsArray1) // [{"ean":5010255079763,"name":"Paperclips Large","description":"Large Plain Pack of 1000"}]
    val jsArray2 = Json.arr(jsValue, jsValue1)
    println(jsArray2) // [{"ean":5010255079763,"name":"Paperclips Large","description":"Large Plain Pack of 1000"},{"ean":5010255079763,"name":"Paperclips Large","description":"Large Plain Pack of 1000"}]
    val jsArray3 = jsArray1 ++ jsArray2
    println(jsArray3) // [{xxx},{xxx},{xxx}]
  }
}
