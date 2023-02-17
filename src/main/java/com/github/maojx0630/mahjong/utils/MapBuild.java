package com.github.maojx0630.mahjong.utils;

import java.util.HashMap;
import java.util.function.Function;

/**
 * 用于快速构建 java.util.HashMap
 *
 * @author MaoJiaXing
 * @since 2020-12-29 11:41
 */
public class MapBuild<K, V> {

  private HashMap<K, V> map;

  private MapBuild() {
    map = new HashMap<>();
  }

  public static <K, V> MapBuild<K, V> createKv() {
    return new MapBuild<>();
  }

  public static <V> MapBuild<String, V> createStr() {
    return new MapBuild<>();
  }

  public static MapBuild<String, Object> create() {
    return new MapBuild<>();
  }

  public static <K, V> MapBuild<K, V> create(Iterable<V> iterable, Function<V, K> function) {
    MapBuild<K, V> objectObjectMapBuild = new MapBuild<>();
    iterable.forEach(v -> objectObjectMapBuild.put(function.apply(v), v));
    return objectObjectMapBuild;
  }

  public HashMap<K, V> build() {
    return map;
  }

  public MapBuild<K, V> put(K k, V v) {
    map.put(k, v);
    return this;
  }

  public MapBuild<K, V> remove(K k) {
    map.remove(k);
    return this;
  }
}
