package org.artificialrevelations.makau

class CyclicListTests {
    /*
    #0, #1, #2
    [a,  b,  c]

    Property for the getter, the negative index value is equal to the size -
    positive index.

    get(-0) = get(0) <-- special case
    get(-1) = get(2)
    get(-2) = get(1)
    get(-3) = get(0)
    get(-4) = get(-1) <-- special case

    get(-index % size) = get(size - index)

    
     */
}