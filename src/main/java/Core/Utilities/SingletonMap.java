package Core.Utilities;

import java.util.*;

public class SingletonMap<K, V> implements Map<K, V> {
    public final AbstractMap.SimpleEntry<K, V> entry;

    public static <K, V> SingletonMap<K, V> of(K k, V v) {
        return new SingletonMap<>(k, v);
    }

    private SingletonMap(K k, V v) {
        this.entry = new AbstractMap.SimpleEntry<>(k, v);
    }

    public String toString() {
        return "(" + this.entry.getKey() + ", " + this.entry.getValue() + ")";
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object o) {
        return this.entry.getKey().equals(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return this.entry.getValue().equals(o);
    }

    @Override
    public V get(Object o) {
        if (this.entry.getKey().equals(o)) {
            return this.entry.getValue();
        } else {
            return null;
        }
    }

    @Override
    public V put(K k, V v) {
        if (this.entry.getKey().equals(k)) {
            return this.entry.setValue(v);
        } else {
            return null;
        }
    }

    @Override
    public V remove(Object o) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
    }

    @Override
    public void clear() {
    }

    @Override
    public Set<K> keySet() {
        return Set.of(this.entry.getKey());
    }

    @Override
    public Collection<V> values() {
        return Set.of(this.entry.getValue());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Set.of(this.entry);
    }
}
