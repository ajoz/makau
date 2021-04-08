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

How to indicate which player should do something. Usually the capabilities
sent are related to the current active player. We would like to cover the case
of attacking the previous player with a King of Spades and the next player with
a King of Hearts.

Let's start with attacking the previous player. The previous player has options:
- draw 5 cards
- draw 1 card and check if there is a king of hearts to play it immediately to
  make a counter attack and if it is not there then draw the other 4 cards
- play a king of hearts from his hand

If the capabilities are always associated with the active player, how can we send
the capabilities for the previous player?

Add player information to the capabilities? This seems ok for:
- draw card capability
- play card capability

but does not have sense for other capabilities, do we want to pollute their
design with this additional info?

Create a distinction between ACTIVE player (which is more related to the TURN
order) and CURRENT player which is the player for which we sent capabilities.
Usually ACTIVE player and CURRENT player could be the same player but in these
rare occasions ACTIVE can be different then CURRENT.

This might solve the issue for the niche case of playing king of spades and a
king of hearts but creates a different way of handling turn to turn gameplay.

 */
}

