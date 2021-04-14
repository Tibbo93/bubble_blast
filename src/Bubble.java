public class Bubble {

    private final int x;
    private final int y;
    private BubbleStatus status;

    public Bubble(int x, int y, BubbleStatus status) {
        this.x = x;
        this.y = y;
        this.status = status;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public BubbleStatus getStatus() {
        return this.status;
    }

    public void setStatus(BubbleStatus status) {
        this.status = status;
    }
}
