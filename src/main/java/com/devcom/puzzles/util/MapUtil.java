package com.devcom.puzzles.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MapUtil {
    public static <K, V> void shuffleMapValues(Map<K, V> map) {
        List<K> ks = map.keySet().stream().toList();

        List<V> values = new ArrayList<>(map.values());
        Collections.shuffle(values);

        Iterator<V> valuesIterator = values.iterator();

        map.clear();

        for (K key : ks) {
            map.put(key, valuesIterator.next());
        }
    }

}
