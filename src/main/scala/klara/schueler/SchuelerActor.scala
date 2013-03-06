package klara.schueler

import klara.mongo.EntityActor
import klara.entity._

import klara.schueler.Schueler._

case class Test(msg: String)


class SchuelerActor extends EntityActor[Schueler]("schueler") {

  override def receive = super.receive orElse {
    case Test(msg) => log.debug(msg)
  }

}