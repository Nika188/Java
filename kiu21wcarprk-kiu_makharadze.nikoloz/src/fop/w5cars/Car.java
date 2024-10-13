package fop.w5cars;

public class Car {
    private String brand;
    private LicensePlate licensePlate;
    private int chassisNumber;
    private static int count=0;




    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setLicensePlate(LicensePlate licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(int chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public static void setCount(int count) {
        Car.count = count;
    }

    public static int getCount() {
        return count;
    }

    public Car(String brand, LicensePlate licensePlate){
        this.brand =brand;
        this.licensePlate=licensePlate;
        this.chassisNumber= count++;
    }

    public LicensePlate getLicensePlate() {
        return licensePlate;
    }

    @Override
    public String toString() {
        return "Car " + chassisNumber + ": (Brand: " + brand + ", License Plate: "+licensePlate+")";
    }
    // TODO
}
