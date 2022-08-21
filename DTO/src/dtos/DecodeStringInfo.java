package dtos;

import java.io.Serializable;

public class DecodeStringInfo implements Serializable {

    private String toEncodeString;

    private String decodedString;

    private long timeInMilli;

    public DecodeStringInfo(String toEncodeString, String decodedString, long timeInMilli) {
        this.toEncodeString = toEncodeString;
        this.decodedString = decodedString;
        this.timeInMilli = timeInMilli;
    }

    public String getToEncodeString() {
        return toEncodeString;
    }
    public String getDecodedString() {
        return decodedString;
    }
    public long getTimeInMilli() {
        return timeInMilli;
    }
}
