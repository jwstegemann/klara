package klara.auth

import spray.json._

case class LoginRequest(username: String, password: String)

object KlaraAuthJsonProtocol extends DefaultJsonProtocol {
  implicit val LoginRequestFormat = jsonFormat2(LoginRequest)
}

