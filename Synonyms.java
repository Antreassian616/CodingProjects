import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/**
 * This class will
 * * <p>
 * *     This class will a global hashmap named descriptor and will have 5 methods, main(), parseCorpus(),
 * *     cosineSimilarity(), cosineNumerator(), and cosineDenominator().
 * *     It will also have 1 constructor named Synonyms().
 * *
 * * </p>
 * * <p>
 * *     This class will take in a corpus and using parseCorpus(), will parse through the corpus
 * *     and populate a global hashmap named descriptor holding a string and another hashmap.
 * *     Then the cosineSimilarity() method will use the hashmap to calculate the cosine similarity of two words.
 * *     The user will pick a word and 4 choices that will be compared to the word chosen with the cosine similarity
 * *     displayed along woth the choices.
 * </p>
 * @author Aaron Antreassian
 * @edu.uwp.cs.242.course CSCI 242 - Computer Science II
 * @edu.uwp.cs.242.Section 001
 * @edu.uwp.cs.242.assignment 5
 * @bugs none
 */
public class Synonyms {
    private HashMap<String, HashMap<String, Integer>> descriptor;

    /**
     * This constructor will instantiate the hasmap and call the parseCorpus() method
     * @param corpus
     */
    public Synonyms ( URL[] corpus ) {
        descriptor = new HashMap<String, HashMap<String, Integer>>();
        parseCorpus(corpus);
    }

    /**
     * Will prompt user for descriptor and word choices to be compared
     * @param args
     * No return type
     * Throws malformed URL Exception
     */
    public static void main(String[] args) throws MalformedURLException {
        Scanner input = new Scanner(System.in);
        String descriptor = "";

        URL[] corpus = {
                //Pride and Prejudice, by Jane Austen
                new URL("https://www.gutenberg.org/files/1342/1342-0.txt"),
                // The Adventures of Sherlock Holmes, by A. Conan Doyle
                new URL("http://www.gutenberg.org/cache/epub/1661/pg1661.txt"),
                // A Tale of Two Cities, by Charles Dickens
                new URL("https://www.gutenberg.org/files/98/98-0.txt"),
                // Alice's Adventures In Wonderland, by Lewis Carroll
                new URL("https://www.gutenberg.org/files/11/11-0.txt"),
                // Moby Dick; or The Whale, by Herman Melville
                new URL("https://www.gutenberg.org/files/2701/2701-0.txt"),
                // War and Peace, by Leo Tolstoy
                new URL("https://www.gutenberg.org/files/2600/2600-0.txt"),
                // The Importance of Being Earnest, by Oscar Wilde
                new URL("http://www.gutenberg.org/cache/epub/844/pg844.txt"),
                // The Wisdom of Father Brown, by G.K. Chesterton
                new URL("https://www.gutenberg.org/files/223/223-0.txt"),
        };
        Synonyms test = new Synonyms(corpus);

        do {
            System.out.println("Enter a word:");
            descriptor = input.nextLine();
            if (descriptor.isEmpty()) {
                System.out.println("no word entered");
                System.exit(0);
            }

            System.out.println("Enter the Choices:");
            String options = input.nextLine();

            //Separating the words entered by spaces and storing them into an array of strings:
            String[] wordOptions = options.split("\\s");

            //Declaring and initializing the closest cosine value calculated by the method:
            //The method will then calculate the closest value and closest word and store it.
            double closestCosValue = test.calculateCosineSimilarity(descriptor,wordOptions[0]);
            String closestWord = wordOptions[0];
            System.out.println(closestWord + " " + closestCosValue);

            for(int i = 1; i < wordOptions.length; i++) {
                String check = wordOptions[i];
                double temp = test.calculateCosineSimilarity(descriptor,check);
                System.out.println(check + " " + temp);
                if(temp > closestCosValue) {
                    closestCosValue = temp;
                    closestWord = check;
                }
            }
            //Print the closest option.
            System.out.println(closestWord + " is the closest option.");
        }
        while (!descriptor.isEmpty()) ;
        System.out.println("End main.");

    }
    /**
     * will parse through the corpus to populate the outer and inner hashmaps
     * @param corpus
     * no return type
     * Catches IO exception
     */
    public void parseCorpus ( URL[] corpus ) {
        try {

            for (int i = 0; i < corpus.length; i++) {
                URL url = corpus[i];

                //create scanner to read urls
                Scanner reader = new Scanner(url.openStream());

                //Set delimiter for the scanner
                reader.useDelimiter ( "[\\.\\?\\!]|\\Z" );

                // Read until the end of the URL
                while (reader.hasNext ())
                {
                    String sentence = reader.next ();
                    sentence = sentence.toLowerCase();
                    String[] words = sentence.split("\\s");


                    //loop through word array to strip out punctuation and add keys to descriptors
                    for(int j = 0; j < words.length; j++) {
                        String keyWord = words[j].replaceAll("\\W","");

                        //create a key for each word inside the corpus
                        if(!descriptor.containsKey(keyWord))
                            descriptor.put(keyWord, new HashMap<String, Integer>());

                        //loop through array to populate the hashmaps for each key
                        for(int k = 0; k < words.length; k++) {
                            String innerWords = words[k];
                            HashMap innerHash = descriptor.get(keyWord);
                            if(keyWord.equals(innerWords)){

                            }
                            else if(!innerHash.containsKey(innerWords)) {
                                innerHash.put(innerWords, 1);
                            }
                            else {
                                int count = (Integer)innerHash.get(innerWords);
                                innerHash.put(innerWords, count + 1);
                            }
                        }
                    }

                }
            }
        } catch(IOException e) {
            System.out.print("Error, something went wrong");
        }
    }

    /**
     * Will call the helper numerator and denominator methods to calculate cosine similarity
     * @param word1
     * @param word2
     * @return
     */
    public double calculateCosineSimilarity ( String word1, String word2 ) {
        if(descriptor.get(word1) == null || descriptor.get(word2) == null)
            return -1;
        double numerator = cosNumerator(word1, word2);
        double denominator = cosDenominator(word1, word2);
        return numerator / denominator;
    }

    /**
     * will calculate numerator of cosineSimilarity formula
     * @param word1
     * @param word2
     * @return
     */
    public double cosNumerator(String word1, String word2) {
        HashMap<String,Integer> word1Hash = descriptor.get(word1);
        HashMap<String,Integer> word2Hash = descriptor.get(word2);
        double numerator = 0.0;
        for(Map.Entry<String, Integer> first : word1Hash.entrySet()) {
            int second = word2Hash.getOrDefault(first.getKey(), 0);
            numerator += (first.getValue() * second);
        }
        return numerator;
    }

    /**
     * will calculate denominator of cosineSimilarity formula
     * @param word1
     * @param word2
     * @return
     */
    public double cosDenominator(String word1, String word2) {

        HashMap<String,Integer> word1Hash = descriptor.get(word1);
        HashMap<String,Integer> word2Hash = descriptor.get(word2);
        double leftSum = 0;
        double rightSum = 0;
        for(Integer i: word1Hash.values()) {
            leftSum += Math.pow(i,2);
        }

        for(Integer i: word2Hash.values()) {
            rightSum += Math.pow(i,2);
        }
        return Math.sqrt(leftSum * rightSum);
    }
}

