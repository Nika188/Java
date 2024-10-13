package pgdp.collections;

public class PenguinCustomer {
    private final String name;
    private int money;
    private Stack<FishyProduct> products;

    public PenguinCustomer(String name1,int money1){
        if (money1<0){
            ExceptionUtil.illegalArgument("penguins cannot have debts");
        }
        if (name1==null){
            ExceptionUtil.illegalArgument("penguins cannot have debts");
        }
        name=name1;
        money=money1;
        products = new LinkedStack<FishyProduct>();
    }
    public void addProductToBasket(FishyProduct x) {
        products.push(x);
    }
    public void placeAllProductsOnBand(Queue<FishyProduct> x){
        StackConnector<FishyProduct> a = new StackConnector<FishyProduct>(products);
        QueueConnector<FishyProduct> b = new QueueConnector<FishyProduct>(x);
        DataStructureLink<FishyProduct> c = new DataStructureLink<FishyProduct>(a, b);
        c.moveAllFromAToB();
    }
    public void takeAllProductsFromBand(Queue<FishyProduct> x){
        StackConnector<FishyProduct> a = new StackConnector<FishyProduct>(products);
        QueueConnector<FishyProduct> b = new QueueConnector<FishyProduct>(x);
        DataStructureLink<FishyProduct> c = new DataStructureLink<FishyProduct>(b, a);
        c.moveAllFromAToB();
    }
    public void pay(int x){
        if (x<0){
            ExceptionUtil.illegalArgument("penguins cannot have debts");
        }
        if(money-x<0){
            ExceptionUtil.illegalArgument("penguins cannot have debts");
        }
        money=money-x;
    }
    public void goToCheckout(PenguinSupermarket market) {
        market.getCheckoutWithSmallestQueue().getQueue().enqueue(this);
    }
    public String getName() {
        return name;
    }
    public int getMoney() {
        return money;
    }
    public Stack<FishyProduct> getProducts() {
        return products;
    }


    @Override
    public String toString() {
        return "PenguinCustomer {" +
                "name='" + name + '\'' +
                ", money=" + money +
                ", products=" + products +
                '}';
    }
}
