package org.artificialrevelations.makau

import net.jqwik.api.ForAll
import net.jqwik.api.Property
import org.junit.Assert

class CyclicListTests {
    /*
    #0, #1, #2
    [a,  b,  c]

    Property for the getter, the negative index value is equal to the size -
    positive index. the positive index value is equal to the index - size.

    get(-0) = get(0) <-- special case
    get(-1) = get(2)
    get(-2) = get(1)
    get(-3) = get(0)
    get(-4) = get(-1) <-- special case

    get(-index % size) = get(size - index)
    get(index % size) = get(index - size)


    - Encode/Decode
    - Invariants

    // encode/decode
    * list -> list == CyclicList(list).toList()
    * list -> list == CyclicList(list).toList(list.size)
    * list -> list == CyclicList(list).toList(0..list.size)
    * list, 0 <= index < list.size  -> list.subList(index, list.size) ==  CyclicList(list).toList(index..list.size)
    * list -> CyclicList(list) == CyclicList(cyclicList.toList())
    * list, value -> CyclicList(list) == CyclicList(list).movedBy(-value).movedBy(value)
    *

    // different paths/same destination
    * list, value1, value2 -> CyclicList(list).movedBy(value1).movedBy(value2) == CyclicList(list).movedBy(value2).movedBy(value1)
    *

    // invariants:
    * list, value -> CyclicList(list).size == CyclicList(list).movedBy(value).size
    * list -> CyclicList.contains(list) and list.contains(CyclicList.toList())
    *

    // other:
    * empty list, index -> CyclicList(empty list).getOrNull(index) == null
    * empty list, index, default -> CyclicList(empty list).getOrElse(index) { default } == default
    * list, list.size > 0, index where 0 < abs(index) < size -> CyclicList(list).get(index % size) == CyclicList(list).get(index - size)


     */

    @Property
    fun test(@ForAll list: List<Any>) {
        Assert.assertEquals(list, list)
    }
}