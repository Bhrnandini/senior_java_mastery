package chapter1;

public class PartitionHashingTrap {

    private static final int PARTITION_COUNT = 12;

    public static void main(String[] args) {
        // Real English word whose Java String.hashCode() equals Integer.MIN_VALUE (-2147483648)
        String poisonKey = "polygenelubricants";

        int rawHash = poisonKey.hashCode();
        System.out.println("Key: " + poisonKey);
        System.out.println("Raw hashCode: " + rawHash);
        System.out.println("Math.abs(rawHash): " + Math.abs(rawHash));

        // 1. Naive routing (Crashes in production when hash == Integer.MIN_VALUE)
        try {
            int badPartition = naivePartition(poisonKey, PARTITION_COUNT);
            System.out.println("Naive partition: " + badPartition);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("CRASH in naive routing -> " + e);
        }

        // 2. Production-grade Kafka/Shard routing (Bitwise mask clears sign bit)
        int safePartition = safePartition(poisonKey, PARTITION_COUNT);
        System.out.println("Safe partition (Bitwise & 0x7fffffff): " + safePartition);
    }

    private static int naivePartition(String key, int numPartitions) {
        int[] partitions = new int[numPartitions];
        int index = Math.abs(key.hashCode()) % numPartitions;
        return partitions[index]; // Throws ArrayIndexOutOfBoundsException for index -8
    }

    private static int safePartition(String key, int numPartitions) {
        // Apache Kafka's Utils.toPositive() implementation
        int positiveHash = key.hashCode() & 0x7fffffff;
        return positiveHash % numPartitions;
    }
}
