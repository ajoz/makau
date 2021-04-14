package org.artificialrevelations.makau.game

import org.artificialrevelations.makau.cards.Card
import org.artificialrevelations.makau.turn.Turn

class Game(
    val turn: Turn,
    val deck: List<Card>,
    val current: Card,
    val played: List<Card>
) {
/*
Topics:

- ForfeitGame capability sent always for every player? or sent only for the
active player? Should non active players need to wait for their turn to forfeit
the game?

- How to store information about the current played card state and previously
played cards? This has the most use for 2, 3, 4s because of cumulative effect
on the next player. This has use for Jack which demands a specific card. In case
of playing Jack and demanding a card, the active player who demanded the card
is supposed to play that card last. Each player needs to play the demanded card,
draw a card or play a Jack and demand a different card.

- Going from Game state to Game state through capabilities
getCapabilities :: Game -> [Capability]
run  :: Capability -> Game

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
 */
}

