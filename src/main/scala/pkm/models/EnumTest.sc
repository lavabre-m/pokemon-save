import pkm.data.Pokedex
import pkm.models.Action
import pkm.models.Action._

import scala.util.Random

object enumTest{

  val listAction = Action.values.toList
  val randomAction = listAction(Random.nextInt(listAction.length))


}