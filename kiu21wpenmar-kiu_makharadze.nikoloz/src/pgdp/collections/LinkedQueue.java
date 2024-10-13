package pgdp.collections;

public class LinkedQueue<T> implements Queue<T> {
    private List<T> list;
    public LinkedQueue() {
        list = null;
    }

    public LinkedQueue(T x) {
        list = new List<T>(x);
    }
    @Override
    public int size() {
        if (list == null)
            return 0;
        return list.length();
    }
    @Override
    public void enqueue(T x) {
        if (list == null){
            list = new List<T>(x);
        }else{
            List<T> t = list;
            while (t.getNext() != null){
                t = t.getNext();
            }
            t.insert(x);
        }
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }
        if (list.length() == 1) {
            T r = list.getInfo();
            list = null;
            return r;
        }
        T r = list.getInfo();
        list = list.getNext();
        return r;

    }

    @Override
    public String toString() {
        return "LinkedQueue{" +
                "list=" + list +
                '}';
    }
}
