package com.devcom.puzzles.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MapUtil {
    public static <K, V> Map<K, V> shuffleMapValues(Map<K, V> originalMap, Map<K, V> shuffledMap) {
        List<V> values = new ArrayList<>(originalMap.values());

        Collections.shuffle(values);

        Iterator<V> valuesIterator = values.iterator();

        for (K key : originalMap.keySet()) {
            shuffledMap.put(key, valuesIterator.next());
        }

        return shuffledMap;
    }

}
