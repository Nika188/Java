package threads;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class Task3 {
    private static final int NUM_THREADS = 10;
    private static final int CHANNEL_CAPACITY = 100;
    private static final int POISON_PILL = -1;
    private static final int MAX_WAIT_SEND_NUM = 100;
    private static final int MAX_WAIT_SEND_ET = 10;

    private Queue<String> generated = new ConcurrentLinkedQueue<>();


    private List<Thread> B = new ArrayList<>(NUM_THREADS);

    private Thread A;

    private BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(CHANNEL_CAPACITY);



    public List<String> get() throws InterruptedException {
        A.join();
        for (Thread thread : B) {
            thread.join();
        }
        return new ArrayList<>(generated);
    }

    public List<Thread> getThreads() {
        return B;
    }

    public void interrupt() {
        A.interrupt();
    }

    public Task3(final int from, final int to, final int count) {
        if (from < 0 || to < 0 || !isInRange(count, 0, to - from + 1)) throw new IllegalArgumentException();

        A = new Thread(() -> {
            Set<Integer> set = new HashSet<Integer>();
            while (set.size() < count) {
                int n = generateNum(from, to, set);
                if (set.add(n)) {
                    try {
                        if (!queue.offer(n, MAX_WAIT_SEND_NUM, TimeUnit.MILLISECONDS))
                            return;
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
            sendPoisonPill();
        });

        for (int i = 0; i < NUM_THREADS; i++) {
            B.add(new Thread(() -> {
                while (true){
                    try {
                        int n = queue.take();
                        if (n == POISON_PILL)
                            break;

                        String res = String.format("%d, %s", n, KanjiLib.convert(n));

                        synchronized (generated) {
                            generated.add(res);
                        }
                    } catch (InterruptedException e) {
                        sendPoisonPill();
                        break;
                    }
                }
            }));
        }

        A.start();

        for (Thread thread : B) {
            thread.start();
        }
    }

    private void sendPoisonPill() {
        for (int i = 0; i < NUM_THREADS; i++) {
            try {
                queue.offer(POISON_PILL, MAX_WAIT_SEND_ET, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {

            }
        }
    }

    private int generateNum(int from, int to, Set<Integer> sent) {

        int n;
        do {
            n = from + (int) (Math.random() * (to - from + 1));
        } while (sent.contains(n));
        return n;
    }



    private static boolean isInRange(int count, int from, int to) {
        return from <= count && count <= to;
    }
}