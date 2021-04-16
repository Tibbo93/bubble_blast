import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Grid {

    private final int rows;
    private final int cols;
    private final Bubble[][] grid;
    private int bubbleCounter;

    public Grid(int rows, int cols) {

        Bubble[][] grid = IntStream.range(0, rows)
                .mapToObj(x -> IntStream.range(0, cols)
                        .mapToObj(i -> new Bubble(x, i, BubbleStatus.randomStatus()))
                        .toArray(Bubble[]::new))
                .toArray(Bubble[][]::new);
        this.rows = rows;
        this.cols = cols;
        this.grid = grid;
        this.bubbleCounter = rows * cols;
    }

    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    public Bubble[][] getGrid() {
        return this.grid;
    }

    public int getBubbleCounter() {
        return this.bubbleCounter;
    }

    public Bubble getBubble(int x, int y) {
        return this.grid[x][y];
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("\t\t 1\t  2\t   3\t4\t 5\t  6\n");
        AtomicInteger index = new AtomicInteger(1);
        Arrays.stream(this.grid)
                .forEach(r -> {
                    s.append("\n\n").append(index.getAndIncrement()).append("\t\t");
                    Arrays.stream(r)
                            .forEach(bubble -> s.append(Bubble.ToStringStatus(bubble)));
                });
        return s.toString();
    }

    public void performMove(int x, int y, Direction direction) {

        if (this.moveIsValid(x, y)) {
            if (this.getBubble(x, y) == null) {
                switch (direction) {
                    case LEFT -> performMove(x, y - 1, Direction.LEFT);
                    case RIGHT -> performMove(x, y + 1, Direction.RIGHT);
                    case UP -> performMove(x - 1, y, Direction.UP);
                    case DOWN -> performMove(x + 1, y, Direction.DOWN);
                }
            } else if (this.getBubble(x, y).getStatus() == BubbleStatus.PER_ESPLODERE) {
                this.grid[x][y] = null;
                BubbleBlast.writeOnFile("\t\t\tBubble (" + (x + 1) + "," + (y + 1) + ") change status: SGONFIA  --->  ESPLOSA\n");
                this.bubbleCounter--;
                performMove(x, y - 1, Direction.LEFT);
                performMove(x, y + 1, Direction.RIGHT);
                performMove(x - 1, y, Direction.UP);
                performMove(x + 1, y, Direction.DOWN);
            } else if (this.getBubble(x, y).getStatus() == BubbleStatus.GONFIA_A_META) {
                this.getBubble(x, y).setStatus(BubbleStatus.PER_ESPLODERE);
                BubbleBlast.writeOnFile("\t\t\tBubble (" + (x + 1) + "," + (y + 1) + ") change status: GONFIA_A_META  --->  PER_ESPLODERE\n");
            } else {
                this.getBubble(x, y).setStatus(BubbleStatus.GONFIA_A_META);
                BubbleBlast.writeOnFile("\t\t\tBubble (" + (x + 1) + "," + (y + 1) + ") change status: SGONFIA  --->  GONFIA_A_META\n");
            }
        }
    }

    public boolean moveIsValid(int x, int y) {
        return !(x < 0 || x > this.rows - 1 || y < 0 || y > this.cols - 1);
    }

    public int[] bestMove() {

        int indexStatus = 0;
        boolean found = false;
        Optional<int[]> c = null;
        while (!found) {
            int tempIndexStatus = indexStatus;
            c = Arrays.stream(this.grid)
                    .flatMap(bubbles -> Arrays.stream(bubbles))
                    .filter(b -> b != null)
                    .filter(b -> b.getStatus() == BubbleStatus.values()[tempIndexStatus])
                    .map(b -> new int[]{
                            b.getX(),
                            b.getY(),
                            findBubblesReady2Explode(b.getX(), b.getY(), b.getStatus())
                    })
                    .max(Comparator.comparingInt(a -> a[2]));

            if (c != null)
                found = true;
            else
                indexStatus++;
        }
        return c.get();
    }

    public int findBubblesReady2Explode(int x, int y, BubbleStatus status) {

        List<Bubble> bubbleChecked = new ArrayList<Bubble>();
        return findBubblesReady2ExplodeRec(x, y, status, 0, bubbleChecked, Direction.NONE, 0);
    }

    public int findBubblesReady2ExplodeRec(int x, int y, BubbleStatus status, int level, List<Bubble> list, Direction direction, int counter) {
        if (moveIsValid(x, y)) {
            if (this.grid[x][y] == null) {
                switch (direction) {
                    case LEFT:
                        return countBubblesReady2Explode(x, y, status, list)
                                + findBubblesReady2ExplodeRec(x, y - 1, status, level, list, direction, counter);
                    case UP:
                        return countBubblesReady2Explode(x, y, status, list)
                                + findBubblesReady2ExplodeRec(x - 1, y, status, level, list, direction, counter);
                    case RIGHT:
                        return countBubblesReady2Explode(x, y, status, list)
                                + findBubblesReady2ExplodeRec(x, y + 1, status, level, list, direction, counter);
                    case DOWN:
                        return countBubblesReady2Explode(x, y, status, list)
                                + findBubblesReady2ExplodeRec(x + 1, y, status, level, list, direction, counter);
                    default:
                        return 0;
                }
            } else if (level == 2) {
                return 0;
            } else {
                return countBubblesReady2Explode(x, y, status, list)
                        + findBubblesReady2ExplodeRec(x, y - 1, status, level + 1, list, Direction.LEFT, counter)
                        + findBubblesReady2ExplodeRec(x - 1, y, status, level + 1, list, Direction.UP, counter)
                        + findBubblesReady2ExplodeRec(x, y + 1, status, level + 1, list, Direction.RIGHT, counter)
                        + findBubblesReady2ExplodeRec(x + 1, y, status, level + 1, list, Direction.DOWN, counter);
            }
        } else {
            return 0;
        }
    }

    public int countBubblesReady2Explode(int x, int y, BubbleStatus status, List<Bubble> list) {
        int counterBubblesReady2Explode = (int) Arrays.stream(this.grid)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .filter(b -> (b.getX() == x) && (b.getY() == (y - 1)) ||
                        ((b.getX() == (x - 1)) && (b.getY() == y)) ||
                        ((b.getX() == x) && (b.getY() == (y + 1))) ||
                        ((b.getX() == (x + 1)) && (b.getY() == y)))
                .filter(b -> b.getStatus() == status)
                .filter(b -> {
                    if (!list.stream().anyMatch(bl -> this.alreadyCounted(bl, b))) {
                        list.add(b);
                        return true;
                    }
                    return false;
                })
                .count();
        return counterBubblesReady2Explode;
    }

    public boolean alreadyCounted(Bubble b1, Bubble b2) {
        if (b1.getX() == b2.getX() && b1.getY() == b2.getY())
            return true;
        return false;
    }

    public void autosolve() {
        while (this.bubbleCounter > 0) {
            int[] move = this.bestMove();
            System.out.println("\nMove: " + (move[0] + 1) + "," + (move[1] + 1) + "\n");
            performMove(move[0], move[1], Direction.NONE);

            if (this.bubbleCounter > 0)
                System.out.println(this.toString() + "\n\n");
        }
    }
}
