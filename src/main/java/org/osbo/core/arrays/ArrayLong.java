package org.osbo.core.arrays;

import java.util.Arrays;

public  class ArrayLong {
    public static long[] getArrayLong(String arraystring) {
        String[] aux = arraystring.split(",");
        long[] arrayLong = Arrays.stream(aux)
                .mapToLong(Long::parseLong)
                .toArray();
        return arrayLong;
    }
}
