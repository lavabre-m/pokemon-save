package pkm.game

import pkm.data.Pokedex
import pkm.ia.{AggressiveIA, RandomIA}
import pkm.models._

import scala.io.StdIn
import scala.util.Random

/**
  * Created by melanie on 03/02/17.
  */

case class Step[S](run: S => S) {

  def andThen(step: Step[S]): Step[S] = {
    Step(s => step.run(run(s)))
  }
}

object FightSteps {

  def updateManaIfPowerAttack(myAction: GameAction, opponentAction: GameAction): Step[Trainer] = {
    Step(myself => {
      (myAction, opponentAction) match {
        case (AttackPokemon(PokemonWithAttack(pokemon, attack)), _) => {
          val newMana = myself.mana - attack.cost
          myself.copy(mana = newMana)
        }
        case (AttackTrainer(PokemonWithAttack(pokemon, attack)), _) => {
          val newMana = myself.mana - attack.cost
          myself.copy(mana = newMana)
        }
        case (_) => myself
      }
    }
    )
  }

    def affectDamages(myAction: GameAction, opponentAction: GameAction): Step[Trainer] = {
      Step(myself => {
        (myAction, opponentAction) match {
          case a @ (AttackPokemon(pokemonWithAT1), AttackPokemon(pokemonWithAT2)) => {
            myself.affectDamagesToFightingPokemon(myAction, a._2, false)
          }
          case a @ (AttackPokemon(pokemonWithAT1), AttackTrainer(pokemonWithAT2)) => {
            myself.affectDamagesToPlayer(a._2, 1)
          }
          case a @ (AttackTrainer(pokemonWithAT1), AttackPokemon(pokemonWithAT2)) => {
            myself.affectDamagesToFightingPokemon(myAction, a._2, false)
          }
          case a @ (AttackTrainer(pokemonWithAT1), AttackTrainer(pokemonWithAT2)) => {
            myself.affectDamagesToPlayer(a._2, 1)
          }
          case a @ (DefendTrainer(pokemonWithAT1), AttackPokemon(pokemonWithAT2)) => {
            myself.affectDamagesToFightingPokemon(myAction, a._2, true)
          }
          case a @(DefendTrainer(pokemonWithAT1), AttackTrainer(pokemonWithAT2)) => {
            myself.affectDamagesToPlayer(a._2, 0.25)
          }
          case a @ (AttackTrainer(pokemonWithAT1), DefendTrainer(pokemonWithAT2)) => {
            myself.affectDamagesToPlayer(a._1, 0.5)
          }
          case (AttackPokemon(pokemonWithAT1), DefendTrainer(pokemonWithAT2)) => {
            myself
          }
          case a @ (DefendTrainer(pokemonWithAT1), DefendTrainer(pokemonWithAT2)) => {
            myself.pokemonHealing(a._1)
          }
        }
      }
      )
    }

    def updateEndOfTurnMana(): Step[Trainer] = {
      Step(myself => {
        val newMana = myself.mana + 1
        myself.copy(mana = newMana)
      }
      )
    }
  }

  class TurnManager() {

    def startAPokemonFight(numberOfFightingPokemons: Int) = {
      val randomIA = new RandomIA()
      val aggressiveIA = new AggressiveIA()
      println("Welcome to pokemon Game")
      val pokemonListT1 = fillPokemonListRec(numberOfFightingPokemons)
      val pokemonListT2 = fillPokemonListRec(numberOfFightingPokemons)
      println("Let's fight")
      val trainer1 = Trainer(name = "1", hp= 350, pokemons = pokemonListT1, iaStrategy = aggressiveIA)
      val trainer2 = Trainer(name = "2", hp=350, pokemons = pokemonListT2, iaStrategy = aggressiveIA)
      turnRec(trainer1, trainer2, 0)
    }

    def turnActionsSeq(initialTrainer: Trainer, myAction: GameAction, opponentAction: GameAction) = {
      val actions = FightSteps.updateManaIfPowerAttack(myAction, opponentAction)
        .andThen(FightSteps.affectDamages(myAction, opponentAction)).
        andThen(FightSteps.updateEndOfTurnMana())
      actions.run(initialTrainer)
    }

    def turnRec(trainer1: Trainer, trainer2: Trainer, counter: Int): Unit = {
      guessGameState(trainer1, trainer2) match {
        case P1Win => println(" And the winner is : .... Both players dead ?!? Amazing fight")
        case P2Win => println("")
        case ExAequo => println("")
        case Running =>
          println("Round #" + counter)
          println("Stats before round :")
          println("Trainer " + trainer1 + " and " + trainer1.pokemons.size + " pokemons"+trainer1.pokemons)
          println("Trainer " + trainer2 + " and " + trainer2.pokemons.size + " pokemons"+trainer2.pokemons)
          println("")
          val trainer1Action = trainer1.iaStrategy.chooseAction(trainer1, trainer2)
          val trainer2Action = trainer2.iaStrategy.chooseAction(trainer2,trainer1)
          println("Player1 " + trainer1Action + " Player2 " + trainer2Action)
          val newTrainer1 = turnActionsSeq(trainer1, trainer1Action, trainer2Action)
          val newTrainer2 = turnActionsSeq(trainer2, trainer2Action, trainer1Action)
          turnRec(newTrainer1, newTrainer2, counter + 1)
        }
      }

    sealed trait GameState
    case object Running extends GameState
    case object P1Win extends GameState
    case object P2Win extends GameState
    case object ExAequo extends GameState

    private def guessGameState(trainer1: Trainer, trainer2: Trainer): GameState = {
      if (trainer1.hp <= 0 && trainer2.hp <= 0)
        ExAequo
      else if (trainer1.hp <= 0 || trainer1.pokemons.isEmpty)
        P2Win
      else if (trainer2.hp <= 0 || trainer2.pokemons.isEmpty)
        P1Win
      else
        Running
    }


    def printAllAvailablePokemons = {
      println("Available pokemons")
      println(Pokedex.pokemons.mkString("\n"))
      val randomPokemon = Pokedex.pokemons(Random.nextInt(Pokedex.pokemons.length))
      println(s"Random: $randomPokemon")
    }

    def fillPokemonListRec(numberOfFightingPokemons: Int): List[Pokemon] = {
      printAllAvailablePokemons
      println("Please select your pokemons for this combat " + numberOfFightingPokemons + " pokemons needed ")

      def fillPokemonRecHelper(acc: List[Pokemon]): List[Pokemon] = {
        if (acc.length == numberOfFightingPokemons)
          acc
        else {
          val pokemonNumber = StdIn.readInt()
          val correspondingPkm = Pokedex.pokemons.find(pokemon => pokemon.number == pokemonNumber)
          correspondingPkm match {
            case Some(p) => {
              println("choosen pokemon is " + p + " is added to combat list")
              fillPokemonRecHelper(p :: acc)
            }
            case None => fillPokemonRecHelper(acc)
          }
        }
      }

      fillPokemonRecHelper(List[Pokemon]())
    }

    def checkIfGameIsOver(player1: Trainer, player2: Trainer) = {
      if (player1.hp >= 0)
        println("Player 1 alive with : " + player1 + " " + player1.pokemons.size + " pokemons")
      else println("Player 1 is dead : RIP ")

      if (player2.hp >= 0)
        println("Player 2 alive with : " + player2.hp + "HP & " + player2.mana + " Mana " + player2.pokemons.size + " pokemons")
      else println("Player 2 is dead : RIP ")

    }

    def checkMoreInfoOnFightingPokemon(player1: Trainer, playerBeforeTurn: Trainer) = {
      println("Player state Before Turn : " + playerBeforeTurn + " \n fightingPokemonBeforeTurn : " + playerBeforeTurn.pokemons.head)
      if (!player1.pokemons.isEmpty) {
        val fightingPokemon = player1.pokemons.head
        println("Fighting pokemon of player" + player1.name + " is : " + fightingPokemon)
        println(" ")
      }

    }
  }
