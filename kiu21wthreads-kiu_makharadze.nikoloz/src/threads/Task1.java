package threads;

import java.util.ArrayList;
import java.util.List;

public class Task1 {
    private static final int NUM_THREADS = 10;

    public static List<String> generate(final int from, final int to, final int count)  {
        if (from < 0 || to < 0 || !isInRange(count, 0, to - from + 1)) throw new IllegalArgumentException();

        List<String> generated = new ArrayList<>(count);

        Thread[] threads = new Thread[NUM_THREADS];
        for(int i=0;i<NUM_THREADS;i++){
            threads[i] = new Thread(() -> {
                while (true){
                    int n = (int) (from + (Math.random() * (to - from + 1)));

                    String res = String.format("%d, %s", n, KanjiLib.convert(n));

                    synchronized (generated){
                        if (generated.size() >= count){
                            break;
                        }
                        if (!generated.contains(res)){
                            generated.add(res);
                        }
                    }
                }
            });
        }
        for (Thread thread : threads){
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return generated;
    }

    private static boolean isInRange(int count, int from, int to) {
        return from <= count && count <= to;
    }
}