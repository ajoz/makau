package org.artificialrevelations.makau

import net.jqwik.api.*
import net.jqwik.api.Tuple.Tuple2
import org.assertj.core.api.Assertions

typealias ListWithPositiveIndex = Tuple2<List<Int>, Int>

class CyclicListTests {
    /*
    get(-0) = get(0) <-- special case
    get(-4) = get(-1) <-- special case
    get(-index % size) = get(size - index)
    get(index % size) = get(index - size)

    // encode/decode
    * list -> list == CyclicList(list).toList(0..list.size)
    * list, 0 <= index < list.size  -> list.subList(index, list.size) ==  CyclicList(list).toList(index..list.size)
    * list -> CyclicList(list) == CyclicList(cyclicList.toList())
    * list, value -> CyclicList(list) == CyclicList(list).movedBy(-value).movedBy(value)

    // different paths/same destination
    * list, value1, value2 -> CyclicList(list).movedBy(value1).movedBy(value2) == CyclicList(list).movedBy(value2).movedBy(value1)

    // invariants:
    * list, value -> CyclicList(list).size == CyclicList(list).movedBy(value).size
    * list -> CyclicList.contains(list) and list.contains(CyclicList.toList())

    // other:
    * empty list, index -> CyclicList(empty list).getOrNull(index) == null
    * empty list, index, default -> CyclicList(empty list).getOrElse(index) { default } == default
    * list, list.size > 0, index where 0 < abs(index) < size -> CyclicList(list).get(index % size) == CyclicList(list).get(index - size)
    */

    @Property
    fun `converting CyclicList to a List will give the same List it was created from`(@ForAll list: List<Any>) {
        // encode/decode: list -> list == CyclicList(list).toList()
        Assertions.assertThat(list).isEqualTo(CyclicList(list).toList())
    }

    @Property
    fun `converting CyclicList to List of base list size will give the same List it was created from`(@ForAll list: List<Any>) {
        // encode/decode: list -> list == CyclicList(list).toList(list.size)
        Assertions.assertThat(list).isEqualTo(CyclicList(list).toList(list.size))
    }

    @Property
    fun `converting CyclicList to a sublist will be the same as a sublist of the List it was created from`(
            @ForAll("listAndPositiveIndex") tuple: ListWithPositiveIndex
    ) {
        // encode/decode: list, 0 <= index < list.size  -> list.subList(index, list.size) ==  CyclicList(list).toList(index..list.size)
        val list = tuple.get1()
        val index = tuple.get2()
        Assertions.assertThat(list.subList(index, list.size)).isEqualTo(CyclicList(list).toList(index..list.size))
    }

    @Provide
    fun listAndPositiveIndex(): Arbitrary<ListWithPositiveIndex> =
            Arbitraries.integers().list().flatMap { list ->
                Arbitraries.integers().between(0, list.size).map { index ->
                    Tuple.of(list, index)
                }
            }
}