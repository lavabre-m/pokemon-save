package pkm.ia

import pkm.models._

/**
  * Created by melanie on 06/02/17.
  */
class AggressiveIA extends IA{
  override def chooseAction(myself: Trainer, opponent: Trainer): GameAction = {
    val fightingPokemon = myself.pokemons.head
    val attack = chooseAttackType(myself, fightingPokemon)
    val pWithA = PokemonWithAttack(fightingPokemon, attack)
    val oppponentsCumulatedPokemonsHp = opponent.pokemons.map(_.stats.hp).sum
    if (opponent.hp < oppponentsCumulatedPokemonsHp ) {
      AttackTrainer(pWithA)
    }
    else{
      AttackPokemon(pWithA)
    }
  }
}
