package klara.schueler

import klara.mongo.EntityActor
import klara.entity._
import klara.entity.validation._

import klara.schueler.Schueler._

import klara.system._
import klara.system.Severities._


case class Test(msg: String)


class SchuelerActor extends EntityActor[Schueler]("schueler", Schueler) {

  override def receive = super.receive orElse {
    case Test(msg) => log.debug(msg)
  }

}