package exam;

import java.util.*;
import java.util.stream.Stream;

public class SetDictionary<K, V>  implements Dictionary<K,V>{
    Set<Pair<K,V>> entries = new HashSet<>();
    @Override
    public Option<V> get(K key) {
        if(!containsKey(key)){
            return Option.none();
        }
        V v = null;
        for (Pair<K,V> p: entries){
            if(p.first.equals(key)){
                v = p.second;
            }
        }
        return Option.some(v);
    }

    @Override
    public V get(K key, V defaultValue) throws EmptyOptionException {
        if (containsKey(key)){
            return this.get(key).get();
        }
        return defaultValue;
    }

    @Override
    public boolean containsKey(K key) {
        for (Pair<K,V> p: entries){
            if(p.first.equals(key)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean put(K key, V value) {
        if(containsKey(key)){
            return false;
        }else{
            entries.add(new Pair<K,V>(key,value));
            return true;
        }
    }

    @Override
    public boolean update(K key, V value) throws EmptyOptionException {
        if(!containsKey(key)){
            return false;
        }else{
            for (Pair<K,V> p:entries){
                if(p.first.equals(key)){
                    p.second = value;
                }
            }
        }
        return true;
    }

    @Override
    public void clear(K key) {
        for (Pair<K,V> p: entries){
            if(p.first.equals(key)) {
                entries.remove(p);
            }
        }
    }

    @Override
    public Stream<K> keyStream() {
        List<K> d= new LinkedList<>();
        for (Pair<K,V> c : entries){
            d.add(c.first);
        }
        return d.stream();
    }

    @Override
    public Stream<V> valueStream() {
        List<V> d= new LinkedList<>();
        for (Pair<K,V> c : entries){
            d.add(c.second);
        }
        return d.stream();
    }
}
