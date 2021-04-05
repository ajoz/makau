package org.artificialrevelations.makau.cards

import org.artificialrevelations.makau.CyclicList

private typealias Players = CyclicList<Player>

val Players.active
    get() = this[0]

val Players.previous
    get() = this[-1]

val Players.next
    get() = this[1]

class Game(
    private val players: Players,
    val deck: List<Card>,
    val current: Card,
    val played: List<Card>
) {
    val activePlayer = players.active
    val previousPlayer = players.previous
    val nextPlayer = players.next
/*
next :: Game -> [Capability]
run  :: Capability -> Game

How to store information about the current played card state?
- for the king of spades we have a situation where the previous player draws 5
cards unless he plays a king of hearts. Some issues:
2 player game (player #A, player #B)
* player #A plays king of spades
* player #B draws the cards

Q: Does the game go to the player #B? Or does the player #A plays the card again
because player #B already drawn 5 cards?

Turn order is:

#A -> #B -> #A -> #B

If the player #A plays king of spades it affects the previous player which is
the player #B, now if the player #B draws 5 cards, then the turn of the player
#A is finished, should the player #B now play a card?

In case of 3 player game this is much easier as:

#A -> #B -> #C -> #A -> #B -> #C -> #A

When player #A plays king of spades then the previous player (which is #C) draws
cards then game goes to player #B and allows him to play the card. It is the same
for games with more players.

- for the king of hearts we have a situation where active player plays it, next
player needs to draw, does the game move to him? I think yes!

sealed interface Capability

Playing a single card:

data class PlayCard(val card: Card): Capability

Playing a group of cards:

data class PlayCards(val groups: List<CardGroup>): Capability

- do we need to have a special PlayCard capability just for playing a single card?
  does not have special sense as we can have a Group consisting of a single card
  it is important if a player has several copies of a card then he can pick how
  many of them he plays and in what order

  C1, C2, C3, C4

  C1
  C2,
  C3,
  C4
  C1, C2
  C1, C3
  C1, C4
  ...
  C1, C2, C3, C4
  C1, C2, C4, C3
  C1, C3, C2, C4
  ...

  How should this be expressed as capabilities? If we would have a capability
  per card group then it will be a lot of capabilities for the client.

  If we split playing the card into picking and playing then we can make the
  player decide the order of cards. First pick would cause that the player has
  the option to pick another or drop a card. In case of a single card to play
  there would not be a pick option but only a play option available.

  The main issue now is how to proceed with a easy and convenient way to
  rearrange the card order?

  Idea would be to send also "Rearrange" capabilities beside the "Pick"
  capabilities after the first Pick was made.

  This creates an issue that there are a lot of capabilities not directly
  associated with the game itself but the UI and interaction with the game.
  Do we want such thing?

  In case of a player that has a hand of:
  - 10 of Diamonds
  - 10 of Spades
  - 10 of Clubs
  - Ace of Hearts
  - 5 of Clubs

  The current card that was played by the previous player is 6 of Clubs. Current
  player had several options:
  - to draw a card
  - to play a single 5 of Clubs (the same Suit)
  - to play a single 10 of Clubs (the same Suit)
  - to play a set of 10s:
    -- 10C, 10D
    -- 10C, 10S
    -- 10C, 10D, 10S
    -- 10C, 10S, 10D
  - to play the Ace of Hearts to change the current Suit

  The capabilities sent:
  - Forfeit <-- a player can always Forfeit a game
  - DrawCard <-- a player can decide to just draw a card instead of playing
  - PlayCard(5C) <-- plays the 5C because the suit matches
  - PlayCard(10C) <-- plays the 10C because the suit matches
  - SelectCard(10C) <-- because it is possible to put a set of cards of the same
    rank starting from 10C

  if the player decides to select the card then capabilities sent are:
  - Forfeit
  - DrawCard <-- immediately drops the selection and just allows to draw a card
  - SelectCard(10S)
  - SelectCard(10D)
  - PlayPickedCards <-- immediately allows to play what is picked, even if one
    card is picked
  - PlayCard(5C)
  - PlayCard(10C)

  current state SELECTED: 10C

  If the player decides to Select 10S then capabilities sent are:
  - Forfeit
  - DrawCard <-- immediately drops the selection and just allows to draw a card
  - SelectCard(10D)
  - DeselectCard(10C) <-- will cause the same as Deselecting all cards
  - DeselectCard(10S)
  - DeselectAllCards <-- to completely deselect all select cards
  - PlaySelectedCards
  - PlayCard(5C) <-- to allow playing cards that are normally available
  - PlayCard(10C)

  current state SELECTED: 10C, 10S

  If the player decides to Select 10D then capabilities sent are:
  - Forfeit
  - DrawCard
  - PlaySelectedCards
  - DeselectCard(10C)
  - DeselectCard(10S)
  - DeselectCard(10D)
  - DeselectAllCards
  - RearrangeSelectedCards(10C, 10D, 10S)

  current state SELECTED: 10C, 10S, 10D

  The above shows that we have several types of capabilities available:

  1) Overall:
  - Forfeit

  2) Gameplay related:
  - DrawCard
  - PlayCard
  - SkipTurn <-- a game could do it automatically but it is better for the
    player to understand what is happening

  3) UI related:
  - SelectCard
  - DeselectCard
  - DeselectAllCards
  - PlaySelectedCards
  - RearrangeSelectedCards

How to indicate which player should do something. Usually the capabilities
sent are related to the current active player. We would like to cover the case
of attacking the previous player with a King of Spades and the next player with
a King of Hearts.

Let's start with attacking the previous player. The previous player has options:
- draw 5 cards
- draw 1 card and check if there is a king of hearts to play it immediately to
  make a counter attack and if it is not there then draw the other 4 cards
- play a king of hearts from his hand

If the capabilities are always associated with the active player, how can we send
the capabilities for the previous player?

Add player information to the capabilities? This seems ok for:
- draw card capability
- play card capability

but does not have sense for other capabilities, do we want to pollute their
design with this additional info?

Create a distinction between ACTIVE player (which is more related to the TURN
order) and CURRENT player which is the player for which we sent capabilites.
Usually ACTIVE player and CURRENT player could be the same player but in these
rare occasions ACTIVE can be different then CURRENT.

this might solve the issue for the niche case of playing king of spades and a
king of hearts but creates a different way of handling turn to turn gameplay.


Rules:
1) 2s force next player to draw 2 cards or place another 2s
2) 3s force next player to draw 3 cards or place another 3s
4) joker can substitute a picked card (it can be decided before game which)
5) 4s force the next player to loose the turn or place another 4s
6) jack demands a rank (cannot demand functional cards: 2, 3, 4, jacks, kings)
   -- all players need abide (even the playing player)
   -- only playing another jack and demanding a different rank can stop it
   -- can play multiple jacks at once
7) ace demands a suit
8) king of spades then the previous player draws 5 cards
9) king of hearts then the next player draws 5 cards

First card that the game starts from does not affect the first player
- if it is a 2 or 3 the player can put a card with the same suit or another 2 or 3,
  but does not cause the first player to draw 2 or 3 cards
- if it is a 4 the player can put a card with the same suit or another 4
 */
}

