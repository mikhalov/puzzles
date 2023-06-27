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

    public static <K, V> void swapValues(Map<K, V> map, K key1, K key2) {
        if (map.containsKey(key1) && map.containsKey(key2)) {
            V value1 = map.get(key1);
            V value2 = map.get(key2);
            map.put(key1, value2);
            map.put(key2, value1);
            log.debug("{} 1k, {} 2k swapped", key1, key2);
        } else {
            log.error("{} 1k, {} 2k", key1, key2, new IllegalArgumentException("Both keys must be present in the map"));
        }
    }

    public static <K, V> void swapValues(Map<K, V> map1, Map<K, V> map2, K key1, K key2) {
        if (map1.containsKey(key1) && map2.containsKey(key2)) {
            V value2 = map2.get(key2);
            map1.put(key1, value2);

            log.debug("Value of {} in map1 swapped with value of {} in map2", key1, key2);
        } else {
            log.error("Either {} does not exist in map1 or {} does not exist in map2", key1, key2, new IllegalArgumentException("Both keys must be present in their respective maps"));
        }
    }
}
