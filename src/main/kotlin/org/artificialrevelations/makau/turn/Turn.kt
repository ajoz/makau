package org.artificialrevelations.makau.turn

import org.artificialrevelations.makau.game.Player
import org.artificialrevelations.makau.util.CyclicList

private typealias Players = CyclicList<Player>

//TODO: remove the suppress after this is used, anoys me
@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter", "unused")
class Turn(
    val order: Players
) {
    val previous: Player = order[-1]
    val active: Player = order[0]
    val next: Player = order[1]
}