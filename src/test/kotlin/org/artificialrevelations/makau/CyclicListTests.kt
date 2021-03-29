package org.artificialrevelations.makau

import net.jqwik.api.*
import net.jqwik.api.Tuple.Tuple2
import net.jqwik.api.Tuple.Tuple3
import org.assertj.core.api.Assertions

typealias ListWithPositiveIndex = Tuple2<List<Int>, Int>
typealias ListWithOneMoveByValue = Tuple2<List<Int>, Int>
typealias ListWithTwoMoveByValues = Tuple3<List<Int>, Int, Int>

@Label("CyclicList property based tests")
class CyclicListTests {
    /*
    get(-4) = get(-1) <-- special case
    get(-index % size) = get(size - index)
    get(index % size) = get(index - size)

    // other:
    * list, list.size > 0, index where 0 < abs(index) < size -> CyclicList(list).get(index % size) == CyclicList(list).get(index - size)
    */

    @Group
    @Label("CyclicList toList")
    internal inner class ToList {
        @Property
        fun `will give the same List it was created from`(@ForAll list: List<Any>) {
            // encode/decode: list -> list == CyclicList(list).toList()
            Assertions.assertThat(list).isEqualTo(CyclicList(list).toList())
        }

        @Property
        fun `of base list size will give the same List it was created from`(@ForAll list: List<Any>) {
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
        fun `that is a sublist will be the same as a sublist of the List it was created from`(
                @ForAll("listWithPositiveIndex") tuple: ListWithPositiveIndex
        ) {
            // encode/decode: list, 0 <= index < list.size  -> list.subList(index, list.size) ==  CyclicList(list).toList(index until list.size)
            val list = tuple.get1()
            val index = tuple.get2()
            // subList is exclusive, normal x .. y range is inclusive, needs to use until for it
            Assertions.assertThat(list.subList(index, list.size)).isEqualTo(CyclicList(list).toList(index until list.size))
        }
    }

    @Group
    @Label("CyclicList movedBy")
    internal inner class MovedBy {
        @Provide
        fun listWithOneMoveByValue(): Arbitrary<ListWithOneMoveByValue> {
            val lists = Arbitraries.integers().list()
            val values = Arbitraries.integers().withoutEdgeCases()
            return Combinators.combine(lists, values).`as` { list, value ->
                Tuple.of(list, value)
            }
        }

        @Property
        fun `an index and then inverted index will result with the same CyclicList`(
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
        fun `value1 then value2 gives the same result as moving by value2 then value1`(
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
        fun `has the same size as the original CyclicList`(
                @ForAll("listWithOneMoveByValue") tuple: ListWithOneMoveByValue
        ) {
            // invariant: list, value -> CyclicList(list).size == CyclicList(list).movedBy(value).size
            val list = tuple.get1()
            val value = tuple.get2()

            Assertions.assertThat(CyclicList(list).size).isEqualTo(CyclicList(list).movedBy(value).size)
        }

        @Property
        fun `has the same elements as the original`(
                @ForAll("listWithOneMoveByValue") tuple: ListWithOneMoveByValue
        ) {
            // invariant: list, value -> CyclicList(list).size == CyclicList(list).movedBy(value).size
            val list = tuple.get1()
            val value = tuple.get2()

            val original = CyclicList(list)
            val moved = original.movedBy(value)
            //FIXME: Implement containsAll for cyclic list
            Assertions.assertThat(original.containsAll(moved.toList()) && moved.containsAll(original.toList()))
        }
    }

    @Group
    @Label("On an empty CyclicList")
    internal inner class EmptyCyclicList {
        @Property
        fun `any get returns an Exception`(
                @ForAll index: Int
        ) {
            // invariants: empty list, index -> list.get(index) == IllegalArgumentException
            Assertions
                    .assertThatExceptionOfType(IllegalArgumentException::class.java)
                    .isThrownBy {
                        CyclicList<Int>(listOf())[index]
                    }
                    .withMessageContaining("CyclicList is empty!")
        }

        @Property
        fun `any getOrNull returns a null value`(
                @ForAll index: Int
        ) {
            // invariants: empty list, index -> list.getOrNull(index) == null
            Assertions.assertThat(CyclicList<Int>(listOf()).getOrNull(index)).isNull()
        }

        @Property
        fun `any getOrElse returns a value generated by the specified function`(
                @ForAll index: Int,
                @ForAll default: () -> Int
        ) {
            // invariants: empty list, index, func -> list.getOrElse(index, func) == func()
            Assertions.assertThat(CyclicList<Int>(listOf()).getOrElse(index, default)).isEqualTo(default())
        }
    }

    @Group
    @Label("A new CyclicList")
    internal inner class NewCyclicList {
        @Property
        fun `has the same size as the base list`(
                @ForAll list: List<Any>
        ) {
            // invariant: list -> list.size == CyclicList(list).size
            Assertions.assertThat(list.size).isEqualTo(CyclicList(list).size)
        }

        @Property
        fun `has the same elements as the original list`(
                @ForAll list: List<Any>
        ) {
            // invariant: list  -> list.contains(CyclicList(list)) and CyclicList(list).contains(list)
            val cyclicList = CyclicList(list)
            //FIXME: Implement containsAll for cyclic list
            Assertions.assertThat(list.containsAll(cyclicList.toList()) && cyclicList.containsAll(list))
        }

        @Property
        fun `made from one element has only that element`(
                @ForAll element: Any
        ) {
            val list = CyclicList(element)
            Assertions.assertThat(list.size).isEqualTo(1)
            Assertions.assertThat(list).contains(element)
            Assertions.assertThat(list[0]).isEqualTo(element)
        }
    }
}