package BloggingApp;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseUtils {

    private static final String DB_URL_TEMPLATE = "jdbc:mysql://localhost:3306/%s?serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public static Connection getConnection(String shard) throws SQLException {
        String url = String.format(DB_URL_TEMPLATE, shard);
        return DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
    }

    public static void initializeDatabase(String shard) {
        String url = String.format(DB_URL_TEMPLATE, shard);
        try (Connection conn = DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
             Reader reader = new FileReader(new File("src/resources/create_tables.sql"));
             PreparedStatement pstmt = conn.prepareStatement(readSQLScript(reader))) {
            pstmt.execute();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    private static String readSQLScript(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[1024];
        int bytesRead;
        while ((bytesRead = reader.read(buffer)) != -1) {
            sb.append(buffer, 0, bytesRead);
        }
        return sb.toString();
    }

    public static void insertBlogPost(int userId, String title, String content) {
        String shard = ShardUtils.getShard(userId);
        String postId = UniqueIDGenerator.generateUniqueId();

        String sql = "INSERT INTO BlogPosts (post_id, user_id, title, content, created_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(shard);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, postId);
            pstmt.setInt(2, userId);
            pstmt.setString(3, title);
            pstmt.setString(4, content);
            pstmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
