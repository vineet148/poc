package BloggingApp;

public class ShardUtils {

    public static String getShard(int userId) {
        int shardNumber = (userId / 1000) + 1; // Simple shard logic
        return "shard" + shardNumber; // Example shard naming
    }
}
