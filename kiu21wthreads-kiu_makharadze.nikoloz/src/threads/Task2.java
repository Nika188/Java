package threads;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Task2 {
    private static final int NUM_THREADS = 10;
    private static final int CHANNEL_CAPACITY = 100;
    private static final int POISON_PILL = -1;

    public static List<String> generate(final int from, final int to, final int count) {
        if (from < 0 || to < 0 || !isInRange(count, 0, to - from + 1)) throw new IllegalArgumentException();

        List<String> generated = new ArrayList<>(count);

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(CHANNEL_CAPACITY);

        Thread A = new Thread(() -> {
            Set<Integer> set = new HashSet<Integer>();
            while (set.size() < count){
                int n = from + (int) (Math.random() * (to - from + 1));
                if (set.add(n)){
                    try {
                        queue.put(n);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            for (int i = 0; i < NUM_THREADS; i++){
                try {
                    queue.put(POISON_PILL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread[] B = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++){
            B[i] = new Thread(() -> {
                while (true){
                    int n = 0;
                    try {
                        n = queue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (n == POISON_PILL)
                            break;

                        String res = String.format("%d, %s", n, KanjiLib.convert(n));
                        synchronized (generated) {
                            generated.add(res);
                        }
                }
            });
        }

        A.start();
        for (Thread thread : B){
            thread.start();
        }
        try {
            A.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Thread b : B){
            try {
                b.join();
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