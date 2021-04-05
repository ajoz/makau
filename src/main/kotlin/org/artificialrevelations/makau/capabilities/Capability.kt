package org.artificialrevelations.makau.capabilities

import org.artificialrevelations.makau.cards.Card

sealed interface Capability

// Overall Capability Category:
object ForfeitGame : Capability

// Gameplay Capability Category:
data class PlayCard(val card: Card) : Capability
object DrawCard : Capability
object SkipTurn : Capability

// Card Handling Capability Category:
data class SelectCard(val card: Card) : Capability
data class DeselectCard(val card: Card) : Capability
object DeselectAllCards : Capability
object PlaySelectedCards : Capability


