package org.artificialrevelations.makau.turn

import org.artificialrevelations.makau.game.Player
import org.artificialrevelations.makau.util.CyclicList

private typealias Players = CyclicList<Player>

//TODO: remove the suppress after this is used, anoys me
@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter", "unused")
class TurnOrder(
    val players: Players
) {
    val previous: Player = players[-1]
    val active: Player = players[0]
    val next: Player = players[1]
}