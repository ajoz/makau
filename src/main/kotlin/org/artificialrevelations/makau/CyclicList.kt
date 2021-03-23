package org.artificialrevelations.makau

import java.lang.IllegalArgumentException

/**
 * Represents an immutable [Collection] that can be treated as a cycle and
 * iterated indefinitely. It has a normal [List] at its core but allows getting
 * elements from it with the use of negative indices or indices exceeding the
 * size of the base list.
 *
 * Usage example:
 *
 * ```
 * val list = CyclicList(1, 2, 3)
 * println(list[0])  // prints: 1
 * println(list[1])  // prints: 2
 * println(list[-1]) // prints: 3
 * println(list[3])  // prints: 1
 * ```
 *
 * It is possible to generate [List]s based on the existing [CyclicList] through
 * a set of [CyclicList.toList] functions. It is possible to specify the view
 * as an [IntRange] for the generated [List].
 *
 * Usage example:
 *
 * ```
 * val list = CyclicList(1, 2, 3)
 * println(list.toList())      // prints: [1, 2, 3]
 * println(list.toList(-2..2)) // prints: [2, 3, 1, 2, 3]
 * ```
 *
 * It is possible to create a new [CyclicList] from the existing one by just
 * moving the elements by a specified amount. A dedicated [CyclicList.movedBy]
 * allows to pass an Integer (positive or negative) to receive a new instance
 * of [CyclicList] that has the elements moved.
 *
 * Usage example:
 *
 * ```
 * val list = CyclicList(1, 2, 3)
 * println(list.movedBy(0))   // prints: CyclicList(1, 2, 3)
 * println(list.movedBy(1))   // prints: CyclicList(3, 1, 2)
 * println(list.movedBy(-1))  // prints: CyclicList(2, 3, 1)
 * println(list.movedBy(3))   // prints: CyclicList(1, 2, 3)
 * ```
 *
 * @see Collection
 * @see Iterable
 * @see Iterator
 */
class CyclicList<A>(private val base: List<A>) : Collection<A> by base {
    /**
     * Creates a new instance of [CyclicList] from the specified elements.
     *
     * @param elements a finite set of elements that will be the base for the
     * cyclic list.
     */
    constructor(vararg elements: A) : this(listOf(*elements))

    // Query Operations

    /**
     * Returns an iterator over the elements of the cyclic list. A non empty
     * cyclic list will always indicate the existence of elements. After
     * reaching the last element of the base list, iterator will start returning
     * elements from the start.
     */
    override fun iterator(): Iterator<A> =
            object : Iterator<A> {
                var index = 0
                override fun hasNext() =
                        base.isNotEmpty()

                // TODO: do not increment index indefinitely, it is not needed!
                override fun next(): A =
                        get(index++)
            }

    // Positional Access Operations

    /**
     *
     */
    operator fun get(index: Int): A =
            if (base.isEmpty())
                throw IllegalArgumentException("CyclicList is empty!")
            else
                base[(index % base.size + base.size) % base.size]

    /**
     * Returns a new instance of [CyclicList] that has all the elements moved
     * by the specified amount (can be negative).
     *
     * @param number value by which the resulting CyclicList should have its
     * elements moved.
     * @return Instance of [CyclicList] with elements moved by the specified
     * amount.
     */
    infix fun movedBy(number: Int): CyclicList<A> =
            CyclicList(List(base.size) { index ->
                this[index - (number % base.size)]
            })

    /**
     *
     */
    fun toList(): List<A> =
            base

    /**
     *
     */
    fun toList(range: IntRange): List<A> =
            if (base.isEmpty())
                emptyList()
            else
                range.map { index -> get(index) }

    /**
     *
     */
    override fun toString(): String =
            base.joinToString(", ", "CyclicList(", ")") {
                if (it === this) "(this Collection)" else it.toString()
            }
}

fun main() {
    var i = 0
    println(i++ % 3)
    println(i++ % 3)
}