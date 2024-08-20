package BloggingApp;

import java.util.concurrent.atomic.AtomicInteger;

public class UniqueIDGenerator {

    private static final AtomicInteger counter = new AtomicInteger(0);

    public static String generateUniqueId() {
        long timestamp = System.currentTimeMillis();
        int uniqueCounter = counter.getAndIncrement() % 10000; // Increment counter, reset every 10000
        return timestamp + "-" + uniqueCounter;
    }
}

