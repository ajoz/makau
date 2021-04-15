package org.artificialrevelations.makau.game

import org.artificialrevelations.makau.cards.Card
import org.artificialrevelations.makau.turn.Turn

class Game(
    val turn: Turn,
    val deck: List<Card>,
    val current: Card,
    val played: List<Card>
)