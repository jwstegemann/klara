package klara.dictionary

import klara.system.Dictionary

object Schulform extends Dictionary("Schulform") {
  type Schulform = Value
  val Grundschule = create("grundschule", "Grundschule", "Grundschule")
  val Hauptschule = create("hauptschule", "Hauptschule", "Hauptschule")
  val Realschule = create("realschule", "Realschule", "Realschule")
  val Gymnasium = create("gymnasium", "Gymnasium", "Gymnasium")
  val Gesamtschule = create("gesamtschule", "Gesamtschule", "Gesamtschule")
}

object Geschlecht extends Dictionary("Geschlecht") {
  type Geschlecht = Value
  val maennlich = create("m","männlich","männlich")
  val weiblich = create("f","weiblich","weiblich")
}
