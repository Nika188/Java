package pgdp.collections;

public class DataStructureLink<T> {
    DataStructureConnector<T> a, b;

    public DataStructureLink(DataStructureConnector<T> a1, DataStructureConnector<T> b1) {
        a = a1;
        b = b1;
    }

    public boolean moveNextFromAToB(){
        if (!a.hasNextElement()){
            return false;
        }
        b.addElement(a.removeNextElement());
        return true;
    }
    public void moveAllFromAToB(){
        if (!a.hasNextElement()){
            return;
        }
        while (a.hasNextElement()){
            b.addElement(a.removeNextElement());
        }
    }
}
