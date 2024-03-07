public class SingleThread {

    // The error threshold
    private double error = 5.0;
    // 2D array to hold the temperature values of each cell in the matrix
    private double[][] matrix;
    // size of the matrix
    private final int size;

    public SingleThread(int size) {
        this.size = size;
        this.matrix = new double[size][size];

        // Set edge temperatures
        // Corners not included
        for (int i = 1; i < size - 1; i++) {
            matrix[0][i] = 30; // Top edge
            matrix[size - 1][i] = 75; // Bottom edge
            matrix[i][0] = 15; // Left edge
            matrix[i][size - 1] = 72; // Right edge
        }

        // Initialize corner cells with the given neighbor cells
        matrix[0][0] = (15 + 30) / 2.0; // Top-left corner
        matrix[0][size - 1] = (30 + 72) / 2.0; // Top-right corner
        matrix[size - 1][0] = (15 + 75) / 2.0; // Bottom-left corner
        matrix[size - 1][size - 1] = (75 + 72) / 2.0; // Bottom-right corner


    }

    // Calculate temperatures
    public void calculateAverage() {
        // Variable for iterations.
        int iterations = 0;
        // Variable for Errors
        double totalError = 0;

        // Iterate through the matrix to fill empty slots until error is low enough
        while (true) {
            totalError = 0; // Reset total error

            //Calculate the new temperature for each inside cell
            for (int i = 1; i < size - 1; i++) {
                for (int j = 1; j < size - 1; j++) {
                    double oldTemp = matrix[i][j];
                    // Calculate average using the 4 neighbors
                    double newTemp = (matrix[i - 1][j] + matrix[i + 1][j] + matrix[i][j - 1] + matrix[i][j + 1]) / 4.0;
                    // new temps.
                    matrix[i][j] = newTemp;
                    // Update the total error
                    totalError += Math.abs(newTemp - oldTemp);
                }
            }

            iterations++; // Increment iteration count
            // Check if total error is within acceptable limits to stop iteration
            if (totalError < error)
                break;
        }


        // Average temp of matrix.
        double averageTemp = getAverageTemp();
        // Print results
        System.out.println("Type: Single thread; Size = " + size);
        System.out.println("Average matrix Value = " + averageTemp);
        System.out.println("Total Error = " + totalError);
        System.out.println("Number of iterations: " + iterations);
        printmatrix(); // Print the final temperature matrix
    }
    
    // Method to get total average temperature
    private double getAverageTemp() {
        // Initialize sum of all temps
        double sum = 0; 
        // For loop to get total of all temps added together
        for (double[] row : matrix) {
            for (double temp : row) {
                sum += temp; 
            }
        }
        // Total number of cells
        int totalSize = size * size;
        // Returns average temp by dividing the sum by the total number of cells
        return sum / (totalSize);
    }

    
    // print the matrix
    private void printmatrix() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // Prints each number and round to nearest whole number
                System.out.printf("%d ", (int)Math.round(matrix[i][j]));
            }
            // move to next line
            System.out.println();
        }
    }

    // Main method
    public static void main(String[] args) {
        // set matrix size
        int matrixSize = 5;
        long startTime = System.currentTimeMillis(); // Record start time
        SingleThread test = new SingleThread(matrixSize);
        test.calculateAverage();
        long endTime = System.currentTimeMillis(); // Record end time
        long elapsedTime = endTime - startTime;
        System.out.println("Elapsed time: " + elapsedTime + " milliseconds");

    }
}