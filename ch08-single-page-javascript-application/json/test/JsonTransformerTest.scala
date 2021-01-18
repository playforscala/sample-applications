import models.Product
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import scala.language.postfixOps

/**
 * https://www.playframework.com/documentation/2.8.x/ScalaJsonTransformers
 */

object JsonTransformerTest {
  val json = Json.parse(
    """
      |{
      |  "key1" : "value1",
      |  "key2" : {
      |    "key21" : 123,
      |    "key22" : true,
      |    "key23" : [ "alpha", "beta", "gamma"],
      |    "key24" : {
      |      "key241" : 234.123,
      |      "key242" : "value242"
      |    }
      |  },
      |  "key3" : 234
      |}
      |""".stripMargin
  )
}

class JsonTransformerTest extends org.scalatest.FunSuite {
  import JsonTransformerTest._

  test("Case 1: Pick JSON value in JsPath") {
    println("Case 1")

    val jsonTransformer = (__ \ Symbol("key2") \ Symbol("key23")).json.pick
    val result = json.transform(jsonTransformer) // return JsResult
    println(result) // JsSuccess(["alpha","beta","gamma"],/key2/key23)

    val jsonTransformer1 = (__ \ Symbol("key2") \ Symbol("key23")).json.pick[JsArray]
    val result1 = json.transform(jsonTransformer1) // return JsResult
    println(result1) // JsSuccess(["alpha","beta","gamma"],/key2/key23) // SAME ?!
  }
  test("gizmo2gremlin") {
    println("g2g")

    val gizmo = Json.obj(
      "name" -> "gizmo",
      "description" -> Json.obj(
        "features" -> Json.arr( "hairy", "cute", "gentle"),
        "size" -> 10,
        "sex" -> "undefined",
        "life_expectancy" -> "very old",
        "danger" -> Json.obj(
          "wet" -> "multiplies",
          "feed after midnight" -> "becomes gremlin"
        )
      ),
      "loves" -> "all"
    )

    val gremlin = Json.obj(
      "name" -> "gremlin",
      "description" -> Json.obj(
        "features" -> Json.arr("skinny", "ugly", "evil"),
        "size" -> 30,
        "sex" -> "undefined",
        "life_expectancy" -> "very old",
        "danger" -> "always"
      ),
      "hates" -> "all"
    )

    val gizmo2gremlin = (
      (__ \ 'name).json.put(JsString("gremlin")) and
      (__ \ 'description).json.pickBranch((
        (__ \ 'size).json.update( of[JsNumber].map{ case JsNumber(size) => JsNumber(size * 3) } ) and
        (__ \ 'features).json.put( Json.arr("skinny", "ugly", "evil") ) and
        (__ \ 'danger).json.put(JsString("always"))
        ) reduce
      ) and
      (__ \ 'hates).json.copyFrom( (__ \ 'loves).json.pick )
      ) reduce

    val result = gizmo.transform(gizmo2gremlin)

    println(gremlin)
    println(result.get)
    println(result.get.equals(gremlin)) // true
  }
}
