package org.artificialrevelations.makau.play

import org.artificialrevelations.makau.capabilities.Capability
import org.artificialrevelations.makau.cards.Card
import org.artificialrevelations.makau.game.Player

interface Play {
    val card: Card
    val by: Player
    val capabilities: List<Capability>
}


