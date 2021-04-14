# 3. Handling capabilities for not the active player

Date: 2021-04-08

## Status

Accepted

## Problem

Most of the cards with special effects in the game affect the next player depending on their play. For example when playing the ranks 2, 3, 4 the main effect of the card is applied on the next player only if that player cannot play another 2, 3, 4 card. The problem occurs for the king of spades which forces the previous player to draw 5 cards. Such player can play a king of hearts to defend which will cause the player who played the king of spades to draw 10 cards instead. How to indicate which player should do something?

## Discussion

Let's imagine a 4 player game with players #A, #B, #C, #D. The order of the round is #A -> #B -> #C -> #D. The active player is player #B.

Case 1:

Player #B plays a king of spades (KS), now the game should return capabilities for the player #A. If player #A has a king of hearts (KH) he can defend with it or he can draw 5 cards. If player #A plays KH then player #B draws 10 cards.

Case 2:

Player #B plays a king of hearts (KH), now the game should return capabilities for the player #C. If player #C has a king of spades (KS) he can defend with it or he can draw 5 cards. If player #C plays KS then player #B draws 10 cards.

Usually the capabilities sent are related to the current active player. We would like to cover the case
of attacking the previous player with a King of Spades and the next player with a King of Hearts. Let's start with attacking the previous player. The previous player has options:
- draw 5 cards
- draw 1 card and check if there is a king of hearts to play it immediately to make a counter attack and if it is not there then draw the other 4 cards
- play a king of hearts from his hand

If the capabilities are always associated with the active player, how can we send the capabilities for the previous player?

### Solution 1

The most obvious solution would be to add target player information to the capabilities sent. This seems ok for any DrawCard and PlayCard capability but probably does not have sense for capabilities like EndTurn, CancePlay etc?

Sending the target player with each capability causes that the having "activePlayer" property in the Game object does not have much sense. We are especially interested in doing back and forth with previous player in the turn order.

Player #B starts his turn, he has a hand consisting of KS, 2S, 3S, AH. Capabilities the game returns:
- PlayCard(player=#B, card=KS)
- PlayCard(player=#B, card=2S)
- PlayCard(player=#B, card=3S)
- PlayCard(player=#B, card=AH)
- DrawCard(player=#B)
- ForfeitGame(player=#B)

When the player chooses the capability PlayCard(player=#B, card=KS) then the game should produce capabilities for Player #A although the next player that is in the turn order is Player #C. When the Game is moved to next Game state it should produce capabilities for Player #A. Player #A has a hand consisting of 4S, 7C. This mean that player #A is lacking a way to counter this attack and will need to draw a card. Capabilities the game returns:
- DrawCard(player=#A)
- ForfeitGame(player=#A)

Then the game should move forward to Player #C.

In case of Game class defined as:

class Game(
    val previous: Player,
    val active: Player,
    val next: Player,
    val players: List<Player>
)

This will create confusion as the application UI will have access to both the active player stored in the Game object and the current player stored in the capability. In such situation UI code would have to check if capability.player and game.player are the same if not then pick the capability.player as the active one. This creates contract that is not visible outside.

Also the problem is that despite sending the information about the target player we still need to keep the internal representation of the game inside the object, so it is possible to do counter plays and move the Player #C.

### Solution 2

Create a distinction between ACTIVE player (which is more related to the TURN order) and CURRENT player which is the player for which we sent capabilities. Usually the ACTIVE player and the CURRENT player should be the same player but in these rare occasions the ACTIVE can be different then the CURRENT.

This will create confusion as to which one is associated with the turn order and which is associated with capabilities. This confusion could be removed by introducing a strict distinction between Turn order and current game state capabilities.

Instead of the Game state defined as:

class Game(
    val previous: Player,
    val active: Player,
    val next: Player,
    val players: List<Player>
)

A deliberate information about the Turn can be specified:

class Turn(
    val previous: Player,
    val active: Player,
    val next: Player,
    val order: CyclicList<Player>
)

class Game(
    val current: Player,
    val turn: Turn
)

Its much more clear what certain information is about this way. A clear distinction between two domains begings to rise, there is a "turn domain" and a "play domain" making the game as a whole.

## Decision

Solution #2 will be used as it creates a distinction between certain concepts of the game.

## Consequences

A specific language begins to form that describes the game and exposes some conceptual domains. They will be named and won't be implict.