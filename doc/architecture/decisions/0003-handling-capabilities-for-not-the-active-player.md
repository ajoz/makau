# 3. Handling capabilities for not the active player

Date: 2021-04-08

## Status

Accepted

## Problem

Most of the cards with special effects in the game affect the next player depending on their play. For example when playing the ranks 2, 3, 4 the main effect of the card is applied on the next player only if that player cannot play another 2, 3, 4 card. The problem occurs for the king of spades which forces the previous player to draw 5 cards. Such player can play a king of hearts to defend which will cause the player who played the king of spades to draw 10 cards instead.

Let's imagine a 4 player game with players #A, #B, #C, #D. The order of the round is #A -> #B -> #C -> #D. The active player is player #B.

We have a situation where player #B plays a king of spades (KS), now the game should return capabilities for the player #A. If player #A has a king of hearts (KH) he can defend with it or he can draw 5 cards.


## Discussion

The change that we're proposing or have agreed to implement.

## Consequences

What becomes easier or more difficult to do and any risks introduced by the change that will need to be mitigated.
