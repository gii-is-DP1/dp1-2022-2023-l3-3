package sevenislands.game.turn;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.card.Card;
import sevenislands.card.CardService;
import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.Game;
import sevenislands.game.GameService;
import sevenislands.game.island.Island;
import sevenislands.game.island.IslandService;
import sevenislands.game.round.Round;
import sevenislands.game.round.RoundService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.treasure.Treasure;
import sevenislands.treasure.TreasureService;
import sevenislands.user.User;

@Service
public class TurnService {

    private TurnRepository turnRepository;
    private final GameService gameService;
    private final RoundService roundService;
    private final LobbyService lobbyService;
    private final TreasureService treasureService;
    private final IslandService islandService;
    private final CardService cardService;

    @Autowired
    public TurnService(TurnRepository turnRepository, GameService gameService, RoundService roundService,
            LobbyService lobbyService, IslandService islandService, TreasureService treasureService,
            CardService cardService) {
        this.turnRepository = turnRepository;
        this.gameService = gameService;
        this.roundService = roundService;
        this.lobbyService = lobbyService;
        this.treasureService = treasureService;
        this.islandService = islandService;
        this.cardService = cardService;
    }

    // No se usa en ningún lado
    @Transactional(readOnly = true)
    public List<Turn> findAllTurns() throws DataAccessException {
        return turnRepository.findAll();
    }

    // No se usa en ningún lado
    @Transactional(readOnly = true)
    public Optional<Turn> findTurnById(int id) throws DataAccessException {
        return turnRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Turn> findByRoundId(Integer id) throws DataAccessException {
        return turnRepository.findByRoundId(id);
    }
    @Transactional
    public List<Integer> IslandToChoose(Turn turn,String nickName){
        List<Integer> islas=new ArrayList<>();
        Map<Treasure, Integer> playerCardsMap=findPlayerCardsLastTurn(nickName);
        if(turn.getDice()==null){
            islas.add(0);
            
        }else{
            for(int i=1; i<=(turn.getDice()+playerCardsMap.values().stream().mapToInt(x->x).sum());i++){
                islas.add(i);
        }
    }
        return islas;
    }

    @Transactional
    public void save(Turn turn) {
        turnRepository.save(turn);
    }

    @Transactional
    public void initTurn(User logedUser, Round round, List<User> userList, List<Treasure> treasures) {
        Turn turn = new Turn();
        Integer nextUser = (userList.indexOf(logedUser) + 1) % userList.size();
        turn.setStartTime(LocalDateTime.now());
        turn.setRound(round);
        turn.setUser(userList.get(nextUser));
        List<Treasure> treasureList = new ArrayList<>();
        if (treasures == null) {
            Turn prevTurn = turnRepository.findTurnByNickname(userList.get(nextUser).getNickname()).get(0);
            treasureList.addAll(prevTurn.getTreasures());
        } else
            treasureList.addAll(treasures);
        turn.setTreasures(treasureList);
        save(turn);
    }


    @Transactional
    public void dice(Turn turn) {
        Random randomGenerator = new Random();
        Integer dice = randomGenerator.nextInt(6) + 1;
        turn.setDice(dice);
        save(turn);
    }

    @Transactional
    public void assignTurn(User logedUser, Optional<Game> game, List<User> userList, List<Round> roundList) {
        Round round = new Round();
        round.setGame(game.get());
        if (roundService.findRoundsByGameId(game.get().getId()).isEmpty()) {
            dealtreasures(logedUser, game, userList, roundList);
            Integer prevUser = userList.indexOf(logedUser) == 0 ? userList.size() - 1 : userList.indexOf(logedUser) - 1;
            roundService.save(round);
            initTurn(userList.get(prevUser), round, userList, null);
        } else if (findByRoundId(roundList.get(roundList.size() - 1).getId()).size() >= userList.size()) {
            roundService.save(round);
            initTurn(logedUser, round, userList, null);
        }
    }

    @Transactional
    public void dealtreasures(User logedUser, Optional<Game> game, List<User> userList, List<Round> roundList) {
        Round round = new Round();
        cardService.initGameCards(game.get());
        Treasure doblon = treasureService.findTreasureByName("doblon").get();
        List<Treasure> treasureList = new ArrayList<>();
        round.setGame(game.get());
        treasureList.add(doblon);
        treasureList.add(doblon);
        treasureList.add(doblon);
        roundService.save(round);
        for (Integer i = 0; i < userList.size(); i++) {
            User user = userList.get((userList.indexOf(logedUser) + i) % userList.size());
            Turn turn = new Turn();
            turn.setRound(round);
            turn.setStartTime(LocalDateTime.now());
            turn.setUser(user);
            turn.setTreasures(treasureList);
            Card doblones = cardService.findCardByGameAndTreasure(game.get().getId(), "doblon");
            doblones.setMultiplicity(doblones.getMultiplicity() - 3);
            cardService.save(doblones);
            save(turn);
        }
        for (Integer i = 0; i < 6; i++) {
            Island island = new Island();
            island.setNum(i + 1);
            island.setGame(game.get());
            List<Card> allCards = cardService.findAllCardsByGameId(game.get().getId());
            List<Integer> cardList = new ArrayList<>();
            for (Card c : allCards) {
                for (Integer j = 0; j < c.getMultiplicity(); j++) {
                    cardList.add(allCards.indexOf(c));
                }
            }
            Random randomGenerator = new Random();
            Integer randomCardNumber = randomGenerator.nextInt(cardList.size() - 1);
            Card selectedCard = allCards.get(cardList.get(randomCardNumber));
            Treasure selectedTreasure = selectedCard.getTreasure();
            island.setTreasure(selectedTreasure);
            selectedCard.setMultiplicity(selectedCard.getMultiplicity() - 1);
            cardService.save(selectedCard);
            islandService.save(island);
        }
    }

    @Transactional
    public void checkUserGame(User logedUser) throws NotExistLobbyException {
        try {
            if (gameService.findGameByNickname(logedUser.getNickname()).isPresent()) {

                // TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que
                // existe
                Lobby lobby = lobbyService.findLobbyByPlayerId(logedUser.getId());
                Optional<Game> game = gameService.findGameByNickname(logedUser.getNickname());
                List<User> userList = lobby.getPlayerInternal();
                List<Round> roundList = roundService.findRoundsByGameId(game.get().getId()).stream()
                        .collect(Collectors.toList());
                if (roundList.size() != 0) {
                    Round lastRound = roundList.get(roundList.size() - 1);
                    List<Turn> turnList = findByRoundId(lastRound.getId());
                    Turn lastTurn = turnList.get(turnList.size() - 1);
                    if (lastTurn.getUser().getId() == logedUser.getId() && userList.size() > 1) {
                        initTurn(logedUser, lastRound, userList, null);
                    }
                }
                if (userList.size() == 1) {
                    game.get().setActive(false);
                    gameService.save(game.get());
                }
            }
        } catch (NotExistLobbyException e) {
            throw e;
        }
    }

    @Transactional
    public List<Turn> findTurnByNickname(String nickname) {
        return turnRepository.findTurnByNickname(nickname);
    }

    @Transactional
    public Map<Treasure, Integer> findPlayerCardsLastTurn(String nickname) {
        Turn lastPlayerTurn = turnRepository.findTurnByNickname(nickname).get(0);
        Map<Treasure, Integer> map = new HashMap<>();
        for (Treasure t : lastPlayerTurn.getTreasures()) {
            if (map.containsKey(t)) {
                Integer newValue = map.get(t) + 1;
                map.put(t, newValue);
            } else {
                map.put(t, 1);
            }
        }
        return map;
    }
}
