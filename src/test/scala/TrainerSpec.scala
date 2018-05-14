import org.scalatest._
import pkm.data.Pokedex
import pkm.ia.RandomIA
import pkm.models._
/**
  * Created by melanie on 07/02/17.
  */
class TrainerSpec extends FlatSpec with Matchers{

  val herbizarre = Pokedex.pokemons(0)
  val pokemonList = List(herbizarre)
  val randomIA = new RandomIA()

  val opponentPokemonReptincel  = Pokedex.pokemons(1)
  val basicAttack = opponentPokemonReptincel.basicAttack
  val powerAttack = opponentPokemonReptincel.powerAttack
  val pWithBasicAttack = AttackPokemon(PokemonWithAttack(opponentPokemonReptincel, basicAttack))
  val pWithPowerAttack = AttackPokemon(PokemonWithAttack(opponentPokemonReptincel, powerAttack))

  val attackTrainerBasic = AttackTrainer(PokemonWithAttack(opponentPokemonReptincel, basicAttack))
  val attackTrainerPower =  AttackTrainer(PokemonWithAttack(opponentPokemonReptincel, powerAttack))

  val trainerAttackPokemon = AttackPokemon(PokemonWithAttack(herbizarre,herbizarre.basicAttack))


  val trainer = Trainer(name ="1", pokemons = pokemonList, iaStrategy = randomIA)

  it should "create a Trainer with specified parameters" in {
    val trainer = Trainer(name ="1", pokemons = pokemonList, iaStrategy = randomIA)
    trainer shouldBe Trainer(name="1",hp = 500, mana = 0, iaStrategy = randomIA, pokemons = pokemonList)
  }

  it should "affect damages of basic attack to pokemon without minoration" in {
    val trainerAfterBasicAttack = trainer.affectDamagesToFightingPokemon(trainerAttackPokemon,pWithBasicAttack,false)
    trainerAfterBasicAttack.pokemons.head.stats.hp shouldBe  140
  }

  it should "affect damages of power attack to pokemon without minoration" in {
    val trainerAfterPowerAttack = trainer.affectDamagesToFightingPokemon(trainerAttackPokemon,pWithPowerAttack,false)
    trainerAfterPowerAttack.pokemons.head.stats.hp shouldBe  120
  }

  it should "affect damages of basic attack to pokemon with minoration" in {
    val trainerAfterBasicAttack = trainer.affectDamagesToFightingPokemon(trainerAttackPokemon,pWithBasicAttack,true)
    trainerAfterBasicAttack.pokemons.head.stats.hp shouldBe  150
  }

  it should "affect damages of power attack to pokemon with minoration" in {
    val trainerAfterPowerAttack = trainer.affectDamagesToFightingPokemon(trainerAttackPokemon,pWithPowerAttack,true)
    trainerAfterPowerAttack.pokemons.head.stats.hp shouldBe  135
  }

  it should "affect damages of basic attack to trainer without minoration" in {
    val trainerAfterBasicAttack = trainer.affectDamagesToPlayer(attackTrainerBasic,1)
    trainerAfterBasicAttack.hp shouldBe  460
  }

  it should "affect damages of power attack to trainer without minoration" in {
    val trainerAfterPowerAttack = trainer.affectDamagesToPlayer(attackTrainerPower,1)
    trainerAfterPowerAttack.hp shouldBe  440
  }

  it should "affect damages of basic attack to trainer with minoration" in {
    val trainerAfterBasicAttack = trainer.affectDamagesToPlayer(attackTrainerBasic,0.25)
    trainerAfterBasicAttack.hp shouldBe  490
  }

  it should "affect damages of power attack to trainer with minoration" in {
    // case of kickaback, the enemy sends back half of damages
    val trainerAfterBasicAttack = trainer.affectDamagesToPlayer(trainerAttackPokemon,0.5)
    trainerAfterBasicAttack.hp shouldBe  478
  }

  it should "heal wounded pokemon" in {

    val trainerAfterBasicAttack = trainer.affectDamagesToFightingPokemon(trainerAttackPokemon,pWithBasicAttack,false) //pokemon hp = 140 / 180
    val trainerAfterHealing = trainerAfterBasicAttack.pokemonHealing(trainerAttackPokemon)
    trainerAfterHealing.pokemons.head.stats.hp shouldBe 154
  }

  it should "try to heal healthy pokemon" in {
    val trainerAfterHealing = trainer.pokemonHealing(trainerAttackPokemon)
    trainerAfterHealing.pokemons.head.stats.hp shouldBe 180
  }

}
