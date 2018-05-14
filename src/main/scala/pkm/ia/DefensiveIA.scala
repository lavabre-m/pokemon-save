package pkm.ia

import pkm.models._

import scala.util.Random

/**
  * Created by melanie on 06/02/17.
  */
class DefensiveIA extends IA{
  override def chooseAction(myself: Trainer, opponent: Trainer): GameAction = {
    val fightingPokemon = myself.pokemons.head
    val attack = chooseAttackType(myself, fightingPokemon)
    val pWithA = PokemonWithAttack(fightingPokemon, attack)
    val pWithBasicActionForDefense = PokemonWithAttack(fightingPokemon, fightingPokemon.basicAttack)
    val listOfGameAction = List(AttackPokemon(pWithA),DefendTrainer(pWithBasicActionForDefense))
    listOfGameAction(Random.nextInt(listOfGameAction.length))
  }
}
