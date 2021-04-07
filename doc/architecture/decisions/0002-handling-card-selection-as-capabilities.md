# 2. Handling card selection as capabilities

Date: 2021-04-06

## Status

Accepted

## Problem

In the game of Makau a player can play several cards at once if they have a matching rank and the first card in the played set has a matching suit or rank as the current card. Should the list of capabilities contain all the possible combinations that are available to play?

## Discussion

Gameplay-wise only a PlayCard and DrawCard capability is needed, the problem is what should be done to allow player to play multiple cards. Makau allows to play several cards of the same rank at once. Do we need a specialized version of PlayCards capability just for this? If we would go this route then a PlayCard that is dedicated just for a single card does not have much sense as PlayCards capability can be sent with a single element instead.

The main issue would be the amount of possible capabilities that the game would generate for a given game state. Let's say
that the player has 4 of the same card: 5 of Hearts (5H), 5 of Spades (5S), 5 of Clubs (5C) and 5 of Diamonds (5D). The current card is 7 of Hearts (7H).

The active player has the option to:
- play a single 5H because the suits match, he cannot plays 5S, 5C or 5D on 7H -- 1 option
- play 5H, 5S or 5H, 5D or 5H, 5C -- 3 options
- play 5H, 5S, 5D or 5H, 5D, 5S or 5H, 5S, 5C or 5H, 5C, 5S or 5H, 5D, 5C or 5H, 5C, 5D -- 6 options
- play 4 cards starting from 5H -- 6 options

This gives 1 + 3 + 6 + 6 which is 16 possible capabilities sent not counting ForfeitGame, DrawCard capabilities or capabilities for playing other cards that the player might have.

Questions here are:
1) Is returning 20+ capabilities too much?
2) How will the game UI handle this?

### Solution 1
  
If we split playing the card into selecting and playing then we can make the player decide the order of cards which in turn would lower the amount of capabilities sent. First select would cause that the player has the option to select another or drop a card. In case of a single card to play there would not be a select option but only a play option available.

The main issue now is how to proceed with an easy and convenient way to rearrange the card order?

Idea would be to send also "Rearrange" capabilities beside the "Select" capabilities after the first "Select" was made.

This creates an issue that there are a lot of capabilities not directly associated with the game itself but the UI and interaction with the game. Do we want such thing?

In case of a player that has a hand of:
- 10 of Diamonds
- 10 of Spades
- 10 of Clubs
- Ace of Hearts
- 5 of Clubs

The current card that was played by the previous player is 6 of Clubs. Current player had several options:
- to draw a card
- to play a single 5 of Clubs (the same Suit)
- to play a single 10 of Clubs (the same Suit)
- to play a set of 10s:
  - 10C, 10D
  - 10C, 10S
  - 10C, 10D, 10S
  - 10C, 10S, 10D
- to play the Ace of Hearts to change the current Suit

The capabilities sent:
- Forfeit <-- a player can always Forfeit a game
- DrawCard <-- a player can decide to just draw a card instead of playing
- PlayCard(5C) <-- plays the 5C because the suit matches
- PlayCard(10C) <-- plays the 10C because the suit matches
- SelectCard(10C) <-- because it is possible to put a set of cards of the same rank starting from 10C

If the player decides to select the card then capabilities sent are:
- Forfeit
- DrawCard <-- immediately drops the selection and just allows to draw a card
- SelectCard(10S)
- SelectCard(10D)
- PlaySelectedCards <-- immediately allows to play what is picked, even if one card is picked
- PlayCard(5C)
- PlayCard(10C)

Current state SELECTED: 10C

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

Current state SELECTED: 10C, 10S

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
- SkipTurn <-- a game could do it automatically but it is better for the player to understand what is happening

3) UI related:
- SelectCard
- DeselectCard
- DeselectAllCards
- PlaySelectedCards
- RearrangeSelectedCards

### Solution 2

The first solution is adding a lot of new capabilities that are related purely with UI and handling some small aspect of the raw game. Let's return  to the example situation, player has the specified hand of cards:
- 10 of Diamonds
- 10 of Spades
- 10 of Clubs
- Ace of Hearts
- 5 of Clubs

The current card that was played by the previous player is 6 of Clubs. The initial capabilities sent are:
- ForfeitGame
- DrawCard
- PlayCard(5C)
- PlayCard(10C)

If the player would decide to use the PlayCard(5C) capability then he would receive in response:
- ForfeitGame
- CancelPlay
- EndTurn

If the player would decide to use the PlayCard(10C) capability then he would receive in response:
- ForfeitGame
- CancelPlay
- EndTurn
- PlayCard(10D)
- PlayCard(10S)
- PlayCard(10H)

If the player would decide to use the PlayCard(10S) capability then he would receive in response:
- ForfeitGame
- CancelPlay
- EndTurn
- PlayCard(10D)
- PlayCard(10H)

Each time the amount of available capabilities will decrease. To finally finish the turn the player needs to confirm everything with using EndTurn capability. Technically it is possible to not add any CancelPlay capability that would allow the player to rearrange the played cards again. Comparing to the solution 1 everything is much simplified and the card selection is implicit and hidden, it does not leak outside in the form of the dedicated capabilities.

This makes the strain on the UI code lower as it does not need to scan the capabilities and correctly assign it to what a player can do.

### Solution 3

Instead of a multiple different PlayCard capabilities introduce a single PlayCards that accepts a list/array of cards to play. Then send all the valid permutations without repetition of cards that can be played. This would cause the UI code to be much more complicated as the list of "play" capabilities can be very large and the UI would need to distinguish when to show certain capabilities.

## Decision

Solution 2 will be used and a small number of capabilities will be sent.

## Consequences

None
