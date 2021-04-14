import java.util.Random;

public enum BubbleStatus {
    SGONFIA,
    GONFIA_A_META,
    PER_ESPLODERE;

    private static final BubbleStatus[] status = BubbleStatus.values();
    private static final Random random = new Random();

    public static BubbleStatus randomStatus() {
        return status[random.nextInt(status.length)];
    }
}