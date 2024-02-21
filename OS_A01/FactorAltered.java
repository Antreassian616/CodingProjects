
import java.util.concurrent.TimeUnit;

public class FactorAltered implements Runnable {
    private int begin;
    private int end;
    private long start_s;

    public FactorAltered(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    public static void main(String[] args) throws InterruptedException {
        int numChild = 4;
        int total = 100;
        int range = total / numChild;
        int begin = 5999901;

        System.err.println("Run Factor " + total + ":" + numChild);

        // Create child threads
        Thread[] threads = new Thread[numChild];
        for (int i = 0; i < numChild; i++) {
            threads[i] = new Thread(new Factor(begin, begin + range));
            threads[i].start();
            System.err.println("Child " + (i + 1) + " processing range: " + begin + " to " + (begin + range));
            begin += range + 1;
        }

        // Wait for child threads to finish
        for (int i = 0; i < numChild; i++) {
            threads[i].join();
        }

        System.err.println("All Children Done: " + numChild);
    }

    public void run() {
        // Get the current time
        start_s = System.nanoTime();

        int val; 
        int i;
        for (val = begin; val < end; val++) {
            for (i = 2; i <= val / 2; i++) {
                if (val % i == 0) break;
            }
            /* For faster run, comment out the two instructions below */
            if (i > val / 2) {
                System.out.println("F:" + val);
            }
        }

        // Calculate execution time in ms and print
        long stop_s = System.nanoTime();
        long duration = TimeUnit.NANOSECONDS.toMillis(stop_s - start_s);
        System.err.println("Child finished processing range: " + begin + " to " + (end - 1) + ". Time taken: " + duration + " ms");
    }
}
