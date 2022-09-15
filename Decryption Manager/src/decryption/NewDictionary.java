package decryption;

import logic.enigma.KeyBoard;
import resources.jaxb.generated.CTEDictionary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// A class to store a Trie node
public class NewDictionary {
    private boolean isLeaf;
    private Map<Character, NewDictionary> children;
    private CTEDictionary cteDictionary;


    private String excludedCharacters;
    private KeyBoard keyBoard;

    // Constructor
    public NewDictionary(KeyBoard keyBoard, CTEDictionary cteDictionary) {
        isLeaf = false;
        children = new HashMap<>();

        this.cteDictionary = cteDictionary;
        initMethod(this.cteDictionary);

        this.keyBoard = keyBoard;
    }

    // Iterative function to insert a string into a Trie
    public void insert(String key) {
        System.out.println("Inserting \"" + key + "\"");

        // start from the root node
        NewDictionary curr = this;

        // do for each character of the key
        for (char c: key.toCharArray()) {
            // create a new node if the path doesn't exist
            curr.children.putIfAbsent(c, new NewDictionary(this.keyBoard, this.cteDictionary));

            // go to the next node
            curr = curr.children.get(c);
        }

        // mark the current node as a leaf
        curr.isLeaf = true;
    }

    // Iterative function to search a key in a Trie. It returns true
    // if the key is found in the Trie; otherwise, it returns false
    public boolean search(String key) {
        System.out.print("Searching \"" + key + "\" : ");

        NewDictionary curr = this;

        // do for each character of the key
        for (char c: key.toCharArray()) {
            // go to the next node
            curr = curr.children.get(c);

            // if the string is invalid (reached end of a path in the Trie)
            if (curr == null) {
                return false;
            }
        }

        // return true if the current node is a leaf node and the
        // end of the string is reached
        return curr.isLeaf;
    }

    /*public static void main (String[] args) {
        // construct a new Trie node
        NewDictionary head = new NewDictionary();

        head.insert("techie!");
        head.insert("techi");
        head.insert("tech");

        System.out.println(head.search("tech"));            // true
        System.out.println(head.search("techi"));           // true
        System.out.println(head.search("techie!"));          // true
        System.out.println(head.search("techiedelight"));   // false

        head.insert("techiedelight");

        System.out.println(head.search("tech"));            // true
        System.out.println(head.search("techi"));           // true
        System.out.println(head.search("techie"));          // true
        System.out.println(head.search("techiedelight"));   // true
    }*/

    private void initMethod(CTEDictionary cteDictionary){
        Set<String> dictionary = new HashSet<>();
        excludedCharacters = cteDictionary.getExcludeChars();

        String words = cteDictionary.getWords().trim();

        int amountOfWords = 0;
        for (int i = 0; i < words.length(); i++) {
            if(words.charAt(i) == ' ') {
                amountOfWords++;
            }
        }
        if(amountOfWords > 0) {//only if the string isn't empty
            amountOfWords++;
        }
        String[] wordsArr = words.split(" ", amountOfWords);
        //String[] regexes = new String[] {"\\+","\\*","\\^", "\\{", "\\}", "\\?"};

        for (int i = 0; i < wordsArr.length; i++) {
            boolean isValidWord = true;

            for (int j = 0; j < excludedCharacters.length(); j++) {//remove all excluded characters
                wordsArr[i] = wordsArr[i].replace(String.valueOf(excludedCharacters.charAt(j)), "");

            }
            wordsArr[i] = wordsArr[i].toUpperCase();
            for (int j = 0; j < wordsArr[i].length() && isValidWord; j++) {//check if characters of word[i] is in language -- !to check if necessary!
                if(! keyBoard.isExist(wordsArr[i].charAt(j)))
                    isValidWord = false;
            }

            if(! dictionary.contains(wordsArr[i]) && isValidWord) {
                dictionary.add(wordsArr[i]);
            }
        }
        for (String string: dictionary) {
            insert(string);
        }
    }

    public String getExcludedCharacters() {
        return excludedCharacters;
    }

}

