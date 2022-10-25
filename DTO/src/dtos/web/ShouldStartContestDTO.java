package dtos.web;

public class ShouldStartContestDTO {
    private final String toEncode;
    private final boolean shouldStart;

    public ShouldStartContestDTO(String toEncode, boolean shouldStart) {
        this.toEncode = toEncode;
        this.shouldStart = shouldStart;
    }

    public String getToEncode() {
        return toEncode;
    }

    public boolean isShouldStart() {
        return shouldStart;
    }
}
