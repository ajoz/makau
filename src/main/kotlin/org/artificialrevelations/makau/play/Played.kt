package org.artificialrevelations.makau.play

import org.artificialrevelations.makau.capabilities.Capability
import org.artificialrevelations.makau.cards.Card
import org.artificialrevelations.makau.game.Player

interface Played {
    val card: Card
    val by: Player
    val capabilities: List<Capability>
}

/*

Card Rank Two Played ->
Turn moves to another Player ->
Is the Card resolved?
-> YES -> Card has no effect other then the need for a matching rank or suit
-> NO -> Card is not resolved yet thus available capabilities should be
related to playing another Two or Drawing

Card Rank Three Played ->
Turn moves to another Player ->
Is the Card resolved?
-> YES -> Card has no effect other then need for a matching rank or suit


 */