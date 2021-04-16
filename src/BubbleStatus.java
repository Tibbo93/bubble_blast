import java.util.Random;

public enum BubbleStatus {
    PER_ESPLODERE,
    GONFIA_A_META,
    SGONFIA;

    private static final BubbleStatus[] status = BubbleStatus.values();
    private static final Random random = new Random();

    public static BubbleStatus randomStatus() {
        return status[random.nextInt(status.length)];
    }
}