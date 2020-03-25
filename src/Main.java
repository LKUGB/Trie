import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class Main {

    // static instance data
    static private ArrayList<String> lexicon = new ArrayList<>(); // the word list read from file
    static private Set<String> foundWords = new TreeSet<>();      // words found in this puzzle
    static private Grid grid;     // represents a Boggle board
    static private int gridSize;  // set after Grid is read from file
    static private int maxWordLength = 0;  // used to cut down on depth of search

    // Read the lexicon or word list from a file named twl06.txt
    // -- creates an ArrayList of Strings in a class variable named lexicon
    static private void readWords(){
        ArrayList<String> result = new ArrayList<>();
        try{
            // twl06 is an official Scrabble word list used in tournament play
            BufferedReader br = new BufferedReader(new FileReader("src/twl06.txt"));
            try {
                String line;
                while ((line = br.readLine()) != null) {
                    if(line.length() > maxWordLength)
                        maxWordLength = line.length();
                    lexicon.add(line);
                }
            } finally {
                br.close();
            }
        } catch (IOException e){
            System.out.println("Exception while processing word list file.");
        }
    }

    // Check str to see if it is either a word in the lexicon or the prefix
    // of a word.  If it's neither, then there's no need to continue down
    // this path.
    // Returns:
    //   -1 if str is not a word or a prefix of any word in the word list
    //    0 if str is a prefix of a word but not a complete word
    //    1 if str is a complete word in the word list
    private static int prefix( String str, ArrayList<String> lex){
        int strLength = str.length();
        // potentially look through entire lexicon
        for(int i=0; i<lex.size(); i++){
            String nextWord = lex.get(i);
            // 3 possibilities to check
            if(str.equals(nextWord))   // complete match
                return 1;
            else if(nextWord.length() > strLength &&   // prefix match
                      str.equals(lex.get(i).substring(0,strLength)))
                return 0;
            else if(str.compareTo(nextWord) < 0)  // shortcut exit
                return -1;
        }
        return -1;
    }

    // A recursive function to find all words present in a Boggle grid
    // and add them to an ArrayList named foundWords
    // - str is the String formed so far in this recursive search
    // - visited[i][j] is true if letter at [i][j] is already in str
    // - i (row), j (col): position of next character to add to str
    static private void findWordsUtil(boolean visited[][],
                                      int i, int j, String str){

        // no need to keep exploring if str is already as long
        //    as the longest word in lexicon
        if(str.length() == maxWordLength)
            return;

        // Mark current cell as visited and append current character
        // to String under consideration
        visited[i][j] = true;
        str = str + grid.getLetter(i,j);

        // look up 'str' in lexicon, prefix() returns:
        //    -1 if str is not a prefix of any word in the word list
        //     0 if str is a prefix of a word but not a complete word
        //     1 if str is a complete word in the word list
        int strCheck = prefix(str, lexicon);

        // str passes first test, it's either a word or a prefix
        if(strCheck >= 0){
            // if str is a complete word in lexicon then add it to foundWords
            if (strCheck == 1 && str.length() > 1)
                foundWords.add(str);  // duplicates automatically eliminated by Set

            // Traverse 8 adjacent cells of boggle[i][j] and explore each recursively
            for (int row = Math.max(i - 1, 0); row <= i + 1 && row < gridSize; row++) {
                for (int col = Math.max(j - 1, 0); col <= j + 1 && col < gridSize; col++)
                    if (!visited[row][col]) {
                        // explore each of 8 unvisited neighbors recursively
                        findWordsUtil(visited, row, col, str);
                    }
            }
        }

        // Backtrack:   mark visited of current cell as false and return
        visited[i][j] = false;
    }

    // Finds all words in the Grid (Boggle board) that are in
    // the lexicon.  Words are collected in a static Set variable
    // named foundWords
    private static void findWords(){
        // Mark all characters in grid as not visited
        boolean visited[][] = new boolean[gridSize][gridSize];

        // Consider every character and look for all words
        // starting with this character
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                findWordsUtil(visited, row, col,"");
            }
        }
    }

    // compute the Boggle score for the words stored in the Set of found words
    public static int score(Set<String> words) {
        int total = 0;
        for (String s : words) {
            int wordLength = s.length();
            if (wordLength <= 4)
                total += 1;
            else if (wordLength == 5)
                total += 2;
            else if (wordLength == 6)
                total += 3;
            else if (wordLength == 7)
                total += 5;
            else if (wordLength > 7)
                total += 11;
        }
        return total;
    }

    // simple method for testing word lookup
    // returns false if something doesn't work right
    private static boolean lookupTest(){
        // check that all legal words were stored
        if(!lexicon.contains("cat"))
            return false;
        if(!lexicon.contains("cats"))
            return false;
        if(!lexicon.contains("catatonic"))
            return false;
        if(!lexicon.contains("rat"))
            return false;
        if(!lexicon.contains("rather"))
            return false;
        // try words not in the list
        if(lexicon.contains("picard") || lexicon.contains("xyzzy"))
            return false;
        // if we get here then all tests passed\
        return true;
    }

    public static void main(String[] args) {

        // read word list / lexicon
        readWords();
        System.out.println( lexicon.size() + " words in word list.");

        // run simple unit test on code for looking up words
        assert(lookupTest()):"test of word lookup failed";

        // read the grid file one puzzle at a time
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/grid.txt"));
            while(true){
                // reset Set of found words to start next puzzle
                foundWords.clear();
                // create and populate a new Grid representing a Boggle board
                grid = new Grid(br);
                gridSize = grid.getGridSize();
                if(gridSize == 0)
                    break;
                else {
                    System.out.println("\n\nSolving Boggle Grid:\n" + grid);
                    findWords();
                    System.out.format("Found %d words\n", foundWords.size());
                    System.out.println("Words: " + foundWords);
                    System.out.println("Max score for this grid is: " + score(foundWords));
                }
            }
        } catch(IOException e) {
            System.out.println("Error reading size from grid.txt");
        }
    }
}
