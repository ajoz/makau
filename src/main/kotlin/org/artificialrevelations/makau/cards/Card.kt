package org.artificialrevelations.makau.cards

/**
 * Describes a playing card with a [Suit] and [Rank].
 * @see Suit
 * @see Rank
 */
sealed class Card {
    /**
     * Plain [Card] that has a [Rank] and a [Suit].
     */
    data class Plain(val rank: Rank, val suit: Suit) : Card()

    /**
     * Special [Card] that does not have a specific [Suit] or
     * [Rank] but can have a meaning depending on the game.
     */
    object Joker : Card()
}