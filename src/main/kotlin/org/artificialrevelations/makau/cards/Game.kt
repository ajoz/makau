package org.artificialrevelations.makau.cards

/*
class Player
class Game(val players: List<Player>)

or

class Game(val activePlayer: Player, val players: List<Player>)


 */

sealed class Game {

}

data class TwoPlayer(
        val playerOne: Player,
        val playerTwo: Player
) : Game()

data class ThreePlayer(
        val playerOne: Player,
        val playerTwo: Player,
        val playerThree: Player
) : Game()

data class FourPlayer(
        val playerOne: Player,
        val playerTwo: Player,
        val playerThree: Player,
        val playerFour: Player
) : Game()