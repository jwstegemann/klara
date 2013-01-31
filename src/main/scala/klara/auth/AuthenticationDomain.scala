package klara.auth

import reactivemongo.bson.BSONArray
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONString
import reactivemongo.bson.handlers.BSONReader
import spray.json.DefaultJsonProtocol


case class LoginRequest(username: String, password: String)

case class UserContext(username: String, info: String, firstName: String, lastName: String, permissions: List[String])

object UserContext {
  implicit object BSONReader extends BSONReader[UserContext] {
    def fromBSON(document: BSONDocument): UserContext = {
      val doc = document.toTraversable

      UserContext(
        doc.getAs[BSONString]("username").get.value,
        doc.getAs[BSONString]("info").get.value,
        doc.getAs[BSONString]("firstName").get.value,
        doc.getAs[BSONString]("lastName").get.value,
        doc.getAs[BSONArray]("permissions").get.toTraversable.toList.map { bsonString =>
          bsonString.asInstanceOf[BSONString].value
        })
    }
  }
}

object KlaraAuthJsonProtocol extends DefaultJsonProtocol {
  implicit val loginRequestFormat = jsonFormat2(LoginRequest)
  implicit val UserContextFormat = jsonFormat5(UserContext.apply)
}

