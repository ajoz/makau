package org.artificialrevelations.makau.game

import org.artificialrevelations.makau.cards.Card
import org.artificialrevelations.makau.turn.TurnOrder

class Game(
    val turnOrder: TurnOrder,
    val deck: List<Card>,
    val discarded: List<Card>,
    val current: Card
)


/*
Card Rank Two Played ->
Turn moves to another Player ->
Is the Card resolved?
-> YES -> Card has no effect other then the need for a matching rank or suit
-> NO -> Card is not resolved yet thus available capabilities should be
related to playing another Two or Drawing

(We need to keep the information how many times a two was played, because it
impacts the amount of cards that will have to be drawn)

Card Rank Three Played ->
Turn moves to another Player ->
Is the Card resolved?
-> YES -> Card has no effect other then need for a matching rank or suit
-> NO -> Card is not resolved yet thus available capabilities should be related
to playing another Three or Drawing

(We need to keep the information how many times a three was played, because it
impacts the amount of cards that will have to be drawn)

Card Rank Four Played ->
Turn moves to another Player ->
Is the Card resolved?
-> YES -> Card has no effect other then need for a matching rank or suit
-> NO -> Card is not resolved yet, thus available capabilities should be related
to playing another Four or Loosing Turns

(We need to keep the information how many times a four was played, because it
impacts the amount of lost turns, we also need to remember how many turns a
certain player lost the turn for)

Card Suit Jack Played ->
Player playing the Card needs to demand a Rank ->
Is the Card resolved?
-> REACHED THE DEMANDING PLAYER -> that player needs to play the card
-> HAS NOT REACHED THE DEMANDING PLAYER -> player can either play the demanded
rank or draw a card or play a different Jack

(We need to keep the information about the demanding player - when the card
effect stops, the demanded card)

Card Suit Ace Played ->
Player playing the Card changes the Suit ->
Is the Card resolved?
-> NEXT PLAYER TURN -> that player can only play a card that has the demanded
suit

How to store information that are affecting different part of the game?

1) Store some information in Player type. In case of Player loosing some turns.
The amount of turns lost can be stored in the Player as a field.

data class Player(
    val hand: Hand,
    val numberOfTurnsLost: Int
) {
    init {
        require(numberOfTurnsLost >= 0)
    }
}

The problem is that if we would like to allow the rules of the game to be
configurable, we could have a situation where there is a game without a rule for
loosing a turn but all the Player instances will be created with numberOfTurnsLost
set to 0.

We cannot store information about how many cards should be drawn the same way.

2) Store multiple active rules information in the game object.

There should be an association between a card played and a rule that applies to
the game:

    card <-> rule

A list of active rules is contained within a game:

    game -> rule, rule, rule

The main issue with such split is that skip turn rule affects other rules:
- draw 2 cards rule affects the next player but if the next player needs to skip
  his turn then the draw 2 cards affect the next player after him
- the same situation applies to every other rule like draw 3, demanding a card
  by Jack does not affect a player that lost the turn





How should we pass information about the demanded Rank by a Jack?
1) send PlayCard(Card) capability then send Demand(Rank) or Demand(Suit) or
DemandCard(Card) - two capabilities one by one
2) send Demand(a = Rank, with = Card) or Demand(a = Suit, with = Card)
- one single capability instead of two one by one

Instead of a very generic DemandCard we could have a simplified DemandRank





 */