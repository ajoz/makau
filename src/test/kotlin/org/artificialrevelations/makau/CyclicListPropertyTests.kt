package org.artificialrevelations.makau

import net.jqwik.api.*
import net.jqwik.api.Tuple.Tuple2
import net.jqwik.api.Tuple.Tuple3
import org.assertj.core.api.Assertions

typealias ListWithPositiveIndex = Tuple2<List<Int>, Int>
typealias ListWithOneMoveByValue = Tuple2<List<Int>, Int>
typealias ListWithTwoMoveByValues = Tuple3<List<Int>, Int, Int>

class CyclicListPropertyTests {
    /*
    get(-0) = get(0) <-- special case
    get(-4) = get(-1) <-- special case
    get(-index % size) = get(size - index)
    get(index % size) = get(index - size)

    // encode/decode
    * list -> list == CyclicList(list).toList(0..list.size)

    // invariants:
    *
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

    @Provide
    fun listWithPositiveIndex(): Arbitrary<ListWithPositiveIndex> =
            Arbitraries.integers().list().flatMap { list ->
                Arbitraries.integers().between(0, list.size).map { index ->
                    Tuple.of(list, index)
                }
            }

    @Property
    fun `converting CyclicList to a sublist will be the same as a sublist of the List it was created from`(
            @ForAll("listWithPositiveIndex") tuple: ListWithPositiveIndex
    ) {
        // encode/decode: list, 0 <= index < list.size  -> list.subList(index, list.size) ==  CyclicList(list).toList(index until list.size)
        val list = tuple.get1()
        val index = tuple.get2()
        // subList is exclusive, normal x .. y range is inclusive, needs to use until for it
        Assertions.assertThat(list.subList(index, list.size)).isEqualTo(CyclicList(list).toList(index until list.size))
    }

    @Provide
    fun listWithOneMoveByValue(): Arbitrary<ListWithOneMoveByValue> {
        val lists = Arbitraries.integers().list()
        val values = Arbitraries.integers().withoutEdgeCases()
        return Combinators.combine(lists, values).`as` { list, value ->
            Tuple.of(list, value)
        }
    }

    @Property
    fun `moving a CyclicList by and index and then inverted index will result with the same CyclicList`(
            @ForAll("listWithOneMoveByValue") tuple: ListWithOneMoveByValue
    ) {
        // list, value -> CyclicList(list) == CyclicList(list).movedBy(-value).movedBy(value)
        val list = tuple.get1()
        val value = tuple.get2()

        Assertions.assertThat(CyclicList(list)).isEqualTo(CyclicList(list).movedBy(value).movedBy(-value))
    }

    @Provide
    fun listWithTwoMoveByValues(): Arbitrary<ListWithTwoMoveByValues> {
        val lists = Arbitraries.integers().list()
        val moveBy1s = Arbitraries.integers().withoutEdgeCases()
        val moveBy2s = Arbitraries.integers().withoutEdgeCases()
        return Combinators.combine(lists, moveBy1s, moveBy2s).`as` { list, moveBy1, moveBy2 ->
            Tuple.of(list, moveBy1, moveBy2)
        }
    }

    @Property
    fun `moving a CyclicList by value1 then value2 gives the same result as moving by value2 then value1`(
            @ForAll("listWithTwoMoveByValues") tuple: ListWithTwoMoveByValues
    ) {
        // different paths but same destination:
        // list, value1, value2 -> CyclicList(list).movedBy(value1).movedBy(value2) == CyclicList(list).movedBy(value2).movedBy(value1)
        val list = tuple.get1()
        val first = tuple.get2()
        val second = tuple.get3()

        val movedByFirstThenSecond = CyclicList(list).movedBy(first).movedBy(second)
        val movedBySecondThenFirst = CyclicList(list).movedBy(second).movedBy(first)

        Assertions.assertThat(movedByFirstThenSecond).isEqualTo(movedBySecondThenFirst)
    }

    @Property
    fun `moved CyclicList has the same size as the original`(
            @ForAll("listWithOneMoveByValue") tuple: ListWithOneMoveByValue
    ) {
        // invariant: list, value -> CyclicList(list).size == CyclicList(list).movedBy(value).size
        val list = tuple.get1()
        val value = tuple.get2()

        Assertions.assertThat(CyclicList(list).size).isEqualTo(CyclicList(list).movedBy(value).size)
    }
}