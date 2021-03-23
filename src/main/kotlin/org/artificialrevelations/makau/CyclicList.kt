package org.artificialrevelations.makau

import java.lang.IllegalArgumentException

/**
 *
 */
class CyclicList<A>(private val base: List<A>) : Collection<A> by base, RandomAccess {

    /**
     *
     */
    constructor(vararg elements: A) : this(listOf(*elements))

    // Query Operations

    /**
     *
     */
    override fun iterator(): Iterator<A> =
            object : Iterator<A> {
                var index = 0
                override fun hasNext() =
                        base.isNotEmpty()

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
     *
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
//    val cyclic5 = CyclicList(1, 2, 3, 4, 5, 6)
//    println(cyclic5.toList(-1..1))
    println(CyclicList<Int>().toList(-100..100))
}

fun <A> CyclicList<A>.testMovedBy(number: Int) =
        println("$this moved by $number is ${movedBy(number)}")

