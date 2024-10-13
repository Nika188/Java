package exam;

public class Option<T> {
    T x;
    private Option(){
        this.x= null;
    }
    private Option(T x){
        this.x=x;
    }
    public static <T> Option<T>  some(T x) {
        return new Option<T>(x);
    }
    public static <T> Option<T> none(){
        return new Option<T>();
    }
    public T get() throws EmptyOptionException{
        if (x==null){
            throw new EmptyOptionException();
        }
        return x;
    }
    T getOrDefault() {
        if (x==null){
            return null;
        }
        return x;
    }
    boolean isNone(){
        if (x==null){
            return true;
        }
        return false;
    }


}
