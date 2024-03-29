package org.artificialrevelations.makau.util

import java.lang.IllegalArgumentException

//TODO: API guardian

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
//TODO: Move to a separate library project and expose it with jitpack.io
data class CyclicList<A>(private val base: List<A>) : Collection<A> by base {
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
     *
     * @return Iterator that allows to go over the elements of the cyclic list.
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
     * Returns element at the specified index of the [CyclicList]. The index
     * can be negative or can exceed the size of cyclic list.
     *
     * @param index Position within the cyclic list for which the element is
     * supposed to be returned.
     * @return Element that is stored under the specified index.
     * @throws IllegalArgumentException If the [CyclicList] is empty.
     */
    operator fun get(index: Int): A =
        if (isEmpty())
            throw IllegalArgumentException("CyclicList is empty!")
        else
            base[(index % base.size + base.size) % base.size]

    /**
     * Returns the base [List] that is behind this [CyclicList]. This method is
     * useful when wanting to iterate through the contents of the [CyclicList]
     * with usual means like for loops or [List.forEach] calls without expose
     * oneself to potential infinite loops.
     *
     * @return Underlying base list.
     */
    fun toList(): List<A> =
            base

    /**
     * Returns a string representation of the [CyclicList]. This string will
     * contain comma separated string representations of contained elements.
     * If this Cyclic list contains references to itself then for each such
     * element a "(this Collection)" will be printed.
     *
     * @return string representation of the [CyclicList]
     */
    override fun toString(): String =
            base.joinToString(", ", "CyclicList(", ")") {
                if (it === this) "(this Collection)" else it.toString()
            }
}