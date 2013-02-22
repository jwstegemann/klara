package klara.schueler

import klara.mongo.EntityActor


/*
 * available message-types for this actor
 */
case class FindAll()
case class Create(item: Schueler)
case class Load(id: String)
case class Update(item: Schueler)
case class Delete(id: String)


//TODO: is it possible to get this from companion object
class SchuelerActor extends EntityActor[Schueler]("Schueler","schueler",Schueler.BSONReader,Schueler.BSONWriter) {

  //TODO: get standard calls to EntityActor
  def receive = {
    case FindAll() => findAll()
    case Create(item) => create(item)
    case Load(id) => load(id)
    case Update(item) => update(item)
    case Delete(id) => delete(id)
  }

}