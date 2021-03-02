import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;
import java.util.Map;

@DynamoDBTable(tableName = "Game")
public class Game {

    private String gameId;
    private int boardRows;
    private int boardColumns;
    private Map<String, List<String>> grid;
    private Map<String, String> players;
    private boolean freeGame;
    private String tokenForTurn;
    private int connectionNumber;
    private String winner;


    public Game() {
        // Default constructor is required by AWS DynamoDB SDK
    }

    public Game(int boardRows, int boardColumns, Map<String, List<String>> grid, Map<String, String> player, String tokenForTurn) {
        this.boardRows = boardRows;
        this.boardColumns = boardColumns;
        this.grid = grid;
        this.players = player;
        this.freeGame = true;
        this.tokenForTurn = tokenForTurn;
        this.connectionNumber = 3;
        this.winner = null;
    }

    public Map<String, List<String>> adddisk (int column, String name) {
        String disk = this.getPlayerdisk(name);
        for (int rowNum = boardRows - 1; rowNum >= 0; rowNum--) {
            List<String> row = this.grid.get(String.valueOf(rowNum));
            if (row.get(column).equals("*")) {
                row.set(column, disk);
                this.grid.put(String.valueOf(rowNum), row);
                return this.grid;
            }
        }
        return null;
    }

    public boolean validMove (int column) {
        for (int rowNum = boardRows - 1; rowNum >= 0; rowNum--) {
            List<String> row = this.grid.get(String.valueOf(rowNum));
            if (row.get(column).equals("*")) {
                return true;
            }
        }
        return false;
    }

    public boolean isGameOver() {
        return check5Vertically() || check5Across() || check5DiagonalPositive() || check5DiagonalNegative();
    }

    private boolean check5Across() {
        String disk = this.getPlayerdisk(this.tokenForTurn);
        for (int rowNum = boardRows - 1; rowNum >= 0; rowNum--) {
            List<String> row = this.grid.get(String.valueOf(rowNum));
            int disksInARow = 0;
            for (int colNum = 0; colNum < row.size(); colNum++) {
                if (row.get(colNum).equals(disk)) {
                    disksInARow++;
                } else {
                    disksInARow = 0;
                }
                if (disksInARow == this.connectionNumber) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean check5Vertically() {
        String disk = this.getPlayerdisk(this.tokenForTurn);
        for (int colNum = 0; colNum < boardColumns; colNum++) {
            int disksInARow = 0;
            for (int rowNum = boardRows - 1; rowNum >= 0; rowNum--) {
                List<String> row = this.grid.get(String.valueOf(rowNum));
                if (row.get(colNum).equals(disk)) {
                    disksInARow++;
                } else {
                    disksInARow = 0;
                }
                if (disksInARow == this.connectionNumber) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean check5DiagonalPositive() {
        String disk = this.getPlayerdisk(this.tokenForTurn);
        for (int row = this.connectionNumber - 1; row < boardRows; row++) {
            int disksInARow = 0;
            for (int col = 0; col < row + 1; col++) {
                List<String> rowList = this.grid.get(String.valueOf(row - col));
                if (rowList.get(col).equals(disk)) {
                    disksInARow++;
                } else {
                    disksInARow = 0;
                }
                if (disksInARow == this.connectionNumber) {
                    return true;
                }
            }
        }

        for (int col = 1; col < boardColumns - (this.connectionNumber - 1); col++) {
            int disksInARow = 0;
            for (int row = 0; row < boardRows; row++) {
                if ( (col + row) < boardRows) {
                    List<String> rowList = this.grid.get(String.valueOf((boardRows - 1) - row));
                    if (rowList.get(row+col).equals(disk)) {
                        disksInARow++;
                    } else {
                        disksInARow = 0;
                    }
                    if (disksInARow == this.connectionNumber) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean check5DiagonalNegative() {
        String disk = this.getPlayerdisk(this.tokenForTurn);
        for (int row = 0; row < boardRows - this.connectionNumber - 1; row++) {
            int disksInARow = 0;
            for (int col = 0; col < boardRows - row; col++) {
                List<String> rowList = this.grid.get(String.valueOf(row + col));
                if (rowList.get(col).equals(disk)) {
                    disksInARow++;
                } else {
                    disksInARow = 0;
                }
                if (disksInARow == this.connectionNumber) {
                    return true;
                }
            }
        }

        for (int col = 1; col < boardColumns - (this.connectionNumber - 1); col++) {
            int disksInARow = 0;
            for (int row = 0; row < boardRows; row++) {
                if ( (col + row) < boardRows) {
                    List<String> rowList = this.grid.get(String.valueOf(row));
                    if (rowList.get(row+col).equals(disk)) {
                        disksInARow++;
                    } else {
                        disksInARow = 0;
                    }
                    if (disksInARow == this.connectionNumber) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    public String getId() {
        return gameId;
    }

    public void setId(String id) {
        this.gameId = id;
    }

    @DynamoDBAttribute
    public int getboardRows() {
        return boardRows;
    }

    public void setboardRows(int boardRows) {
        this.boardRows = boardRows;
    }

    @DynamoDBAttribute
    public int getboardColumns() {
        return boardColumns;
    }

    public void setboardColumns(int boardColumns) {
        this.boardColumns = boardColumns;
    }

    @DynamoDBAttribute
    public Map<String, List<String>> getGrid() {
        return grid;
    }

    public void setGrid(Map<String, List<String>> grid) {
        this.grid = grid;
    }

    @DynamoDBAttribute
    public Map<String, String> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, String> players) {
        this.players = players;
    }

    public void addPlayer(String name, String disk) {
        this.players.put(name, disk);
    }

    @DynamoDBAttribute
    public boolean isfreeGame() {
        return freeGame;
    }

    public void setfreeGame(boolean freeGame) {
        this.freeGame = freeGame;
    }

    @DynamoDBAttribute
    public String gettokenForTurn() {
        return tokenForTurn;
    }

    public void settokenForTurn(String tokenForTurn) {
        this.tokenForTurn = tokenForTurn;
    }

    public String getPlayerdisk(String name) {
        return this.players.get(name);
    }

    @DynamoDBAttribute
    public int getconnectionNumber() {
        return connectionNumber;
    }

    public void setconnectionNumber(int connectionNumber) {
        this.connectionNumber = connectionNumber;
    }

    @DynamoDBAttribute
    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}