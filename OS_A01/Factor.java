import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class Factor implements Runnable {
    private int begin;
    private int end;
    private long start_s;

    public Factor(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    public static void main(String[] args) throws InterruptedException {
        

        int numChild = 1;
        int total = 100;
        int range = total / numChild;
        int begin = 0;

        System.err.println("Run Factor " + total + ":" + numChild);

        // Create child threads
        Thread[] threads = new Thread[numChild];
        for (int i = 0; i < numChild; i++) {
            threads[i] = new Thread(new Factor(begin, begin + range));
            threads[i].start();
            begin += range + 1;
        }

        // Wait for child threads to finish
        for (int i = 0; i < numChild; i++) {
            threads[i].join();
        }
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
            if (i > val / 2) {
                System.out.println("F:" + val);
            }
        }

        // Calculate execution time in ms and print
        long stop_s = System.nanoTime();
        System.err.println("time: " + TimeUnit.NANOSECONDS.toMillis(stop_s - start_s));
    }

    
}
