package org.artificialrevelations.makau.game

/*
Topics:

- ForfeitGame capability sent always for every player? or sent only for the
active player? Should non active players need to wait for their turn to forfeit
the game?

- How to store information about the current played card state and previously
played cards? This has the most use for 2, 3, 4s because of cumulative effect
on the next player. This has use for Jack which demands a specific card. In case
of playing Jack and demanding a card, the active player who demanded the card
is supposed to play that card last. Each player needs to play the demanded card,
draw a card or play a Jack and demand a different card.

- Going from Game state to Game state through capabilities
getCapabilities :: Game -> [Capability]
run  :: Capability -> Game
 */
fun next(game: Game): Game {
    // for the sake of compilation
    return game
}