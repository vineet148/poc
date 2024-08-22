package BloggingApp;

public class BlogApp {

    public static void main(String[] args) {
        // Initialize the databases (create tables if not exist)
        for (int i = 1; i <= 3; i++) {
          //  DatabaseUtils.initializeDatabase("shard" + i);
        }

        // Example user ID and blog post data
        int userId = 1234; // Example user ID
        String title = "My First Blog Post";
        String content = "This is the content of the blog post.";
        System.out.println("started inserting");
        long curremtTime= System.currentTimeMillis();
        // Insert a blog post
        for(int i=100;i<1100;i++) {
            DatabaseUtils.insertBlogPost(i, title, content);
        }
        long currenTime2= System.currentTimeMillis();
        int timeDifference = (int) (currenTime2-curremtTime);
        System.out.println("time taken"+timeDifference);
        System.out.println("Blog post inserted successfully.");
    }
}