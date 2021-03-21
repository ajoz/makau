package org.artificialrevelations.makau.cards

/*
We would like to design a Game type that describes a current state
of the Makau game. It should be possible to serialize the state of
the game and the type should be immutable.

Function responsible for moving the game forward should be stateless.

         processing
Game #1 ------------> Game #2

Problems:
- how to make the Game an immutable
- how to determine which player is the active player
- how to determine which player is the previous player
  -- in case of playing a king of Spades (previous player draws 5 cards)
- how to determine which player is the next player
  -- in case of playing a king of Hearts (next player draws 5 cards)
  -- in case of playing 2 (next player draws 2 cards)
  -- in case of playing 3 (next player draws 3 cards)
  -- in case of playing 4 (next player skips a turn)
  -- in case of playing Jack (all players in the order from next to active need
     to supply a demanded card(s), if they are no
- how to store information about the player after the next one (4 player game)
- how to express the cycle of the players


Solution #1

Keep the players as a list:
class Game(val players: List<Player>)

First we can set information about the active player:

class Game(
    val activePlayer: Player,
    val players: List<Player>
)

Then we need to set information about the next player:

class Game(
    val activePlayer: Player,
    val nextPlayer: Player,
    val players: List<Player>
)

Then we need to set information about the previous player:

class Game(
    val activePlayer: Player,
    val nextPlayer: Player,
    val previousPlayer: Player,
    val players: List<Player>
)

The list of players is the real problem as we need to implicitly assume they
are in the correct order. Any issue that will cause wrong order of the players
on the list can break our 4+ player game as we are bookkeeping only 3 players
or the neighbourhood of the active player so to speak.



2p: GA ---> GB ----> GA
3p: GA ----> GB ----> GC ----> GA
4p: GA ----> GB ----> GC ----> GD ----> GA


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