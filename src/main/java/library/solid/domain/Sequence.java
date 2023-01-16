package library.solid.domain;

public class Sequence {

    private static Long sequence = 0L;

    public static Long getSequence() {
        return ++sequence;
    }
}
