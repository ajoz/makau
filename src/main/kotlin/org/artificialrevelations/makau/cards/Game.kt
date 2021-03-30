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
    val deck: List<Card>, // list of cards that are available for draw
    val current: Card,
    val played: List<Card> // list of played cards
) {
    val activePlayer = players.active
    val previousPlayer = players.previous
    val nextPlayer = players.next
}

/*
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
- if 
 */