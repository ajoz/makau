# 4. How kings should be handled in a two player game

Date: 2021-04-14

## Status

Accepted

## Problem

In the most common version of the game all cards that have an effect affect the next player in turn order. This is not the case for the King of Spades (KS) that causes the previous player to draw 5 cards. How should King of Spades behave in case of a two player game, where the attacked player is both previous and next player in the turn order?

## Discussion

Let's imagine a situation where we have a two player game. Player #A and player #B. The turn order can be visualized as #A -> #B -> #A -> #B -> ...

This is player #B turn and he plays the King of Spades. KS affects the previous player which is player #A in case where player #A does not have a King of Hearts (KS) to defend he will be affected by the play and will have to draw 5 cards.

Player #B performed a successful attack and player #A drew cards. Technically the turn order should move to the player #A as the turn of player #B has ended. Should player #A proceed with his turn or should player #B make another move?

### Solution 1

We could treat the turn order indepently from this particular play. This means that we should use the same rules for the KS as in 3 or 4 player game. 

In case of a 3 player game the turn order would be: #A -> #B -> #C -> #A -> #B -> #C -> ... After playing KS player #B would finish his turn and player #C turn would proceed as normal. After player #C turn player #A who drew the 5 cards does not have a penalty of loosing his chance to play. Why would he need to have this penalty in a 2 player game?

### Solution 2

We could treat two player game as a special case. Technically a card affecting the direction in which the played card affects players does not have real meaning. Each play affects technically the next player, so we could treat KS as a card that affects the next player, the counter play would also affect the next player. The play and turn order handling would be greatly simplified. The problem is that there would be a change of behaviour in a 3 and 4 player game.

## Decision

Picked solution #1 and allow for cards to affect a previous player. In case of a two player game this should not cause that player to skip his turn.

## Consequences

None