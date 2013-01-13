package karla.auth
import scala.concurrent.{ExecutionContext, Promise}
import spray.routing.authentication._

import reactivemongo.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import reactivemongo.api._
import reactivemongo.bson._
import reactivemongo.bson.handlers.DefaultBSONHandlers._
import play.api.libs.iteratee.Iteratee


case class MongoUserContext(username: String, info: String) 


object MongoAuthenticator {
	val digest = java.security.MessageDigest.getInstance("SHA-256")

	//TODO: get from config-DB
	val connection = MongoConnection( List( "localhost:27017" ) )
  	val db = connection("klara")
  	val collection = db("user")

  	def fromMongoDB: UserPassAuthenticator[MongoUserContext] = { 
  		case Some(userPass) => getUserContextFromDB(userPass)
  		case None => future { None }
  	}

  	def getUserContextFromDB(userPass : UserPass) = {
  		val query = BSONDocument("username" -> BSONString(userPass.user), "password" -> BSONString(sha(userPass.pass)))

  		collection.find(query).toList map { list => 
  			if (list.size == 1) {
  				Some(MongoUserContext(userPass.user, list(0).getAs[BSONString]("lastName").get.value))
			} 
			else {
				None
			}
  		}
  	}

  	/*
	def fromMongoDB: UserPassAuthenticator[MongoUserContext] = { userPassOption =>
	    Promise.successful(
	      userPassOption.flatMap { userPass =>
	      		val query = BSONDocument("username" -> BSONString(userPass.user), "password" -> BSONString(userPass.pass))

	      		val userList = collection.find(query).toList

	      		userList.onSuccess {
	      			case list => 
			      		if (list.size == 1) {
	    		  			Some(MongoUserContext(userPass.user, list(0).getAs[BSONString]("lastName").get.value))
	      				} 
	      				else {
	      					None
	      				}
	      		}

	      }
	    ).future
  	}
  	*/

  	def sha( s:String ) : String = {
  		val m = digest.digest(s.getBytes("UTF-8"));
  		m map {c => (c & 0xff) toHexString} mkString	
	}
}