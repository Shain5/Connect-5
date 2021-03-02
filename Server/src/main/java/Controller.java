import Game.Game;
import Game.GameRepository;
import Player.Player;
import Player.playerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class Controller {
    @Autowired
    playerRepository playerRepository;

    @Autowired
    GameRepository gameRepository;

    @GetMapping(value = "/{name}", produces = "application/json")
    public Player getPlayer(@PathVariable String name) {
        return playerRepository.findFirstByName(name);
    }

    @PostMapping(value = "joinGame/{name}", consumes = "application/json", produces = "application/json")
    public Game joinGame(@RequestBody Player newPlayer) {

        Game freeGameGame = gameRepository.findFirstByFreeTrue();
        System.out.println("freeGameGame " + freeGameGame);
        Gson basicGson = new Gson();
        if (freeGameGame != null && freeGameGame.getPlayers().get(newPlayer.getName()) == null) {
            newPlayer.setdisk("o");
            freeGameGame.setfreeGame(false);
            freeGameGame.addPlayer(newPlayer.getName(), newPlayer.getdisk());
            gameRepository.save(freeGameGame);

            Iterable<Game> games = gameRepository.findAll();
            for (Game gameObject : games) {
                System.out.println("Game object: " + basicGson.toJson(gameObject));
            }

            return freeGameGame;
        } else {
            newPlayer.setdisk("x");
            Board board = new Board(6, 9);

            int boardRows = board.getHeight();
            int boardColumns = board.getWidth();
            Map<String, List<String>> grid = board.getGrid();
            Map<String, String> p1Map = new HashMap<>();
            p1Map.put(newPlayer.getName(), newPlayer.getdisk());

            String tokenForTurn = newPlayer.getName();

            Game newGame = new Game(boardRows, boardColumns, grid, p1Map, tokenForTurn);

            newGame = gameRepository.save(newGame);

            System.out.println("newGame " + newGame.getId());
            Optional<Game> gameQuery = gameRepository.findById(newGame.getId());
            Game presentGame = gameQuery.isPresent() ? gameQuery.get() : null;

            System.out.println();
            System.out.println("Queried object: " + basicGson.toJson(presentGame));

            Iterable<Game> games = gameRepository.findAll();
            for (Game gameObject : games) {
                System.out.println("Game object: " + basicGson.toJson(gameObject));
            }

            return newGame;
        }
    }

    @GetMapping(value = "/checkTurn/{gameId}/{name}")
    public Game checkTurn(@PathVariable String gameId, @PathVariable String name) {
        Game game = gameRepository.findById(gameId).orElse(null);
        if (game != null) {
            return game;
        }
        return null;
    }

    @PutMapping(value = "/makeMove/{gameId}/{name}/{column}")
    public Game makeMove(@PathVariable String gameId, @PathVariable String name, @PathVariable Integer column) {
        Game game = gameRepository.findById(gameId).orElse(null);
        if (game != null) {
            Map<String, List<String>> diskAdded = game.adddisk(column, name);
            if (game.isGameOver()){
                game.setWinner(game.gettokenForTurn());
                gameRepository.save(game);
                return game;
            }
            if (diskAdded != null) {
                for (Map.Entry<String,String> entry : game.getPlayers().entrySet()) {
                    if (!entry.getKey().equals(name)) {
                        game.settokenForTurn(entry.getKey());
                    }
                }
                gameRepository.save(game);
                return game;
            }
        }
        return null;
    }
}
