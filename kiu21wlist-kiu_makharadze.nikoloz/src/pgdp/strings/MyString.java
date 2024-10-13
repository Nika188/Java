package pgdp.strings;
public class MyString{
    private char [] data;
    private MyString next;
    public MyString (char [] data){
        this.data=data;
    }
    public int length (){
        MyString t =this;
        int i=0;
        while (t!=null){
            i=i+t.data.length;
            t = t.next;
        }

        return i;
    }
    public void concat (char [] data){

        MyString data1=this;
        while (data1.next!=null){
            data1=data1.next;
        }
        data1.next =new MyString(data);


    }
    public String toString (){
        MyString t =this;
//        int i=0;
//        while (t!=null){
//            i=i+t.data.length;
//            t = t.next;
//        }
        char[] a=new char[t.length()];
        MyString t1 =this;
        int j=0;
        int k=0;
        while(t1!=null){
            a[j]=t1.data[k];
            if (k==t1.data.length-1){
                t1 = t1.next;
                k=-1;
            }
            k++;
            j++;
        }

        return new String(a);


    }
    public boolean equals (MyString other){
        MyString t =this;
        MyString t1 =other;
        if (t.length()!=t1.length()){
            return false;
        }
//
////        for(int i =0; i < t.data.length; ++i) {
////            if(t.data[i]!=t1.data[i]){
////                return false;
////            }
////            if (i==data.length-1){
////                i=-1;
////                t = t.next;
////                t1 = t1.next;
////            }
////        }
////        int j=0;
////        int k=0;
//        while(t!=null){
//            if (t1==null){
//                return false;
//            }
//            if (t.data.equals(t1.data)){
//                t=t.next;
//                t1=t1.next;
//            }else{
//                return false;
//            }
//
////            k++;
////            j++;
//        }
//        if (t1!=null){
//            return false;
//        }

        int k=0;
        int j=0;
        for (int i=0;i<t.length();i++){
            if (t.data[k]!=t1.data[j]){
                return false;
            }
            if (k==t.data.length-1){
                t=t.next;
                k=-1;
            }
            if (j==t1.data.length-1){
                t1=t1.next;
                j=-1;
            }
            k++;
            j++;
        }
        return true;



       // return t.toString().equals(t1.toString());
    }
    public int indexOf (char c){
        MyString t =this;
        int k=0;
        for(int i =0; i < t.data.length; ++i) {
            if(t.data[i]==c){
                return k;
            }
            if (i==data.length-1){
                t = t.next;
                i=-1;

            }
            k++;
        }
        return -1;
    }
    public int lastIndexOf (char c){
        MyString t =this;
        int a=-1;
        int k=0;
        for(int i =0; i < t.data.length; ++i) {
            if(t.data[i]==c){
                a=k;
            }
            if (i==data.length-1){
                t = t.next;
                i=-1;
            }
            k++;
        }
        return a;
    }

}