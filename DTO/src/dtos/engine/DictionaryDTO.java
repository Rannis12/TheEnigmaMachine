package dtos.engine;

import java.util.Set;

public class DictionaryDTO {

    private Set<String> dictionary;
    private String excludedCharacters;

    public DictionaryDTO(Set<String> dictionary, String excludedCharacters) {
        this.dictionary = dictionary;
        this.excludedCharacters = excludedCharacters;
    }

    public Set<String> getDictionary() {
        return dictionary;
    }

    public String getExcludedCharacters() {
        return excludedCharacters;
    }
}
