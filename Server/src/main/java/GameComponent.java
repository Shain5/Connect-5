
import Game.Game;
import Game.GameRepository;
import Player.Player;
import Player.playerRepository;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Profile("!test")
@Component
public class GameComponent implements CommandLineRunner {

    @Autowired
    AmazonDynamoDB amazonDynamoDB;

    @Autowired
    DynamoDBMapper dynamoDBMapper;

    @Autowired
    playerRepository playerRepository;

    @Autowired
    GameRepository gameRepository;

    @Override
    public void run(String... strings) throws Exception {

        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        createTable(Game.class);
        createTable(Player.class);


        System.out.println();

        // demoCustomInterface(playerRepository);
    }

    private void createTable(Class table) {
        CreateTableRequest tableRequest = dynamoDBMapper
                .generateCreateTableRequest(table);

        tableRequest.setProvisionedThroughput(
                new ProvisionedThroughput(1L, 1L));

        try{
            TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
        } catch (SdkClientException e){
            System.out.println("****************************");
            System.out.println();
            System.out.println("DynamoDB needs to be running");
            System.out.println();
            System.out.println("****************************");
            System.exit(1);
        }
    }

    private void demoCustomInterface(playerRepository playerRepository) {
        Player player = new Player("Shain Sutton", "x");
        playerRepository.save(player);

        String PlayerId = player.getId();

        Optional<Player> playerQuery = playerRepository.findById(PlayerId);

        System.out.println();
        if (playerQuery.get() != null) {
            System.out.println("Queried object: " + new Gson().toJson(playerQuery.get()));
        }

        Iterable<Player> players = playerRepository.findAll();

        System.out.println();
        for (Player playerObject : players) {
            System.out.println("List object: " + new Gson().toJson(playerObject));
        }

        Board board = new Board(6, 9);
        int boardRows = board.getHeight();
        int boardColumns = board.getWidth();
        Map<String, List<String>> grid = board.getGrid();
        Map<String, String> p1Map = new HashMap<>();
        p1Map.put(player.getName(), player.getdisk());
        String tokenForTurn = player.getName();

        Game newGame = new Game(boardRows, boardColumns, grid,  p1Map, tokenForTurn);

        gameRepository.save(newGame);

        String gameId = newGame.getId();

        System.out.println("Game Id " + gameId);

        Optional<Game> gameQuery = gameRepository.findById(gameId);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Gson basicGson = new Gson();

        System.out.println();
        if (gameQuery.get() != null) {
            System.out.println("Queried object: " + gson.toJson(gameQuery.get()));
        }

        Iterable<Game> games = gameRepository.findAll();

        System.out.println();
        for (Game gameObject : games) {
            System.out.println("Game object: " + basicGson.toJson(gameObject));
        }
    }
}
