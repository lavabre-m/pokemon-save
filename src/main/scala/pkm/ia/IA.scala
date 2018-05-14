package pkm.ia

import pkm.models.{Attack, GameAction, Pokemon, Trainer}

/**
  * Created by melanie on 06/02/17.
  */
trait IA {

  def chooseAction(myself: Trainer, opponent: Trainer): GameAction

  def chooseAttackType (myself: Trainer, pokemon: Pokemon) : Attack ={
    val manaCostOfPowerAttack = pokemon.powerAttack.cost
    if( (myself.mana-manaCostOfPowerAttack) > 0){
      pokemon.powerAttack   }
    else{
      pokemon.basicAttack
    }
  }

}
