package org.artificialrevelations.makau.capabilities

import org.artificialrevelations.makau.cards.Card
import org.artificialrevelations.makau.cards.Rank
import org.artificialrevelations.makau.cards.Suit

sealed interface Capability

object ForfeitGame : Capability

data class PlayCard(val card: Card) : Capability
object CancelPlay : Capability

// After playing a Jack
data class DemandRank(val rank: Rank) : Capability
// After playing an Ace
data class ChangeSuit(val suit: Suit) : Capability

object DrawCard : Capability
object SkipTurn : Capability // <- when handling 4s
object EndTurn : Capability


