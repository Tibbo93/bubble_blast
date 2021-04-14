import java.util.Scanner;

public class Prova {

    public static void main(String[] args) {

        BubbleBlast game = new BubbleBlast(new Grid(5, 6));
        BubbleBlast.writeOnFile("BUBBLE BLAST\n\nHistory game:\n");

        System.out.println("Legend:\n\t- (3): SGONFIA\n\t- (2): GONFIA_A_META\n\t- (1): PER_ESPLODERE\n\t- (_): VUOTO\n\n");

        Grid grid = game.getGrid();
        int movesCounter = 0, bubblesCounter = grid.getBubbleCounter();
        while (bubblesCounter > 0) {

            System.out.println("Bubbles left: " + bubblesCounter + "\n\n");
            System.out.println(grid);

            System.out.print("\nMake your move (es. x,y): ");
            Scanner input = new Scanner(System.in);

            try {
                String[] moves = input.nextLine().split(",");
                if (moves.length != 2) {
                    System.out.println("ERROR: Invalid move!!");
                    continue;
                }
                int x = Integer.parseInt(moves[0]) - 1, y = Integer.parseInt(moves[1]) - 1;
                if (grid.moveIsValid(x, y) && grid.getBubble(x, y) != null) {
                    BubbleBlast.writeOnFile("\t" + (++movesCounter) + ") Move: " + (x + 1) + "," + (y + 1) + "\n\t\tResult:\n");
                    grid.performMove(x, y, Direction.NONE);
                } else {
                    System.out.println("ERROR: Invalid move!!");
                }
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Invalid move!!");
            }

            bubblesCounter = grid.getBubbleCounter();
        }
        System.out.println("\n\n" + grid + "\n\nCONGRATULATIONS!! YOU WIN");
        BubbleBlast.closeFile();
    }
}
