package klara.auth

import spray.json._

import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.handlers.BSONReader

case class LoginRequest(username: String, password: String)

case class KlaraUserContext(username : String, info : String, firstName : String, lastName : String, permissions : List[String])

object KlaraUserContext {
  implicit object BSONReader extends BSONReader[KlaraUserContext] {
    def fromBSON(document: BSONDocument) : KlaraUserContext = {
      val doc = document.toTraversable
	  
      KlaraUserContext(
        doc.getAs[BSONString]("username").get.value,
        doc.getAs[BSONString]("info").get.value,
        doc.getAs[BSONString]("firstName").get.value,
        doc.getAs[BSONString]("lastName").get.value,
        doc.getAs[BSONArray]("permissions").get.toTraversable.toList.map { bsonString =>
          bsonString.asInstanceOf[BSONString].value
        }
      )
    }
  }
}

object KlaraAuthJsonProtocol extends DefaultJsonProtocol {
  implicit val loginRequestFormat = jsonFormat2(LoginRequest)
  implicit val klaraUserContextFormat = jsonFormat5(KlaraUserContext.apply)
}

