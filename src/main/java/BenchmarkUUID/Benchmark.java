package BenchmarkUUID;

import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Benchmark {

    private static final String URL = "jdbc:mysql://localhost:3306/shard3";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Benchmark UUID Insertion
            System.out.println("started for uuid insertion");
            long startTime= System.currentTimeMillis();
            benchmarkInsertion(conn, "UUIDTable", false);
            long endTime = System.currentTimeMillis();
            System.out.println("time taken"+(endTime-startTime));

            // Benchmark Custom ID Insertion
            System.out.println("started for custom id insertion");
            long startTime1= System.currentTimeMillis();
            benchmarkCustomIdInsertion(conn, "CustomIDTable");
            long endTime1 = System.currentTimeMillis();
            System.out.println("time taken"+(endTime1-startTime1));

        }
    }

    private static void benchmarkInsertion(Connection conn, String tableName, boolean useAutoIncrement) throws SQLException {
        String sql = useAutoIncrement
                ? "INSERT INTO " + tableName + " (name) VALUES (?)"
                : "INSERT INTO " + tableName + " (id, name) VALUES (UUID(), ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            Instant start = Instant.now();
            for (int i = 0; i < 1000000; i++) {
                System.out.println("inserted"+i);
                pstmt.setString(1, "Name" + i);
                pstmt.executeUpdate();
            }
            Instant end = Instant.now();
            System.out.println("Insertion into " + tableName + " took " + Duration.between(start, end).toMillis() + " milliseconds.");
        }
    }

    private static void benchmarkCustomIdInsertion(Connection conn, String tableName) throws SQLException {
        String sql = "INSERT INTO " + tableName + " (id, name) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            Instant start = Instant.now();
            for (int i = 0; i < 1000000; i++) {
                int customId = generateCustomId();
                pstmt.setInt(1, customId);
                pstmt.setString(2, "Name" + i);
                System.out.println("inserted"+i);
                pstmt.executeUpdate();
            }
            Instant end = Instant.now();
            System.out.println("Insertion into " + tableName + " with custom ID took " + Duration.between(start, end).toMillis() + " milliseconds.");
        }
    }

    private static int generateCustomId() {
        // Example of custom ID generation: Timestamp + Counter
        long timestamp = System.currentTimeMillis();
        return counter.getAndIncrement(); // Simple example
    }
}
