import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.atomic.AtomicBoolean;

public class BarrierThread implements Runnable {

    private static final double ERROR = 5.0;
    private int iterations;
    private final double[][] matrix;
    private final int size;
    private static volatile double totalError = 0;
    private final CyclicBarrier sync;
    private static AtomicBoolean complete = new AtomicBoolean(false);
    private final int startIndex;
    private final int endIndex;

    public BarrierThread(double[][] matrix, int size, int startIndex, int endIndex, CyclicBarrier sync) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.size = size;
        this.matrix = matrix;
        this.sync = sync;

        for (int i = 1; i < size - 1; i++) {
            matrix[0][i] = 30; // Top edge
            matrix[size - 1][i] = 75; // Bottom edge
            matrix[i][0] = 15; // Left edge
            matrix[i][size - 1] = 72; // Right edge
        }

        // Initialize corner cells with the given neighbor cells
        matrix[0][0] = (15 + 30) / 2.0; // Top left corner
        matrix[0][size - 1] = (30 + 72) / 2.0; // Top right corner
        matrix[size - 1][0] = (15 + 75) / 2.0; // Bottom left corner
        matrix[size - 1][size - 1] = (75 + 72) / 2.0; // Bottom right corner
    }

    public void run() {
        while (!complete.get()) {
            double localError = 0;
            iterations++;

            for (int i = startIndex; i < endIndex; i++) {
                for (int j = 1; j < size - 1; j++) {
                    if (i == 0 || i == size - 1) continue;

                    double oldTemp = matrix[i][j];
                    double newTemp = Math.round((matrix[i - 1][j] + matrix[i + 1][j] + matrix[i][j - 1] + matrix[i][j + 1]) / 4.0);
                    matrix[i][j] = newTemp;

                    localError += Math.abs(newTemp - oldTemp);
                }
            }

            synchronized (BarrierThread.class) {
                totalError += localError;
            }

            try {
                sync.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            if (sync.getNumberWaiting() == 0) {
                synchronized (BarrierThread.class) {
                    if (!complete.get()) {
                        totalError = 0;
                    }
                }
            }
        }
    }

    public double getAverageTemp() {
        double sum = 0;
        for (double[] row : matrix) {
            for (double temp : row) {
                sum += temp;
            }
        }
        return Math.round(sum / (size * size));
    }

    public void printMatrix() {
        for (double[] row : matrix) {
            for (double temp : row) {
                System.out.print((int) Math.round(temp) + " ");
            }
            System.out.println();
        }
    }

    public double getTotalError() {
        return totalError;
    }

    public int getIterations() {
        return iterations;
    }

    public static void main(String[] args) {
        int matrixSize = 1000;
        long startTime = System.currentTimeMillis();

        double[][] matrix = new double[matrixSize][matrixSize];

        CyclicBarrier sync = new CyclicBarrier(4, () -> {
            if (totalError < ERROR) {
                complete.set(true);
            }
        });

        BarrierThread[] workers = new BarrierThread[4];
        Thread[] threads = new Thread[4];

        int partSize = (int) Math.ceil((double) matrixSize / workers.length);
        for (int i = 0; i < workers.length; i++) {
            int startIndex = i * partSize;
            int endIndex = Math.min(startIndex + partSize, matrixSize);
            workers[i] = new BarrierThread(matrix, matrixSize, startIndex, endIndex, sync);
            threads[i] = new Thread(workers[i]);
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        System.out.println("Type: Multi-threaded with barrier; Size = " + matrixSize);
        System.out.println("Average matrix Value = " + workers[0].getAverageTemp());
        System.out.println("Total Error = " + totalError);
        System.out.println("Total iterations: " + workers[0].getIterations()); 
        workers[0].printMatrix();
        System.out.println("Elapsed time: " + elapsedTime + " milliseconds");
    }
}
