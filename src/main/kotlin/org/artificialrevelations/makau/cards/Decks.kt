package org.artificialrevelations.makau.cards

/**
 * Returns a standard 52-card deck of cards.
 */
fun getFrenchSuitedDeck(): Deck {
    val cards: List<Card> =
            Rank.values()
                    .flatMap { rank ->
                        Suit.values()
                                .map { suit ->
                                    Card.Plain(rank, suit)
                                }
                    }
                    .plus(Card.Joker)
                    .plus(Card.Joker)
    return Deck(cards)
}