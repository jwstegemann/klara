package klara.schueler

import klara.mongo.EntityActor
import klara.entity._

import klara.schueler.Schueler._

import klara.system._
import klara.system.Severities._


case class Test(msg: String)


class SchuelerActor extends EntityActor[Schueler]("schueler") {

  override def receive = super.receive orElse {
    case Test(msg) => log.debug(msg)
  }

  override def defaultChecks = checkName _ :: Nil

  def checkName(schueler: Schueler): Validation[Schueler].Result = {
  	if (name.startsWith("Ca")) Right(schueler)
  }

}