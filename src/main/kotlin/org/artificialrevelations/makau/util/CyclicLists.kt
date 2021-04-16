package org.artificialrevelations.makau.util

/**
 * Returns element at the specified index of the [CyclicList]. The index
 * can be negative or can exceed the size of cyclic list.
 *
 * @param index Position within the cyclic list for which the element is
 * supposed to be returned.
 * @return Element that is stored under the specified index or null if the
 * cyclic list is empty.
 */
fun <A> CyclicList<A>.getOrNull(index: Int): A? =
    if (isEmpty()) null else get(index)

/**
 * Returns element at the specified index of the [CyclicList] or runs the
 * specified function if the list is empty. The index can be negative or
 * can exceed the size of cyclic list.
 *
 * @param index Position within the cyclic list for which the element is
 * supposed to be returned.
 * @return Element that is stored under the specified index or value
 * returned by the specified function.
 */
inline fun <A> CyclicList<A>.getOrElse(
    index: Int,
    crossinline emptyValue: () -> A
): A =
    if (isEmpty()) emptyValue() else get(index)

/**
 * Returns a new instance of [CyclicList] that has all the elements moved
 * by the specified amount (can be negative).
 *
 * @param number value by which the resulting CyclicList should have its
 * elements moved.
 * @return Instance of [CyclicList] with elements moved by the specified
 * amount.
 */
infix fun <A> CyclicList<A>.movedBy(number: Int): CyclicList<A> =
    CyclicList(List(size) { index ->
        this[index - (number % size)]
    })

/**
 * Returns a [List] constructed from the values stored under indices
 * represented as the specified range. Resulting [List] can be larger or
 * smaller then the base underlying list.
 *
 * @return List created from the elements of the cyclic list.
 */
fun <A> CyclicList<A>.toList(range: IntRange): List<A> =
    if (isEmpty()) emptyList() else range.map { index -> get(index) }


/**
 * Returns a [List] containing the specified number of values. If the amount
 * is larger than the size of the base list, elements from the base list
 * will be repeated.
 *
 * @return List created from the elements of the cyclic list.
 */
fun <A> CyclicList<A>.toList(amount: Int): List<A> =
    toList(0 until amount)

/**
 * Returns a [CyclicList] containing all the elements of the original CyclicList
 * but in a reversed order.
 *
 * @return CyclicList created from the elements of the original CyclicList.
 */
fun <A> CyclicList<A>.reversed(): CyclicList<A> =
    CyclicList(toList().reversed())
