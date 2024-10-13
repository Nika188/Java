package pgdp.collections;

public class StackConnector<T> implements DataStructureConnector<T> {
    private final Stack<T> stack;

    public StackConnector(Stack<T> s) {
        stack = s;
    }

    @Override
    public boolean hasNextElement() {
        return !stack.isEmpty();
    }

    @Override
    public void addElement(T x) {
        stack.push(x);
    }

    @Override
    public T removeNextElement() {
        if (!hasNextElement())
            return null;
        return stack.pop();
    }

}
