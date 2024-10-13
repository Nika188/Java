package pgdp.collections;

public class LinkedStack<T> implements Stack<T>{
    private List<T> list;

    public LinkedStack() {
        list = null;
    }

    public LinkedStack(T x) {
        list = new List<T>(x);
    }

    @Override
    public int size() {
        if (list == null)
            return 0;
        return list.length();
    }

    @Override
    public void push(T x) {
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
    public T pop() {
        if (isEmpty()) {
            return null;
        }
        if (list.length() == 1) {
            T r = list.getInfo();
            list = null;
            return r;
        }

        List<T> t = list;
        while (t.getNext().getNext() != null){
            t = t.getNext();
        }
        T r = t.getNext().getInfo();
        t.delete();
        return r;
    }

    @Override
    public String toString() {
        return "LinkedStack{" +
                "list=" + list +
                '}';
    }
}
