package controllers

import play.mvc.Http
import play.api.mvc._
import scala.Left
import scala.Right
import play.api.Logger
import play.api.http.HeaderNames

/**
 * Controller with basic authentication.
 */
object Application extends Controller {

  def action = Action {
    request => Ok("Response…")
  }

  def authenticate(request: Request[AnyContent]) = true

  def AuthenticatedActionOutline(f: Request[AnyContent] => Result):
    Action[AnyContent] = {

    Action { request =>
      if (authenticate(request)) {
        f(request)
      }
      else {
        Unauthorized
      }
    }
  }

  def outline = AuthenticatedActionOutline { request =>
    Ok("Authenticated response…")
  }


  def readQueryString(request: Request[_]):
  Option[Either[Result, (String, String)]] = {

    request.queryString.get("user").map { user =>
      request.queryString.get("password").map { password =>
        Right((user.head, password.head))
      }.getOrElse {
        Left(BadRequest("Password not specified"))
      }
    }
  }

  def authenticate(user: String, password: String) = {
    Logger.info("authenticate " + user + " " + password)
    true
  }

  def readBasicAuthentication(headers: Headers):
    Option[Either[Result, (String, String)]] = {

    headers.get(Http.HeaderNames.AUTHORIZATION).map {
      header =>
        val BasicHeader = "Basic (.*)".r
        header match {
          case BasicHeader(base64) => {
            try {
              import org.apache.commons.codec.binary.Base64
              val decodedBytes = Base64.decodeBase64(base64.getBytes)
              val credentials = new String(decodedBytes).split(":", 2)
              if (credentials.length != 2) {
                Left(BadRequest("Invalid basic authentication"))
              } else {
                val (user, password) = (credentials(0), credentials(1))
                Right((user, password))
              }
            }
          }
          case _ => Left(BadRequest("Invalid Authorization header"))
        }
    }
  }

  def AuthenticatedAction(f: Request[AnyContent] => Result):
    Action[AnyContent] = {

    Action {
      request =>
        val maybeCredentials = readQueryString(request) orElse
          readBasicAuthentication(request.headers)
        maybeCredentials.map { resultOrCredentials =>
          resultOrCredentials match {
            case Left(errorResult) => errorResult
            case Right(credentials) => {
              val (user, password) = credentials
              if (authenticate(user, password)) {
                f(request)
              }
              else {
                Unauthorized("Invalid user name or password")
              }
            }
          }
        }.getOrElse {
          val authenticate = (HeaderNames.WWW_AUTHENTICATE, "Basic")
          Unauthorized.withHeaders(authenticate)
        }
    }
  }


  def index = AuthenticatedAction { request =>
    Ok("Authenticated response…")
  }

}