package org.artificialrevelations.makau.cards

/**
 * Describes a concept of a deck of cards. Depending on
 * the game the game it can have a different set of cards.
 *
 * @see Card
 */
data class Deck(val cards: List<Card>)