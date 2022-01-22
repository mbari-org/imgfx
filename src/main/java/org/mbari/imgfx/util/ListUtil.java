package org.mbari.imgfx.util;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ListUtil {

    private ListUtil() {}

    public static <T> Set<T> intersection(List<T> a, List<T> b) {
        return a.stream()
                .distinct()
                .filter(b::contains)
                .collect(Collectors.toSet());
    }
}
