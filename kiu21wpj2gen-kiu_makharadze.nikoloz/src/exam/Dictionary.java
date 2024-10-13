package exam;

import java.util.stream.Stream;

public interface Dictionary<K, V> {
    public Option<V> get(K key);
    public V get(K key, V defaultValue) throws EmptyOptionException;
    public boolean containsKey(K key);

    public boolean put(K key, V value);
    public boolean update(K key, V value) throws EmptyOptionException;

    public void clear(K key);

    public Stream<K> keyStream();
    public Stream<V> valueStream();
}
