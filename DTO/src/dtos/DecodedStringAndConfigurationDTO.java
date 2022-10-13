package dtos;

public class DecodedStringAndConfigurationDTO {
    private String decodedString;
    private String currConfiguration;

    public DecodedStringAndConfigurationDTO(String decodedString, String currConfiguration) {
        this.decodedString = decodedString;
        this.currConfiguration = currConfiguration;
    }

    public String getDecodedString() {
        return decodedString;
    }

    public String getCurrConfiguration() {
        return currConfiguration;
    }
}
