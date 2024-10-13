package pgdp.collections;

public class PenguinSupermarket {
    private Checkout[] checkouts;
    public PenguinSupermarket(int x) {
        if (x <= 0){
            ExceptionUtil.illegalArgument("must be greater than 0");
        }
        checkouts = new Checkout[x];
        for (int i = 0; i < checkouts.length; i++){
            checkouts[i] = new Checkout();
        }
    }
    public Checkout getCheckoutWithSmallestQueue(){
        Checkout s =checkouts[0];
        for (int i=0;i<checkouts.length;i++){
            if (s.queueLength()>checkouts[i].queueLength()){
                s=checkouts[i];
            }
        }
        return s;
    }
    public void closeCheckout(int index) {
        if (index >= checkouts.length  || index < 0){
            ExceptionUtil.illegalArgument("starting from 0");
        }
        if (checkouts.length  <= 1){
            ExceptionUtil.unsupportedOperation("Cannot close all checkouts");
        }


        Checkout closedCheckout = checkouts[index];

        Checkout[] newCheckouts = new Checkout[checkouts.length - 1];
        for (int i = 0, j = 0; i < checkouts.length; i++) {
            if (i == index)
                continue;
            newCheckouts[j] = checkouts[i];
            j++;
        }

        QueueConnector<PenguinCustomer> a = new QueueConnector<PenguinCustomer>(closedCheckout.getQueue());
        StackConnector<PenguinCustomer> b = new StackConnector<PenguinCustomer>(new LinkedStack<PenguinCustomer>());
        DataStructureLink<PenguinCustomer> c = new DataStructureLink<PenguinCustomer>(a, b);
        c.moveAllFromAToB();
        checkouts = newCheckouts;

        while (b.hasNextElement()) {
            PenguinCustomer customer= b.removeNextElement();
            customer.goToCheckout(this);
        }
    }
    public void serveCustomers() {
        for (Checkout checkout : checkouts)
            checkout.serveNextCustomer();
    }


    public Checkout[] getCheckouts() {
        return checkouts;
    }
}
