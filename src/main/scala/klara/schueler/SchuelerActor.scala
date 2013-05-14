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

  def isValid(schueler: Schueler) {
  	checkName(schueler.name)
  }

  def checkName(name: String) = {
  	if (!name.startsWith("Ca")) throw new ValidationException(Message("ungueltiger Name",`ERROR`,"Der Name muss mit Ca beginnen.",Some("name")) :: Nil)
  }

}