// An implementation of the basic Trie data structure from
// Geeks for Geeks.
// This code was contributed by Sumit Ghosh

// Java implementation of search and insert operations
// on Trie.  You will need to modify this class to complete
// Project 3.

public class Trie {

    // Alphabet size (# of symbols) 
    static final int ALPHABET_SIZE = 26;
    static TrieNode root = new TrieNode(); //root node declaration

    //Default constructor with no parameters
    Trie() {
    }

    // Inner class - TrieNode
    static class TrieNode {
        TrieNode[] children = new TrieNode[ALPHABET_SIZE];

        // isEndOfWord is true if the node represents 
        // end of a word 
        boolean isEndOfWord;

        TrieNode() {
            isEndOfWord = false;
            for (int i = 0; i < ALPHABET_SIZE; i++)
                children[i] = null;
        }
    }

    ;

    // If not present, inserts key into trie 
    // If the key is prefix of trie node,  
    // just marks leaf node 
    public static void insert(String key) {
        int level;
        int length = key.length();
        int index;

        TrieNode pCrawl = root;

        for (level = 0; level < length; level++) {
            index = key.charAt(level) - 'a';
            if (pCrawl.children[index] == null)
                pCrawl.children[index] = new TrieNode();

            pCrawl = pCrawl.children[index];
        }

        // mark last node as leaf 
        pCrawl.isEndOfWord = true;
    }

    //isWord method that check whether the inserted word is within the trie
    public static boolean isWord(String key) {
        int level;
        int length = key.length();
        int index;
        TrieNode pCrawl = root;

        for (level = 0; level < length; level++) {
            index = key.charAt(level) - 'a';

            if (pCrawl.children[index] == null)
                return false;

            pCrawl = pCrawl.children[index];
        }

        return (pCrawl != null && pCrawl.isEndOfWord);
    }

    //isTrie method that
    //returns 1 if argument is a complete word
    //returns 0 if argument is not a complete word but could be the prefix of a longer word
    //returns -1 if the argument is not a complete word nor a prefix
    public static int isTrie(String key) {
        int level;
        int length = key.length();
        int index;

        TrieNode pCrawl = root;

        for (level = 0; level < length; level++) {
            index = key.charAt(level) - 'a';

            //case when the key is not within the trie
            if (pCrawl.children[index] == null)
                return -1;

            pCrawl = pCrawl.children[index];
        }

        //case when the key is a prefix or a complete word
        if (pCrawl != null && !pCrawl.isEndOfWord) {
            return 0;
        } else {
            return 1;
        }
    }
}