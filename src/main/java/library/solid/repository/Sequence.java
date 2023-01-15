package library.solid.repository;

public class Sequence {

    private static Long sequence = 0L;

    public static Long getSequence() {
        return ++sequence;
    }
}
