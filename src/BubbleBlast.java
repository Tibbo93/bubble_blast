import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BubbleBlast {

    private final Grid grid;
    private File file;
    private static BufferedWriter bufferedWriter;

    public BubbleBlast(Grid grid) {
        this.grid = grid;

        try {
            File f = new File("bubble_blast_history_game.txt");
            if (f.exists())
                this.file = f;
            else if (f.createNewFile())
                this.file = f;
            bufferedWriter = new BufferedWriter(new FileWriter(this.file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Grid getGrid() {
        return grid;
    }

    public static void writeOnFile(String s) {
        try {
            bufferedWriter.write(s);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeFile() {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
