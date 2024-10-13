package pgdp.collections;

public class FishyProduct {
    private final String name;
    private final int price;


    public FishyProduct(String name1, int price1){
        if (price1<=0){
            ExceptionUtil.illegalArgument("penguins don’t give away anything, especially food");
        }
        if (name1==null ){
            ExceptionUtil.illegalArgument("penguins don’t give away anything, especially food");
        }
        name=name1;
        price=price1;
    }
    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "FishyProduct{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
