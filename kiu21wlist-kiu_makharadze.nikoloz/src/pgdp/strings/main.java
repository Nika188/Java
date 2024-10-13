package pgdp.strings;

public class main {
    public static void main(String[] args) {
        char[] c= {'a','e','c','d'};
        MyString str = new MyString(c);
        char[] c1={'e','f'};
        str.concat(c1);


        System.out.println(str.toString()); //
        char[] c3= {'a','e','c','e'};
        MyString str1= new MyString(c3);
        char[] c2={'e'};
        str1.concat(c2);
        char[] c4={'f'};
        str1.concat(c4);

        System.out.println(str.equals(str1));
        System.out.println(str.indexOf('e'));
        System.out.println(str.lastIndexOf('e'));
    }
}
