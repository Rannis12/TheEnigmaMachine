package logic.enigma;

import resources.jaxb.generated.CTEDecipher;
import resources.jaxb.generated.CTEDictionary;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.MatchResult;

public class Dictionary implements Serializable {

    private Set<String> dictionary;

    private String excludedCharacters;
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
        //String[] regexes = new String[] {"\\+","\\*","\\^", "\\{", "\\}", "\\?"};

        for (int i = 0; i < wordsArr.length; i++) {
            boolean isValidWord = true;

            for (int j = 0; j < excludedCharacters.length(); j++) {//remove all excluded characters
                /*String tmpString = "";
                tmpString += excludedCharacters.charAt(j);

                for (String tmp : regexes) {
                    if(tmp.equals(tmpString)){
                        tmpString = tmp;
                        break;
                    }
                }*/
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
}
