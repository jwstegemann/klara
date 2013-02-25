package klara.schueler

import klara.mongo.EntityActor
import klara.entity._



//TODO: is it possible to get this from companion object
class SchuelerActor extends EntityActor[Schueler]("Schueler","schueler",Schueler.BSONReader,Schueler.BSONWriter) {

  //TODO: get standard calls to EntityActor
  def receive = {
    case FindAll() => findAll()
    case Create(item : Schueler) => create(item)
    case Load(id) => load(id)
    case Update(item : Schueler) => update(item)
    case Delete(id) => delete(id)
  }
}