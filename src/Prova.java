import java.util.Scanner;

public class Prova {

    public static void main(String[] args) {

        BubbleBlast game = new BubbleBlast();
        BubbleBlast.writeOnFile("BUBBLE BLAST\n\nHistory game:\n");

        System.out.println("Legend:\n\t- (3): SGONFIA\n\t- (2): GONFIA_A_META\n\t- (1): PER_ESPLODERE\n\t- (_): VUOTO\n\n");
        int movesCounter = 0;
        boolean play = true;
        while (play) {
            game.setGrid(new Grid(5, 6));
            BubbleBlast.writeOnFile("\n\n"+game.getGrid().toString()+"\n\n");
            System.out.println("\n\n");
            System.out.println(game.getGrid());

            Scanner input = new Scanner(System.in);
            String s;

            while (game.getGrid().getBubbleCounter() > 0) {
                System.out.println("\n\nBubbles left: " + game.getGrid().getBubbleCounter() + "\n\n");
                System.out.print("Make your move (es. x,y): ");
                s = input.nextLine();
                System.out.println("\n");

                if (s.equals("tip")) {
                    int[] move = game.getGrid().bestMove();
                    System.out.println("Best move: " + (move[0] + 1) + "," + (move[1] + 1) + "\n\n");
                } else if (s.equals("solve")) {
                    game.getGrid().autosolve();
                } else {
                    String[] move = s.strip().split(",");
                    try {
                        if (move.length != 2) {
                            System.out.println("ERROR: Invalid move!!");
                            continue;
                        }
                        int x = Integer.parseInt(move[0]) - 1, y = Integer.parseInt(move[1]) - 1;
                        if (game.getGrid().moveIsValid(x, y) && game.getGrid().getBubble(x, y) != null) {
                            BubbleBlast.writeOnFile("\t" + (++movesCounter) + ") Move: " + (x + 1) + "," + (y + 1) + "\n\t\tResult:\n");
                            game.getGrid().performMove(x, y, Direction.NONE);
                        } else {
                            System.out.println("ERROR: Invalid move!!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("ERROR: Invalid move!!");
                    }
                    BubbleBlast.writeOnFile("\n\n"+game.getGrid().toString()+"\n\n");
                    System.out.println("\n\n");
                }
                System.out.println(game.getGrid());
            }
            System.out.println("\n\nCONGRATULATIONS!! YOU WIN\n\n");
            System.out.print("Do you wanna play again? (Type 'yes' or 'no'): ");
            play = input.nextLine().equals("yes");
        }
        BubbleBlast.closeFile();
    }
}
