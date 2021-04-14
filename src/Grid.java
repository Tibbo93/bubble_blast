import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;


public class Grid {

    private final int rows;
    private final int cols;
    private final Bubble[][] grid;
    private int bubbleCounter;

    public Grid(int rows, int cols) {

        Bubble[][] grid = IntStream.range(0, 5)
                .mapToObj(x -> IntStream.range(0, 6)
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
        StringBuilder s = new StringBuilder("\t\t 1\t  2\t   3\t4\t 5\t  6\n\n");
        AtomicInteger index = new AtomicInteger(1);

        Arrays.stream(this.grid)
                .forEach(i -> {
                    s.append("\n\n").append(index.getAndIncrement()).append("\t\t");
                    Arrays.stream(i)
                            .forEach(j -> s.append(Bubble.ToStringStatus(j)));
                });

        return s.toString();
    }

    public void performMove(int x, int y, Direction direction) {
        //mossa valida
        if (this.moveIsValid(x, y)) {
            //caso ricorsivo - propagazione
            if (this.getBubble(x, y) == null) {
                switch (direction) {
                    case LEFT -> performMove(x, y - 1, Direction.LEFT);
                    case RIGHT -> performMove(x, y + 1, Direction.RIGHT);
                    case UP -> performMove(x - 1, y, Direction.UP);
                    case DOWN -> performMove(x + 1, y, Direction.DOWN);
                }
            }
            //cosa ricorsivo - esplosione
            else if (this.getBubble(x, y).getStatus() == BubbleStatus.PER_ESPLODERE) {
                //this.grid.get(x).remove(y);
                this.grid[x][y] = null;
                BubbleBlast.writeOnFile("\t\t\tBubble (" + (x + 1) + "," + (y + 1) + ") change status: SGONFIA  --->  ESPLOSA\n");
                this.bubbleCounter--;
                performMove(x, y - 1, Direction.LEFT);
                performMove(x, y + 1, Direction.RIGHT);
                performMove(x - 1, y, Direction.UP);
                performMove(x + 1, y, Direction.DOWN);
            }
            //caso base
            else if (this.getBubble(x, y).getStatus() == BubbleStatus.GONFIA_A_META) {
                this.getBubble(x, y).setStatus(BubbleStatus.PER_ESPLODERE);
                BubbleBlast.writeOnFile("\t\t\tBubble (" + (x + 1) + "," + (y + 1) + ") change status: GONFIA_A_META  --->  PER_ESPLODERE\n");
            }
            //caso base
            else {
                this.getBubble(x, y).setStatus(BubbleStatus.GONFIA_A_META);
                BubbleBlast.writeOnFile("\t\t\tBubble (" + (x + 1) + "," + (y + 1) + ") change status: SGONFIA  --->  GONFIA_A_META\n");
            }
        }
    }

    public boolean moveIsValid(int x, int y) {
        return !(x < 0 || x > this.rows - 1 || y < 0 || y > this.cols - 1);
    }
}
