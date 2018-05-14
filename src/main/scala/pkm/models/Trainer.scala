package pkm.models

import pkm.data.Pokedex
import pkm.ia.IA

/**
  * Created by melanie on 03/02/17.
  */
case class Trainer(
                    name: String,
                    hp: Int = 500,
                    mana: Int = 0,
                    pokemons: List[Pokemon],
                    iaStrategy: IA) {

  /* BRAND NEW CODE WATCH OUT. The first one is so exciting!!*/
  def affectDamagesToFightingPokemon(myAction: GameAction, opponentAction: AttackPokemon, minorated: Boolean): Trainer = {

    def updatePokemon(p: Option[Pokemon]): Option[Pokemon] = {
      val opponentPokemonDamage = opponentAction.attack.attack.damage
      val damage = if (minorated) {
        (opponentPokemonDamage * 0.75).toInt
      } else {
        opponentPokemonDamage
      }
      p match {
        case Some(pokemon) => {
          val newHp = pokemon.stats.hp - damage
          if (newHp > 0) {
            Some(pokemon.copy(stats = pokemon.stats.copy(hp = newHp)))
          }
          else {
            None
          }
        }
        case None => None
      }
    }

    def updatePokemonList(): List[Pokemon] = {
      val newPokemon = updatePokemon(Some(myAction.attack.pokemon))
      newPokemon match {
        case Some(p) => p :: pokemons.tail
        case None => pokemons.tail
      }
    }

    this.copy(pokemons = updatePokemonList())
  }

  def affectDamagesToPlayer(opponentAction: AttackTrainer, percentage: Double): Trainer = {
    val damage = (opponentAction.attack.attack.damage * percentage).toInt
    val newPlayerHp = hp - damage
    this.copy(hp = newPlayerHp)
  }

  def pokemonHealing(myAction: DefendTrainer): Trainer = {
    def updatePokemon(myAction: GameAction): Option[Pokemon] = {
      val myFightingPokemon = pokemons.head
      val originalMaxHP = Pokedex.pokemons.find(pokemon => pokemon.number == myFightingPokemon.number).map(pokemon => pokemon.stats.hp)
      val maxHp = originalMaxHP match {
        case Some(int) => int
      }
      val newHp = (myFightingPokemon.stats.hp * 1.1).toInt
      if (newHp <= maxHp) {
        Some(myFightingPokemon.copy(stats = myFightingPokemon.stats.copy(hp = newHp)))
      }
      else {
        Some(myFightingPokemon.copy(stats = myFightingPokemon.stats.copy(hp = maxHp)))
      }
    }

    def updatePokemonList(): List[Pokemon] = {
      updatePokemon(myAction) match {
        case Some(p) => p :: pokemons.tail
        case None => pokemons.tail
      }
    }

    this.copy(pokemons = updatePokemonList())
  }

  override def toString = s"""Trainer $name HP: $hp Mana: $mana"""
}

sealed trait GameAction {
  val attack: PokemonWithAttack
}

case class AttackPokemon(attack: PokemonWithAttack) extends GameAction

case class AttackTrainer(attack: PokemonWithAttack) extends GameAction

case class DefendTrainer(attack: PokemonWithAttack) extends GameAction
