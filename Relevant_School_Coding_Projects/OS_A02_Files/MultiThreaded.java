public class MultiThreaded implements Runnable {

    private static final double errorThreshold = 5.0;
    private final double[][] matrix;
    private final int size;
    private double totalError = 0;
    private int iterations = 0;

    public MultiThreaded(double[][] matrix, int size) {
        this.size = size;
        this.matrix = matrix;

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

    @Override
    public void run() {
        while (true) {
            totalError = 0;

            double localError = 0;

            int startI = 1;
            int endI = size - 1;

            for (int j = startI; j < endI; j++) {
                for (int k = 1; k < size - 1; k++) {
                    double oldTemp = matrix[j][k];
                    double newTemp = (matrix[j - 1][k] + matrix[j + 1][k] + matrix[j][k - 1] + matrix[j][k + 1]) / 4.0;
                    matrix[j][k] = newTemp;
                    localError += Math.abs(newTemp - oldTemp);
                }
            }

            synchronized (MultiThreaded.class) {
                totalError += localError;
            }

            synchronized (MultiThreaded.class) {
                iterations++;
                if (totalError < errorThreshold)
                    break;
            }
        }
    }


    public double getTotalError() {
        return totalError;
    }

    public int getIterations() {
        return iterations;
    }

    public double getAverageTemp() {
        double sum = 0;
        for (double[] row : matrix) {
            for (double temp : row) {
                sum += temp;
            }
        }

        int totalSize = size * size;
        return sum / totalSize;
    }

    public void printMatrix() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.printf("%d ", (int) Math.round(matrix[i][j]));
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int matrixSize = 100;
        long startTime = System.currentTimeMillis();

        double[][] matrix = new double[matrixSize][matrixSize];

        MultiThreaded[] workers = new MultiThreaded[4];
        Thread[] threads = new Thread[4];


        for (int i = 0; i < 4; i++) {

            workers[i] = new MultiThreaded(matrix, matrixSize);
            threads[i] = new Thread(workers[i]);

            threads[i].start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // Print results
        System.out.println("Type: Multi-threaded; Size = " + matrixSize);
        System.out.println("Total Error = " + workers[0].getTotalError());
        System.out.println("Average matrix Value = " + workers[0].getAverageTemp());
        System.out.println("Number of Iterations: " + workers[0].getIterations());
        workers[0].printMatrix();

        System.out.println("Elapsed time: " + elapsedTime + " milliseconds");
    }
}



