import org.scalatest.{FlatSpec, Matchers}
import pkm.data.Pokedex
import pkm.game.{FightSteps, TurnManager}
import pkm.ia.RandomIA
import pkm.models._
import pkm.models.TurnManager._

/**
  * Created by melanie on 07/02/17.
  */
class TurnManagerSpec extends FlatSpec with Matchers {

  val herbizarre = Pokedex.pokemons(0)
  val reptincel = Pokedex.pokemons(1)

  val woundedHerbizarre = herbizarre.copy(stats = herbizarre.stats.copy(hp = 100))
  val woundedReptincel= reptincel.copy(stats = herbizarre.stats.copy(hp = 100))

  val carabaffe= Pokedex.pokemons(2)
  val papilusion = Pokedex.pokemons(3)
  val pokemonListT1 = List(herbizarre, reptincel)
  val pkemonListT2 = List(carabaffe, papilusion)
  val woundedPokemonListT1 = List(woundedHerbizarre)
  val woundedPokemonListT2 = List(woundedReptincel)
  val randomIA = new RandomIA()

  val basicAttackT1 = herbizarre.basicAttack
  val powerAttackT1 = herbizarre.powerAttack

  val basicAttackT2 = carabaffe.basicAttack
  val powerAttackT2 = carabaffe.powerAttack

  val attackPkmBasicT1 = AttackPokemon(PokemonWithAttack(herbizarre,basicAttackT1))
  val attackPkmPowerT1 = AttackPokemon(PokemonWithAttack(herbizarre,powerAttackT1))

  val defendTrainer1 = DefendTrainer(PokemonWithAttack(herbizarre,basicAttackT1))

  val attackPkmBasicT2 = AttackPokemon(PokemonWithAttack(carabaffe,basicAttackT2))
  val attackPkmPowerT2=  AttackPokemon(PokemonWithAttack(carabaffe,powerAttackT2))

  val attackTrnrBasicT1 = AttackTrainer(PokemonWithAttack(herbizarre,basicAttackT1))
  val attackTrnrPowerT1=  AttackTrainer(PokemonWithAttack(herbizarre,powerAttackT1))

  val attackTrnrBasicT2 = AttackTrainer(PokemonWithAttack(carabaffe,basicAttackT2))
  val attackTrnrPowerT2=  AttackTrainer(PokemonWithAttack(carabaffe,powerAttackT2))

  val defendTrainer2 = DefendTrainer(PokemonWithAttack(carabaffe, basicAttackT2))

  val turnManager = new TurnManager()

  val trainerTest = Trainer(name ="1", mana = 20, pokemons = pokemonListT1, iaStrategy = randomIA)
  val trainer1TestWithWoundedPokemon = Trainer(name ="1", mana = 20, pokemons = woundedPokemonListT1, iaStrategy = randomIA)
  val trainer2TestWithWoundedPokemon = Trainer(name ="1", mana = 20, pokemons = woundedPokemonListT2, iaStrategy = randomIA)

  it should "test mana update for basic attack --> no mana update" in {
  val trainer = FightSteps.updateManaIfPowerAttack(attackPkmBasicT1, attackPkmBasicT2).run(trainerTest)
     trainer.mana shouldBe 20
  }

  it should "test mana update for power attack" in {
    val trainer = FightSteps.updateManaIfPowerAttack(attackPkmPowerT1, attackPkmBasicT2).run(trainerTest)
    trainer.mana shouldBe 17
  }

  it should "test pokemon hp update after basic attack" in {
    val trainer = FightSteps.affectDamages(attackPkmPowerT1, attackPkmBasicT2).run(trainerTest)
    trainer.pokemons.head.stats.hp shouldBe 140
  }

  it should "test pokemon hp update after powerattack" in {
    val trainer = FightSteps.affectDamages(attackPkmPowerT1, attackPkmPowerT2).run(trainerTest)
    trainer.pokemons.head.stats.hp shouldBe 120
  }

  it should "test player hp update after basic attack" in {
    val trainer = FightSteps.affectDamages(attackPkmPowerT1, attackTrnrBasicT2).run(trainerTest)
    trainer.hp shouldBe 460
  }

  it should "test player hp update after powerattack" in {
    val trainer = FightSteps.affectDamages(attackPkmPowerT1, attackTrnrPowerT2).run(trainerTest)
    trainer.hp shouldBe 440
  }

  it should "test player hp update after basicAttack while defending himself only affected by 25% of damages " in {
    val trainer = FightSteps.affectDamages(defendTrainer1, attackTrnrBasicT2).run(trainerTest)
    trainer.hp shouldBe 490
  }

  it should "test player hp update after powerAttack while defending himself only affected by 25% of damages " in {
    val trainer = FightSteps.affectDamages(defendTrainer1, attackTrnrPowerT2).run(trainerTest)
    trainer.hp shouldBe 485
  }

  it should "test player unwounded pokemon hp update pokemon healing because both defending - make sure hp is not higher than basic hp" in {
    val trainer = FightSteps.affectDamages(defendTrainer1, defendTrainer2).run(trainerTest)
    trainer.pokemons.head.stats.hp shouldBe 180
  }

  it should "test player wounded pokemon hp update pokemon healing because both defending" in {
    val trainer = FightSteps.affectDamages(defendTrainer1, defendTrainer2).run(trainer1TestWithWoundedPokemon)
    trainer.pokemons.head.stats.hp shouldBe 110
  }

  it should "test consequences of opponent kickBack ( opponenent defending himself while being personnaly BasicAttacked" in {
    val trainer = FightSteps.affectDamages(attackTrnrBasicT1, defendTrainer2).run(trainerTest)
    trainer.hp shouldBe 478
  }

  it should "test consequences of opponent kickBack ( opponenent defending himself while being personnaly PowerAttacked" in {
    val trainer = FightSteps.affectDamages(attackTrnrPowerT1, defendTrainer2).run(trainerTest)
    trainer.hp shouldBe 473
  }

  it should "test end of turn mana update" in {
  val trainer = FightSteps.updateEndOfTurnMana().run(trainerTest)
    trainer.mana shouldBe 21
  }



}
