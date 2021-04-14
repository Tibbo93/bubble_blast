public class Grid {

    private final int rows;
    private final int cols;
    private final Bubble[][] grid;
    private int bubbleCounter;

    public Grid(int rows, int cols) {
        Bubble[][] grid = new Bubble[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Bubble(r, c, BubbleStatus.randomStatus());
            }
        }

        this.rows = rows;
        this.cols = cols;
        this.grid = grid;
        this.bubbleCounter = rows * cols;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Bubble[][] getGrid() {
        return grid;
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
        int index = 1;

        for (Bubble[] row : this.grid) {
            s.append(index++).append("\t\t");
            for (Bubble b : row) {
                if (b == null) {
                    s.append("( )  ");
                } else {
                    switch (b.getStatus()) {
                        case SGONFIA -> s.append("(3)  ");
                        case GONFIA_A_META -> s.append("(2)  ");
                        case PER_ESPLODERE -> s.append("(1)  ");
                    }
                }
            }
            s.append("\n\n");
        }
        return s.toString();
    }

    public void performMove(int x, int y, Direction direction) {
        //mossa valida
        if (this.moveIsValid(x, y)) {
            //caso ricorsivo - propagazione
            if (this.grid[x][y] == null) {
                switch (direction) {
                    case LEFT -> performMove(x, y - 1, Direction.LEFT);
                    case RIGHT -> performMove(x, y + 1, Direction.RIGHT);
                    case UP -> performMove(x - 1, y, Direction.UP);
                    case DOWN -> performMove(x + 1, y, Direction.DOWN);
                }
            }
            //cosa ricorsivo - esplosione
            else if (this.grid[x][y].getStatus() == BubbleStatus.PER_ESPLODERE) {
                this.grid[x][y] = null;
                BubbleBlast.writeOnFile("\t\t\tBubble (" + (x + 1) + "," + (y + 1) + ") change status: SGONFIA\t\t--->\t\tESPLOSA\n");
                this.bubbleCounter--;
                performMove(x, y - 1, Direction.LEFT);
                performMove(x, y + 1, Direction.RIGHT);
                performMove(x - 1, y, Direction.UP);
                performMove(x + 1, y, Direction.DOWN);
            }
            //caso base
            else if (this.grid[x][y].getStatus() == BubbleStatus.GONFIA_A_META) {
                this.grid[x][y].setStatus(BubbleStatus.PER_ESPLODERE);
                BubbleBlast.writeOnFile("\t\t\tBubble (" + (x + 1) + "," + (y + 1) + ") change status: GONFIA_A_META\t\t--->\t\tPER_ESPLODERE\n");
            }
            //caso base
            else {
                this.grid[x][y].setStatus(BubbleStatus.GONFIA_A_META);
                BubbleBlast.writeOnFile("\t\t\tBubble (" + (x + 1) + "," + (y + 1) + ") change status: SGONFIA\t\t--->\t\tGONFIA_A_META\n");
            }
        }
    }

    public boolean moveIsValid(int x, int y) {
        return !(x < 0 || x > this.rows - 1 || y < 0 || y > this.cols - 1);
    }
}
