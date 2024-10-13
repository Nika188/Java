package exam;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ListDictionary<K, V> implements Dictionary<K,V> {
    List<K> keys;
    List<V> values;

    public Option<V> get(K key){
        int j=0;
        while (j<keys.size())
        {
            if(keys.get(j).equals(key) ) return Option.some(values.get(j));
        j++;
        }
       return Option.none();





    }
    public V get(K key, V defaultValue) throws EmptyOptionException {
          if (this.get(key).get() == null) {
              return defaultValue;
          }
          return this.get(key).get();
    }
    public boolean containsKey(K key){
         for (K k: keys) {
             if(k.equals(key)){
                 return true;
             }
         }
         return false;
    }

    public boolean put(K key, V value){
        if (containsKey(key)){
            return false;
        }else{
           keys.add(key);
           values.add(value);
           return true;
        }

    }

    @Override
    public boolean update(K key, V value) throws EmptyOptionException {
       if (containsKey(key)){
           int j=0;
           while (j<keys.size()){
               if(keys.get(j).equals(key)){
                   values.set(j,value);
               }
           }
           return true;
       }else{
           return false;
       }
    }

    @Override
    public void clear(K key) {
        int j=0;
        while (j<keys.size()){
            if(keys.get(j).equals(key)){
                keys.remove(j);
                values.remove(j);
            }
        }
    }

    @Override
    public Stream<K> keyStream() {
        return keys.stream();
    }

    @Override
    public Stream<V> valueStream() {
        return values.stream();
    }

}
