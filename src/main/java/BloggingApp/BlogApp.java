package BloggingApp;

public class BlogApp {

    public static void main(String[] args) {
        // Initialize the databases (create tables if not exist)
        for (int i = 1; i <= 3; i++) {
            DatabaseUtils.initializeDatabase("shard" + i);
        }

        // Example user ID and blog post data
        int userId = 1234; // Example user ID
        String title = "My First Blog Post";
        String content = "This is the content of the blog post.";

        // Insert a blog post
        DatabaseUtils.insertBlogPost(userId, title, content);
        System.out.println("Blog post inserted successfully.");
    }
}