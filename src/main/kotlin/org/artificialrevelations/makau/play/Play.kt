package org.artificialrevelations.makau.play

import org.artificialrevelations.makau.capabilities.Capability
import org.artificialrevelations.makau.cards.Card
import org.artificialrevelations.makau.game.Player

interface Play {
    fun getActiveCard(): Card
    fun getCurrentPlayer(): Player
    fun getCapabilities(): List<Capability>
}

/*
Current played card impacts how the game should process the rules in terms of the
affected player and capabilities that are available for that player. Sometimes
the played card can impact the turn order and even change it completely.

Basic Rules:
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

Extra rules:
1) Playing a JOKER changes the turn order.

There is a relation between the played card and the rules of the game.
- For 2, 3, 4 the play needs to work in two ways:
  -- store information how many 2, 3, 4s were played up to this point
  -- if 4x 2 were played then the next player needs to draw 8 cards and skips
     his turn. The last played 2 stays as the top of the played cards, next
     player after the one that drawn the card does not need to draw 2 cards.
     There needs to be a distinction between unresolved and resolved card.
  -- the game should correctly move to the next player in the turn order.
- For King of Spades the play needs to:
  -- store information how many cards the player should draw if no counter play
     available, if a counter play was done, then it should store information
     about how many cards should be drawn
  -- temporarily change the turn order when determining which player is the
     current player, then if that player cannot do a counter play the turn should
     continue accordingly to the turn order
  -- turn order should proceed accordingly
- For King of Hearts the play needs to:
  -- store information how many cards the player should draw if no counter play
     available
  -- temporarily change the turn order
  -- turn order should proceed accordingly
- For Ace the play needs to:
  -- change the demanded suit
  -- the suit that was demanded needs to be visible only for a single turn
- For Jack the play needs to:
  -- change the demanded rank
  -- cannot demand 2, 3, 4, kings, jacks, aces
  -- the demanded card needs to be supplied by all players, ending on the player
     that played the JACK

It should be possible to save the game state on each stage the capabilities can
be generated.




getCounterPlay(): List<Card> - good idea or not?




First card that the game starts from does not affect the first player
- if it is a 2 or 3 the player can put a card with the same suit or another 2
  or 3 but does not cause the first player to draw 2 or 3 cards
- if it is a 4 the player can put a card with the same suit or another 4
 */