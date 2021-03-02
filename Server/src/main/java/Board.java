import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Board {
    private int height;
    private int width;
    Map<String, List<String>> grid;
    private static final String defaultdisk = "*";

    public Board() {
        // Default constructor is required by AWS DynamoDB SDK
    }

    public Board(int height, int width) {
        this.height = height;
        this.width = width;
        this.grid = new TreeMap<>();
        this.StartBoard();
    }
    //method to initalise the board
    private void StartBoard(){
        for (int i = height - 1; i >= 0; i--) {
            List<String> row = new ArrayList<>();
            for (int j = width - 1; j >= 0; j--) {
                row.add(defaultdisk);
            }
            this.grid.put(String.valueOf(i), row);
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Map<String, List<String>> getGrid() {
        return grid;
    }

    public void setGrid(Map<String, List<String>> grid) {
        this.grid = grid;
    }
}
