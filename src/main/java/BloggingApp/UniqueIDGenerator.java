package BloggingApp;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;


public class UniqueIDGenerator {

    int currentValue = 0;
    private static final AtomicInteger counter;

    static {
        try {
            counter = new AtomicInteger(readCounterFromFile()+200);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String COUNTER_FILE = "counter.txt";
    private static final Lock lock = new ReentrantLock();
    private static int iterationCount = 0;

    public static String generateUniqueId() {
        long timestamp = System.currentTimeMillis();
        int uniqueCounter = counter.getAndIncrement() % 10000; // Increment counter, reset every 10000
        return timestamp + "-" + uniqueCounter;
    }

    // Generate a unique integer ID from the MAC address
    private static int generateMachineId(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(input.getBytes());
        int machineId = 0;
        for (int i = 0; i < 4; i++) {
            machineId = (machineId << 8) | (hash[i] & 0xFF);
        }
        return machineId;
    }


    public static int getNextId() {
        lock.lock(); // Acquire lock to ensure thread-safety
        try {
            int currentValue = counter.getAndIncrement();
            long currentTimestamp = System.currentTimeMillis();
            int nextValue = (int) (currentTimestamp+ currentValue );
            iterationCount++;
            if (iterationCount == 200) {
                 writeCounterToFile(nextValue);
                 iterationCount=0;
            }
            return nextValue;
        }catch (IOException e) {
            //throw new RuntimeException("Error accessing counter file", e);
        }
        finally {
            lock.unlock(); // Release lock
        }

        return 0;
    }


    // Read counter value from file
    private static int readCounterFromFile() throws IOException {
        File file = new File(COUNTER_FILE);
        if (!file.exists()) {
            // File does not exist, initialize counter to 0
            return 0;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            return line != null ? Integer.parseInt(line) : 0;
        }
    }

    // Write counter value to file
    private static void writeCounterToFile(int value) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COUNTER_FILE))) {
            writer.write(Integer.toString(value));
        }
    }

}

