package utils;

import java.util.Random;
import java.util.UUID;

public class GeneratorUtils {
    public static UUID generateUUID() {
        return UUID.randomUUID();
    }

    public static long generateLongUUID() {
        return new Random().nextLong();
    }
}
