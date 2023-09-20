package logic.enigma;

import resources.jaxb.generated.CTEDictionary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Dictionary implements Serializable {
    private Set<String> dictionary;
    private String excludedCharacters;

    public Dictionary(Set<String> dictionary, String excludedCharacters){
        this.dictionary = dictionary;
        this.excludedCharacters = excludedCharacters;
    }
    Dictionary(KeyBoard keyBoard, CTEDictionary cteDictionary) {
        dictionary = new HashSet<>();
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
    }
    public String getExcludedCharacters(){
        return excludedCharacters;
    }
    public boolean isExistInDictionary(String string) {
        for (String str : dictionary) {
            if(str.equals(string)){
                return true;
            }
        }
        return false;
    }
    public ArrayList<String> searchForSubStrings(String prefix) {
        ArrayList<String> wordsInDictionary = new ArrayList<>();
        for (String word : dictionary) {
            if(word.contains(prefix)){
                wordsInDictionary.add(word);
            }
        }
        //sorting the words.
        wordsInDictionary.
                stream().
                sorted();

        return wordsInDictionary;
    }
    public Set<String> getDictionary() {
        return dictionary;
    }
}
