import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
/**
 * This class will
 * * <p>
 * *     This class has 5 methods, main(), breadthFirstSearch(), findNeighbors(), wordDiff(), getPath()
 * *     and printPath().
 * * </p>
 * * <p>
 * *     This class will use a start and end word and will make a graph using neighbors of the words
 * *     and will find the shortest path between the words to eventually make a word ladder.
 * </p>
 * @author Aaron Antreassian
 * @edu.uwp.cs.340.course CSCI 340 - Data Structures/Algorithm Design
 * @edu.uwp.cs.340.Section 001
 * @edu.uwp.cs.340.assignment 5
 * @bugs none
 */
public class WordLadder {

    private static ArrayList<String> path = new ArrayList<>(); //global list that will keep the path of the ladder

    /**
     * main() will ask the user for a start words and an end word.
     * It will then make a hashmap named graph and put that as well as the start and end word
     * @param args
     */
    public static void main(String[] args) {
        Scanner keybd = new Scanner(System.in);
        System.out.println("Enter a starting word");
        String first = keybd.next();
        System.out.println("Enter your ending word");
        String last = keybd.next();

        //Confirm lengths of the words are the same
        int length = first.length();
        if (length != last.length()) {
            System.err.println("ERROR! Words not of the same length.");
            System.exit(1);
        }

        // Creates an empty graph with the starting word as the root node
        HashMap<String, ArrayList<String>> wordList = new HashMap<>();
        breadthFirstSearch(wordList, first, last);
    }

    /**
     * Performs breadth-first search to find the shortest path between two words
     * @param graph
     * @param startWord
     * @param endWord
     */
    public static void breadthFirstSearch(HashMap<String, ArrayList<String>> graph, String startWord, String endWord) {

        // If the starting word and the ending word are the same, return the starting word as the path
        if (startWord.equals(endWord)) {
            path.add(startWord);
            printPath();
            return;
        }

        // Create an array list of visited nodes and add the starting word
        ArrayList<String> visitedWord = new ArrayList<String>();
        visitedWord.add(startWord);

        // Create an array list for the queue and add the starting word
        ArrayList<String> queue = new ArrayList<>();
        queue.add(startWord);

        // Create a hashmap for the parents of each node
        HashMap<String, String> parent = new HashMap<>();
        parent.put(startWord, null);

        // Traverse the graph using breadth-first search algorithm
        while (!queue.isEmpty()) {
            String currentWord = queue.remove(0);

            // Find the neighboring nodes of the current word
            findNeighbors(currentWord, graph);

            ArrayList<String> neighbors = graph.get(currentWord);
            for (String neighbor : neighbors) {
                if (!visitedWord.contains(neighbor)) {
                    visitedWord.add(neighbor);
                    queue.add(neighbor);
                    parent.put(neighbor, currentWord);

                    // If the neighbor word is the end word, the shortest path is found and return it
                    if (neighbor.equals(endWord)) {
                        getPath(parent, startWord, endWord);
                        return;
                    }
                }
            }
        }

        // If no path is found, print an error message
        System.out.println("No word ladder found");
    }


    /**
     * Finds all the neighbors of the given word by searching in the corresponding file of words
     * @param word
     * @param graph
     * @throws FileNotFoundException
     */
    public static void findNeighbors(String word, HashMap<String, ArrayList<String>> graph) {
        File file = new File("./src/words." + word.length());
        ArrayList<String> neighbors = new ArrayList<String>();
        try {
            Scanner readFile = new Scanner(file);

            while (readFile.hasNextLine()) {
                String nextWord = readFile.nextLine();
                if (wordDiff(word, nextWord))
                    neighbors.add(nextWord);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file.getAbsolutePath());
            e.printStackTrace();
            System.exit(1);
        }
        graph.put(word, neighbors);
    }

    /**
     * Returns true if there is only one character difference between two words
     * @param word1
     * @param word2
     * @returns a boolean value determining if two words are neighbors
     */
    public static boolean wordDiff(String word1, String word2) {
        int difference = 0;
        for (int x = 0; x < word1.length(); x++) {
            if (word1.charAt(x) != word2.charAt(x))
                difference++;
        }
        return difference == 1;
    }

    /**
     * Finds the path from the start word to the end word using the parent hashmap
     * @param parent
     * @param startWord
     * @param endWord
     */
    public static void getPath(HashMap<String, String> parent, String startWord, String endWord) {
        String current = endWord;
        path.add(endWord); //add end word first since we want to print from the start to end
        while (!current.equals(startWord)) {
            current = parent.get(current);
            path.add(0, current); // add to 0 index to push elements further into the list
        }
        printPath(); //print finished path once it is complete
    }

    /**
     * Prints the word ladder from the original start to end word
     */
    public static void printPath() {
        for (String word : path) {
            if(word.equals(path.get(path.size()-1))) {
                System.out.print(word);
                break;
            }
            System.out.print(word + " --> ");
        }
        System.out.println();
        path.clear();
    }
}

