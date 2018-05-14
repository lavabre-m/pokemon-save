package pkm

import scala.util.Random
import pkm.data.Pokedex
import models._
import pkm.game.TurnManager

import scala.collection.mutable
import scala.io.StdIn

object Main extends App {

  val numberOfFightingPokemons = 2
  val turnManager = new TurnManager()
  turnManager.startAPokemonFight(numberOfFightingPokemons)

}
