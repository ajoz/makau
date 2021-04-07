package org.artificialrevelations.makau.capabilities

import org.artificialrevelations.makau.cards.Card

sealed interface Capability

object ForfeitGame : Capability

data class PlayCard(val card: Card) : Capability
object CancelPlay : Capability

object DrawCard : Capability
object SkipTurn : Capability // <- when handling 4s
object EndTurn : Capability


