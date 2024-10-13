package pgdp.collections;

public class QueueConnector <T> implements DataStructureConnector<T>{
    private final Queue<T> Queue;

    public QueueConnector(Queue<T> q) {
        Queue = q;
    }
    @Override
    public boolean hasNextElement() {
        return !Queue.isEmpty();
    }

    @Override
    public void addElement(T x) {
        Queue.enqueue(x);
    }

    @Override
    public T removeNextElement() {
        if (!hasNextElement())
            return null;
        return Queue.dequeue();
    }

}
