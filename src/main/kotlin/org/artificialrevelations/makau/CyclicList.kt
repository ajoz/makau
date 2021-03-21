package org.artificialrevelations.makau

interface CyclicList<A> : Iterable<A> {
    fun previousOf(index: Int): A

    fun nextOf(index: Int): A

    operator fun get(index: Int): A
}