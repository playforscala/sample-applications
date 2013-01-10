package amazon

import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Date

import org.apache.commons.codec.binary.Base64
import org.glassfish.grizzly.memory.ByteBufferWrapper

import com.ning.http.client.{ AsyncHttpClient, Request, RequestBuilder }
import com.ning.http.client.providers.grizzly.{ FeedableBodyGenerator, GrizzlyAsyncHttpProvider }

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import play.api.Play
import play.api.Play.current
import play.api.http.HeaderNames
import play.api.libs.iteratee.Iteratee
import play.api.libs.ws.WS
import play.api.mvc.{ BodyParser, RequestHeader, Result, Results }

object AmazonBodyParsers extends Results {

  private val dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");

  // We create our own client, since we need the Grizzly provider for the
  // FeedableBodyGenerator.
  private lazy val client = {
    val playConfig = WS.client.getConfig
    new AsyncHttpClient(new GrizzlyAsyncHttpProvider(playConfig),
      playConfig);
  }

  private val emptyBuffer = new ByteBufferWrapper(ByteBuffer.wrap(Array[Byte]()))

  def sign(method: String, path: String, secretKey: String,
    date: String, contentType: Option[String] = None,
    aclHeader: Option[String] = None) = {
    val message = List(method, "", contentType.getOrElse(""),
      date, aclHeader.map("x-amz-acl:" + _).getOrElse(""), path)
      .mkString("\n")

    // Play's Crypto.sign method returns a Hex string, 
    // instead of Base64, so we do hashing ourselves.
    val mac = Mac.getInstance("HmacSHA1")
    mac.init(new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA1"))
    val codec = new Base64()
    new String(codec.encode(mac.doFinal(message.getBytes("UTF-8"))))
  }

  def S3Upload(bucket: String, objectId: String) = BodyParser {
    requestHeader =>
      val awsSecret = Play.configuration.getString("aws.secret").get
      val awsKey = Play.configuration.getString("aws.key").get
      // Later improvement; block here when the content-length header is missing!
      val (request, bodyGenerator) = buildRequest(bucket, objectId, awsKey, awsSecret, requestHeader)
      S3Writer(objectId, request, bodyGenerator)
  }

  def S3Writer(objectId: String, request: Request, bodyGenerator: FeedableBodyGenerator): Iteratee[Array[Byte], Either[Result, String]] = {
    // We execute the request, but we can send body chunks afterwards.
    val responseFuture = client.executeRequest(request)

    Iteratee.fold[Array[Byte], FeedableBodyGenerator](bodyGenerator) {
      (generator, bytes) =>
        val isLast = false
        generator.feed(new ByteBufferWrapper(ByteBuffer.wrap(bytes)), isLast)
        generator
    } mapDone { generator =>
      val isLast = true
      generator.feed(emptyBuffer, isLast)
      val response = responseFuture.get
      response.getStatusCode match {
        case 200 => Right(objectId)
        case _ => Left(Forbidden(response.getResponseBody))
      }
    }
  }

  def buildRequest(bucket: String, objectId: String, key: String,
    secret: String, requestHeader: RequestHeader): (Request, FeedableBodyGenerator) = {
    val expires = dateFormat.format(new Date())
    val path = "/%s/%s" format (bucket, objectId)
    val acl = "public-read"
    val contentType = requestHeader.headers.get(HeaderNames.CONTENT_TYPE)
      .getOrElse("binary/octet-stream")
    val auth = "AWS %s:%s" format (key, sign("PUT", path, secret,
      expires, Some(contentType), Some(acl)))
    val url = "https://%s.s3.amazonaws.com/%s" format (bucket, objectId)
    val bodyGenerator = new FeedableBodyGenerator()
    val request = new RequestBuilder("PUT")
      .setUrl(url)
      .setHeader("Date", expires)
      .setHeader("x-amz-acl", acl)
      .setHeader("Content-Type", contentType)
      .setHeader("Authorization", auth)
      .setContentLength(requestHeader.headers.get(HeaderNames.CONTENT_LENGTH).get.toInt)
      .setBody(bodyGenerator)
      .build()
    (request, bodyGenerator)
  }
}
