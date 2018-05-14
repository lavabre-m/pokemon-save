package pkm.models

import io.circe.Decoder

case class Pokemon(
  number: Int,
  name: String,
  `type`: String,
  stats: PokemonStats,
  basicAttack: BasicAttack,
  powerAttack: PowerAttack
) {
  override def toString = s"""Pokemon $number "$name" (${`type`}) - HP: ${stats.hp}"""
}

sealed trait Attack {
  val damage: Int
  val cost: Int = 0
}

case class PokemonType(name: String) extends AnyVal
case class PokemonStats(hp: Int, speed: Int)
case class BasicAttack(name: String, `type`: String, damage: Int) extends Attack
case class PowerAttack(name: String, `type`: String, damage: Int, override val cost: Int) extends Attack

case class PokemonWithAttack (val pokemon: Pokemon,attack: Attack)

trait PokemonDecoder {
  import io.circe.generic.semiauto._

  implicit protected val basicAttackDecoder = deriveDecoder[BasicAttack]
  implicit protected val powerAttackDecoder = deriveDecoder[PowerAttack]
  implicit protected val statsDecoder = deriveDecoder[PokemonStats]
  implicit protected val pokemonDecoder = Decoder.forProduct6("number", "name", "type", "stats", "basic-attack", "power-attack")(Pokemon.apply)
}
