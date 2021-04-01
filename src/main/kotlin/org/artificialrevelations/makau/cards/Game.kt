package org.artificialrevelations.makau.cards

import org.artificialrevelations.makau.CyclicList

private typealias Players = CyclicList<Player>

val Players.active
    get() = this[0]

val Players.previous
    get() = this[-1]

val Players.next
    get() = this[1]

class Game(
    private val players: Players,
    val deck: List<Card>,
    val current: Card,
    val played: List<Card>
) {
    val activePlayer = players.active
    val previousPlayer = players.previous
    val nextPlayer = players.next
}

/*
next :: Game -> [Capability]
run  :: Capability -> Game

How to store information about the current played card state?
- for the king of spades we have a situation where the previous player draws 5
cards unless he plays a king of hearts. Some issues:
2 player game (player #A, player #B)
* player #A plays king of spades
* player #B draws the cards

Q: Does the game go to the player #B? Or does the player #A plays the card again
because player #B already drawn 5 cards?

Turn order is:

#A -> #B -> #A -> #B

If the player #A plays king of spades it affects the previous player which is
the player #B, now if the player #B draws 5 cards, then the turn of the player
#A is finished, should the player #B now play a card?

In case of 3 player game this is much easier as:

#A -> #B -> #C -> #A -> #B -> #C -> #A

When player #A plays king of spades then the previous player (which is #C) draws
cards then game goes to player #B and allows him to play the card. It is the same
for games with more players.

- for the king of hearts we have a situation where active player plays it, next
player needs to draw, does the game move to him? I think yes!

sealed interface Capability

Playing a single card:

data class PlayCard(val card: Card): Capability

Playing a group of cards:

data class PlayCards(val groups: List<CardGroup>): Capability

- do we need to have a special PlayCard capability just for playing a single card?
  does not have special sense as we can have a Group consisting of a single card
  it is important if a player has several copies of a card then he can pick how
  many of them he plays and in what order

  C1, C2, C3, C4

  C1
  C2,
  C3,
  C4
  C1, C2
  C1, C3
  C1, C4
  ...
  C1, C2, C3, C4
  C1, C2, C4, C3
  C1, C3, C2, C4
  ...

  How should this be expressed as capabilities? If we would have a capability
  per card group then it will be a lot of capabilities for the client.

  If we split playing the card into picking and playing then we can make the
  player decide the order of cards.




object DrawCard : Capability
object ForfeitGame : Capability







Rules:
1) 2s force next player to draw 2 cards or place another 2s
2) 3s force next player to draw 3 cards or place another 3s
4) joker can substitute a picked card (it can be decided before game which)
5) 4s force the next player to loose the turn or place another 4s
6) jack demands a rank (cannot demand functional cards: 2, 3, 4, jacks, kings)
   -- all players need abide (even the playing player)
   -- only playing another jack and demanding a different rank can stop it
   -- can play multiple jacks at once
7) ace demands a suit
8) king of spades then the previous player draws 5 cards
9) king of hearts then the next player draws 5 cards

First card that the game starts from does not affect the first player
- if it is a 2 or 3 the player can put a card with the same suit or another 2 or 3,
  but does not cause the first player to draw 2 or 3 cards
- if it is a 4 the player can put a card with the same suit or another 4
 */