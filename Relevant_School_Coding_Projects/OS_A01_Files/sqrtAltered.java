import java.util.concurrent.TimeUnit;

public class sqrtAltered implements Runnable {
    private double begin;
    private double end;
    private long start_s;

    public sqrtAltered(double begin, double end) {
        this.begin = begin;
        this.end = end;
    }

    public static void main(String[] args) throws InterruptedException {
        int numChild = 4;
        int total = 100;
        double range = total / numChild;
        double begin = 0;

        System.err.println("Run Sqrt " + total + ":" + numChild);

        // Spawn child threads
        Thread[] threads = new Thread[numChild];
        for (int i = 0; i < numChild; i++) {
            threads[i] = new Thread(new Sqrt(begin, begin + range));
            threads[i].start();
            begin += range + 1;
        }

        // Wait for child threads to finish
        for (int i = 0; i < numChild; i++) {
            threads[i].join();
        }

        System.err.println("All Children Done: " + numChild);
    }

    public void run() {
    // Print the current thread ID and start time
    System.err.println("Thread ID=" + Thread.currentThread() + ", Start Time: " + System.nanoTime());

    // Calculate and print square roots
    for (double i = begin; i <= end; i++) {
        double sqrt = Math.sqrt(i);
        System.out.println(i + ": " + sqrt);
    }

    // Calculate execution time in ms and print
    long endTime = System.nanoTime();
    System.err.println("Thread ID=" + Thread.currentThread() + ", End Time: " + endTime);
    System.err.println("time: " + TimeUnit.NANOSECONDS.toMillis(endTime - start_s));
}
}
