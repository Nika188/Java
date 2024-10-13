package pgdp.collections;

public class Checkout {
    private Queue<PenguinCustomer> queue;
    private Queue<FishyProduct> bandBeforeCashier;
    private Queue<FishyProduct> bandAfterCashier;
    public Checkout() {
        queue = new LinkedQueue<PenguinCustomer>();
        bandBeforeCashier = new LinkedQueue<FishyProduct>();
        bandAfterCashier = new LinkedQueue<FishyProduct>();
    }

    public Queue<PenguinCustomer> getQueue() {
        return queue;
    }

    public Queue<FishyProduct> getBandBeforeCashier() {
        return bandBeforeCashier;
    }

    public Queue<FishyProduct> getBandAfterCashier() {
        return bandAfterCashier;
    }
    public int queueLength(){
        return queue.size();
    }
    public void serveNextCustomer() {
        PenguinCustomer customer = queue.dequeue();
        if (customer==null){
            return;
        }
        customer.placeAllProductsOnBand(bandBeforeCashier);
        int bill=0;
        while (bandBeforeCashier.size()>0) {
            FishyProduct product = bandBeforeCashier.dequeue();
            bill=bill+product.getPrice();
            bandAfterCashier.enqueue(product);
        }
        customer.takeAllProductsFromBand(bandAfterCashier);
        customer.pay(bill);

    }

    @Override
    public String toString() {
        return "Checkout{" +
                "queue=" + queue +
                ", bandBeforeCashier=" + bandBeforeCashier +
                ", bandAfterCashier=" + bandAfterCashier +
                '}';
    }
}
