package pkm.models

/**
  * Created by melanie on 07/02/17.
  */
class Brouillon {

  /* case(AttackPokemon,AttackPokemon) => {
      (Some(updatedTrainer2.attackPokemonBasicAttack(updatedTrainer1, false)), Some(updatedTrainer1.attackPokemonBasicAttack(updatedTrainer2, false)))
    }
    case(Action.attackPokemonPower, Action.attackPlayer) => {
      (updatedTrainer2.attakPlayer(updatedTrainer1, 1), Some(updatedTrainer1.attackPokemonBasicAttack(updatedTrainer2, false)))
    }
    case(Action.attackPlayer, Action.attackPokemonPower) => {
      ( Some(updatedTrainer2.attackPokemonBasicAttack(trainer1, false)), updatedTrainer1.attakPlayer(updatedTrainer2, 1) )
    }
    case(Action.attackPlayer, Action.attackPlayer) =>{
      ( updatedTrainer2.attakPlayer(trainer1, 1), updatedTrainer1.attakPlayer(updatedTrainer2, 1) )
    }
    case( Action.defend , Action.attackPokemonPower) =>  {
      (Some(updatedTrainer2.attackPokemonBasicAttack(updatedTrainer1, true)), Some(updatedTrainer2.defendAgainstPokemonAttack))
    }
    case(Action.attackPokemonPower, Action.defend) => {
      (Some(updatedTrainer1.defendAgainstPokemonAttack), Some(updatedTrainer1.attackPokemonBasicAttack(updatedTrainer2, true)))
    }
    case(Action.defend, Action.attackPlayer) => {
      (updatedTrainer2.attakPlayer(updatedTrainer1, 0.25), updatedTrainer2.playerDefenseKickBack())
    }
    case (Action.attackPlayer, Action.defend) => {
      (updatedTrainer1.playerDefenseKickBack(), updatedTrainer1.attakPlayer(updatedTrainer2, 0.25))
    }
    case _ => {
      (Some(updatedTrainer1.pokemonHealing()), Some(updatedTrainer2.pokemonHealing()))
    }*/


  /*

  if (actionP1 == attackPokemon && actionP2 == attackPokemon) {
    (Some(player2.attakPokemonBasicAttak(player1, false)), Some(player1.attakPokemonBasicAttak(player2, false)))
  }
  else if (actionP1 == attackPokemon && actionP2 == attackPlayer) {

    (player2.attakPlayer(player1, 1), Some(player1.attakPokemonBasicAttak(player2, false)))
  }
  else if (actionP1 == attackPlayer && actionP2 == attackPokemon) {
    ( Some(player2.attakPokemonBasicAttak(player1, false)), player1.attakPlayer(player2, 1) )
  }
  else if (actionP1 == attackPlayer && actionP2 == attackPlayer) {
    ( player2.attakPlayer(player1, 1), player1.attakPlayer(player2, 1) )
  }
  else if (actionP1 == defend && actionP2 == attackPokemon) {
    (Some(player2.attakPokemonBasicAttak(player1, true)), Some(player2))
  }
  else if (actionP1 == attackPokemon && actionP2 == defend) {

    (Some(player1), Some(player1.attakPokemonBasicAttak(player2, true)))
  }
  else if (actionP1 == defend && actionP2 == attackPlayer) {
    (player2.attakPlayer(player1, 0.25), player2.playerDefenseKickBack())
  }
  else if (actionP1 == attackPlayer && actionP2 == defend) {
    (player1.playerDefenseKickBack(), player1.attakPlayer(player2, 0.25))
  }
  //else (actionP1 == denfend && actionP2 == defend)
  else {
    (Some(player1.pokemonHealing()), Some(player2.pokemonHealing()))
  }


  def attackPokemonAttack(opponent: Trainer, minorated: Boolean): Trainer = {

    def updatePokemon(p: Option[Pokemon]): Option[Pokemon] = {
      val basicDamage = pokemons.head.basicAttack.damage
      val damage = if (minorated) {
        (basicDamage * 0.75).toInt
      } else {
        basicDamage
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
      val firstPokemon = opponent.pokemons.headOption
      //! -> headOption et pas simplement head --> OK
      val newPokemon = updatePokemon(firstPokemon)
      newPokemon match {
        case Some(p) => p :: opponent.pokemons.tail
        case None => opponent.pokemons.tail
      }
    }

    val newMana = opponent.mana + 1
    opponent.copy(mana = newMana, pokemons = updatePokemonList())
  }

  def attakPlayer(opponent: Trainer, percentage: Double): Option[Trainer] = {
    val damage = (pokemons.head.basicAttack.damage * percentage).toInt

    val newMana = opponent.mana + 1

    val newPlayerHp = opponent.hp - damage
    if (newPlayerHp > 0) {
      Some(opponent.copy(hp = newPlayerHp, mana = newMana))
    }
    else {
      None
    }
  }

  def playerDefenseKickBack(): Option[Trainer] = {
    val damage = pokemons.head.basicAttack.damage
    val newPlayerHp = hp - (damage * 0.5).toInt
    if (newPlayerHp > 0) {
      val newMana = mana + 1
      Some(this.copy(hp = newPlayerHp, mana = newMana))
    }
    else {
      None
    }
  }

  def pokemonHealing(): Trainer = {
    def updatePokemon(): Option[Pokemon] = {
      val firstPokemon = pokemons.headOption
      firstPokemon match {
        case Some(p) => {
          val newHp = p.stats.hp * 1.1.toInt
          Some(p.copy(stats = p.stats.copy(hp = newHp)))
        }
        case None => None
      }
    }

    def updatePokemonList(): List[Pokemon] = {
      updatePokemon() match {
        case Some(p) => p :: pokemons.tail
        case None => pokemons.tail
      }
    }

    val newMana = mana + 1
    this.copy(mana = newMana, pokemons = updatePokemonList())
  }

  // No damages done
  def defendAgainstPokemonAttack(): Trainer = {
    val newMana = mana + 1
    this.copy(mana = newMana)
  }
  */

  // println("Number of Pokemons:"+Pokedex.pokemons.length)
  // val randomPokemon = Pokedex.pokemons(Random.nextInt(Pokedex.pokemons.length))
  // println(s"Random: $randomPokemon")
  // val listPokemonPlayer1 = List.fill(6)(Pokedex.pokemons(Random.nextInt(Pokedex.pokemons.length)))
  // val listPokemonPlayer2 = List.fill(6)(Pokedex.pokemons(Random.nextInt(Pokedex.pokemons.length)))
  // val player1 = Trainer(name = "1", pokemons = listPokemonPlayer1, iaStrategy = randomIA)
  // val player2 = Trainer(name = "2", pokemons = listPokemonPlayer2, iaStrategy = randomIA)

}
