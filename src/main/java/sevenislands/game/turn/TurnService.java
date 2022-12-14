package sevenislands.game.turn;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.card.Card;
import sevenislands.card.CardService;
import sevenislands.enums.Tipo;
import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.Game;
import sevenislands.game.GameService;
import sevenislands.game.island.Island;
import sevenislands.game.island.IslandService;
import sevenislands.game.round.Round;
import sevenislands.game.round.RoundService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyService;
import sevenislands.user.User;

@Service
public class TurnService {

    private TurnRepository turnRepository;
    private final GameService gameService;
    private final RoundService roundService;
    private final LobbyService lobbyService;
    private final IslandService islandService;
    private final CardService cardService;

    @Autowired
    public TurnService(TurnRepository turnRepository, GameService gameService, RoundService roundService,
            LobbyService lobbyService, IslandService islandService,CardService cardService) {
        this.turnRepository = turnRepository;
        this.gameService = gameService;
        this.roundService = roundService;
        this.lobbyService = lobbyService;
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
    public List<Island> islandToChoose(Turn turn,String nickName, List<Island> islandList, HttpServletRequest request){
        List<Island> islands=new ArrayList<>();
        HttpSession session = request.getSession();
        Map<Card, Integer> selectedCards = (Map<Card,Integer>) session.getAttribute("selectedCards");
        Integer numSelectedCards = selectedCards.values().stream().mapToInt(x->x).sum();
        if(turn.getDice()!=null){
            Integer selectedIsland1=turn.getDice()-numSelectedCards;
            Integer selectedIsland2=turn.getDice()+numSelectedCards;
            if(numSelectedCards!=0) {
                if(selectedIsland1>0) islands.add(islandList.get(selectedIsland1-1));
                if(selectedIsland2<=islandList.size()) islands.add(islandList.get(selectedIsland2-1));
            } else islands.add(islandList.get(turn.getDice()-1));
            
        }
        return islands;
    }

    @Transactional
    public Turn save(Turn turn) {
        return turnRepository.save(turn);
    }

    @Transactional
    public Turn initTurn(User logedUser, Round round, List<User> userList, List<Card> cards) {
        Turn turn = new Turn();
        Integer nextUser = (userList.indexOf(logedUser) + 1) % userList.size();
        turn.setStartTime(LocalDateTime.now());
        turn.setRound(round);
        turn.setUser(userList.get(nextUser));
        List<Card> treasureList = new ArrayList<>();
        if (cards == null) {
            Optional<List<Turn>> turnList = turnRepository.findTurnByNickname(userList.get(nextUser).getNickname());
            if(turnList.isPresent()) {
                Turn prevTurn = turnList.get().get(0);
                treasureList.addAll(prevTurn.getCards());
            }
        } else
            treasureList.addAll(cards);
        turn.setCards(treasureList);
        save(turn);
        return turn;
    }


    @Transactional
    public Turn dice(Turn turn) {
        Random randomGenerator = new Random();
        Integer dice = randomGenerator.nextInt(6) + 1;
        turn.setDice(dice);
        return save(turn);
    }

    @Transactional
    public Turn assignTurn(User logedUser, Optional<Game> game, List<User> userList, List<Round> roundList) {
        Round round = new Round();
        Turn turn = null;
        round.setGame(game.get());
        if (roundService.findRoundsByGameId(game.get().getId()).isEmpty()) {
            dealtreasures(logedUser, game, userList, roundList);
            Integer prevUser = userList.indexOf(logedUser) == 0 ? userList.size() - 1 : userList.indexOf(logedUser) - 1; 
            roundService.save(round);
            turn = initTurn(userList.get(prevUser), round, userList, null);
        } else if (findByRoundId(roundList.get(roundList.size() - 1).getId()).size() >= userList.size()) {
            roundService.save(round);
            
            turn = initTurn(logedUser, round, userList, null);
        }
        return turn;
    }

    @Transactional
    public List<Island> dealtreasures(User logedUser, Optional<Game> game, List<User> userList, List<Round> roundList) {
        Round round = new Round();
        cardService.initGameCards(game.get());
        List<Card> treasureList = new ArrayList<>();
        round = initRound(round, game, treasureList);
        repartirCartaJugadoresIniciales(userList, logedUser, round, treasureList, game);
        return asignarCartasIslas(game);
        
    }

    public Round initRound(Round round, Optional<Game> game, List<Card> treasureList){
        round.setGame(game.get());
        return roundService.save(round);
    }

    public List<Turn> repartirCartaJugadoresIniciales(List<User> userList, User logedUser, Round round, List<Card> treasureList, Optional<Game> game){
        List<Turn> turnList = new ArrayList<>();
        treasureList.add(cardService.findCardByGameAndTreasure(game.get().getId(), Tipo.Doblon));
        treasureList.add(cardService.findCardByGameAndTreasure(game.get().getId(), Tipo.Doblon));
        treasureList.add(cardService.findCardByGameAndTreasure(game.get().getId(), Tipo.Doblon));
        for (Integer i = 0; i < userList.size(); i++) {
            User user = userList.get((userList.indexOf(logedUser) + i) % userList.size());
            Turn turn = new Turn();
            turn.setRound(round);
            turn.setStartTime(LocalDateTime.now());
            turn.setUser(user);
            turn.setCards(treasureList);
            Card doblones = cardService.findCardByGameAndTreasure(game.get().getId(), Tipo.Doblon);
            doblones.setMultiplicity(doblones.getMultiplicity() - 3);
            cardService.save(doblones);
            turnList.add(save(turn));
        }
        return turnList;
    }

    public List<Island> asignarCartasIslas(Optional<Game> game){
        List<Island> islandList = new ArrayList<>();
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
            Integer randomCardNumber = randomGenerator.nextInt(cardList.size());
            Card selectedCard = allCards.get(cardList.get(randomCardNumber));
            island.setCard(selectedCard);
            selectedCard.setMultiplicity(selectedCard.getMultiplicity() - 1);
            cardService.save(selectedCard);
            islandService.save(island);
            islandList.add(island);
        }
        return islandList;
    }

    

    @Transactional
    public Island refreshDesk(Integer idIsla,User logedUser, Optional<Game> game){
        Island islandRes;
        List<Card> allCards = cardService.findAllCardsByGameId(game.get().getId());
        List<Integer> cardList = new ArrayList<>();
        for (Card c : allCards) {
            for (Integer j = 0; j < c.getMultiplicity(); j++) {
                cardList.add(allCards.indexOf(c));
            }
        }

        List<Island> islandList = islandService.findIslandsByGameId(game.get().getId());
        if(cardList.size()!=0){
            Random randomGenerator = new Random();
            Integer randomCardNumber = randomGenerator.nextInt(cardList.size());
            Card selectedCard = allCards.get(cardList.get(randomCardNumber));
            Island island=islandList.get(idIsla-1);
            island.setCard(selectedCard);
            selectedCard.setMultiplicity(selectedCard.getMultiplicity() - 1);
            cardService.save(selectedCard);
            islandService.save(island);
            islandRes = island;
        }else {
            Island island=islandList.get(idIsla-1);
            island.setNum(0);
            islandService.save(island);
            islandRes = island;
        }
        return islandRes;
    }

    @Transactional
    public Optional<Game> checkUserGame(User logedUser) throws NotExistLobbyException {
        Optional<Game> game = null;
        try {
            if (gameService.findGameByNicknameAndActive(logedUser.getNickname(), true).isPresent()) {

                // TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que
                // existe
                Lobby lobby = lobbyService.findLobbyByPlayerId(logedUser.getId());
                game = gameService.findGameByNicknameAndActive(logedUser.getNickname(), true);
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
            return game;
        } catch (NotExistLobbyException e) {
            throw e;
        }
    }

    @Transactional
    public List<Turn> findTurnByNickname(String nickname) {
        Optional<List<Turn>> turnList = turnRepository.findTurnByNickname(nickname);
        if (turnList.isPresent()) {
            return turnList.get();
        } else {
            return new ArrayList<Turn>();
        }
    }

    @Transactional
    public Map<Card, Integer> findPlayerCardsLastTurn(String nickname) {
        Optional<List<Turn>> turnList = turnRepository.findTurnByNickname(nickname);
        Map<Card, Integer> map = new TreeMap<>();
        if(turnList.isPresent()) {
            Turn lastPlayerTurn = turnList.get().get(0);
            for (Card card : lastPlayerTurn.getCards()) {
                if (map.containsKey(card)) {
                    Integer newValue = map.get(card) + 1;
                    map.put(card, newValue);
                } else {
                    map.put(card, 1);
                }
            }
            
            
        }
        return map;
    }

    @Transactional
    public List<Card> findPlayerCardsPenultimateTurn(String nickname) {
        Optional<List<Turn>> turnList = turnRepository.findTurnByNickname(nickname);
        if(turnList.isPresent()) {
            Turn lastPlayerTurn = turnList.get().get(1);
            return lastPlayerTurn.getCards();
        }
        return new ArrayList<Card>();
    }

    @Transactional
    public Turn addCarta(Integer id,String nickname){
        Optional<List<Turn>> turnList = turnRepository.findTurnByNickname(nickname);
        Turn turn = null;
        if(turnList.isPresent()) {
            Turn lastPlayerTurn = turnList.get().get(0);
            List<Card> cartasLastTurn=lastPlayerTurn.getCards();
            Optional<Game> game=gameService.findGameByNicknameAndActive(nickname, true);
            Island island=islandService.findCardOfIsland(game.get().getId(),id);
            Card card=cardService.findCardById(island.getCard().getId());
            cartasLastTurn.add(card);
            lastPlayerTurn.setCards(cartasLastTurn);
            turnRepository.save(lastPlayerTurn);
            turn = lastPlayerTurn;
        }
        return turn;
    }

    @Transactional
    public Turn deleteCard(Integer id,String nickname){
        Optional<List<Turn>> turnList = turnRepository.findTurnByNickname(nickname);
        Turn turn = null;
        if(turnList.isPresent()) {
            Turn lastPlayerTurn = turnList.get().get(0);
            List<Card> cartasLastTurn=lastPlayerTurn.getCards();
            Card carta=cardService.findCardById(id);
            for(Card c:cartasLastTurn){
                if(c.getTipo().equals(carta.getTipo())){
                    cartasLastTurn.remove(c);
                    break;
                }
            }
            lastPlayerTurn.setCards(cartasLastTurn);
            turnRepository.save(lastPlayerTurn);
            turn = lastPlayerTurn;
        }
        return turn;
    }

    @Transactional
    public Turn addCardToUser(Integer id, User user){
        Optional<List<Turn>> turnList = turnRepository.findTurnByNickname(user.getNickname());
        Turn turn = null;
        if(turnList.isPresent()) {
            Turn lastPlayerTurn = turnList.get().get(0);
            List<Card> cartasLastTurn=lastPlayerTurn.getCards();
            Card card = cardService.findCardById(id);
            cartasLastTurn.add(card);
            lastPlayerTurn.setCards(cartasLastTurn);
            turnRepository.save(lastPlayerTurn);
            turn = lastPlayerTurn;
        }
        return turn;
    }

    @Transactional
    public Boolean endGame(Game game){
        List<Island> islandList = islandService.findIslandsByGameId(game.getId());
        Boolean res=true;
        for(Card i: cardService.findAllCardsByGameId(game.getId())){
            if(!(i.getMultiplicity().equals(0))){
                res=false;
            }
        }
        for(Island island:islandList){
            if(!(island.getNum().equals(0))){
                res=false;
            }
        }
        return res;
    }

    @Transactional
    public Integer findTotalTurnsByNickname(String nickname) {
        return turnRepository.findTotalTurnsByNickname(nickname);
    }

    @Transactional
    public Integer turnCount() {
        return (int) turnRepository.count();
    }

    @Transactional
    public void changeCard(Integer id, User logedUser, Integer mode, HttpServletRequest request) {
        Card card = cardService.findCardById(id);
        HttpSession session = request.getSession();
        Map<Card,Integer> selectedCards = new TreeMap<>();

        selectedCards = (Map<Card,Integer>) session.getAttribute("selectedCards");
        List<Card> cards = selectedCards.keySet().stream().collect(Collectors.toList());
        
        switch (mode) {
            case 0:
                if(cards.stream().anyMatch(c -> c.getTipo().equals(card.getTipo()))) {
                    Card selectedCard = cards.stream().filter(x -> x.getTipo().equals(card.getTipo())).findFirst().get();
                    selectedCards.put(selectedCard, selectedCards.get(selectedCard)+1);
                } else selectedCards.put(card, 1);
                break;
            case 1:
                if(cards.stream().anyMatch(c -> c.getTipo().equals(card.getTipo()))) {
                    Card selectedCard = cards.stream().filter(x -> x.getTipo().equals(card.getTipo())).findFirst().get();
                    selectedCards.put(selectedCard, selectedCards.get(selectedCard)-1);
                    if(selectedCards.get(selectedCard) == 0) selectedCards.remove(selectedCard);
                }
                break;
            default:
                if(cards.stream().anyMatch(c -> c.getTipo().equals(card.getTipo()))) {
                    Card selectedCard = cards.stream().filter(x -> x.getTipo().equals(card.getTipo())).findFirst().get();
                    selectedCards.put(selectedCard, selectedCards.get(selectedCard)+1);
                } else selectedCards.put(card, 1);
        }
    }

    @Transactional
    public Double findDailyAverageTurns() {
        Double average = (double) turnCount() / turnRepository.findTotalTurnsByDay().size();
        return Math.round(average * 100.0) / 100.0;
    }

    @Transactional
    public Integer findMaxTurnsADay() {
        return turnRepository.findTotalTurnsByDay().stream().max(Comparator.naturalOrder()).get();
    }

    @Transactional
    public Integer findMinTurnsADay() {
        return turnRepository.findTotalTurnsByDay().stream().min(Comparator.naturalOrder()).get();
    }

    @Transactional
    public Double findAverageTurns() {
        Double average = (double) turnCount() / gameService.gameCount();
        return Math.round(average * 100.0) / 100.0;
    }

    @Transactional
    public Integer findMaxTurns() {
        return turnRepository.findTotalTurnsByGame().stream().max(Comparator.naturalOrder()).get();
    }

    @Transactional
    public Integer findMinTurns() {
        return turnRepository.findTotalTurnsByGame().stream().min(Comparator.naturalOrder()).get();
    }

    @Transactional
    public Double findAverageTurnsByNickname(String nickname) {
        Optional<List<Turn>> allTurns = turnRepository.findTurnByNickname(nickname);
        Double averageTurns = 0.;
        if(allTurns.isPresent()) {
            Integer totalTurns = allTurns.get().size();
            Integer totalGames = gameService.findTotalGamesPlayedByNickname(nickname);
            averageTurns = (double) totalTurns/totalGames;
        }
        return averageTurns;
    }

    @Transactional
    public Integer findMaxTurnsInGameByNickname(String nickname) {
        Optional<List<Turn>> allTurns = turnRepository.findTurnByNickname(nickname);
        Integer maxTurns = 0;
        if(allTurns.isPresent()) {
            Map<Integer, List<Turn>> turnsByGame = allTurns.get().stream()
            .collect(Collectors.groupingBy(g -> g.getRound().getGame().getId()));
            
            maxTurns = turnsByGame.values().stream()
            .map(turnsList -> turnsList.size())
            .reduce(0, Integer::max);
        }
        return maxTurns;
    }

    @Transactional
    public Integer findMinTurnsInGameByNickname(String nickname) {
        Optional<List<Turn>> allTurns = turnRepository.findTurnByNickname(nickname);
        Integer minTurns = 0;
        if(allTurns.isPresent()) {
            Map<Integer, List<Turn>> turnsByGame = allTurns.get().stream()
            .collect(Collectors.groupingBy(g -> g.getRound().getGame().getId()));
            
            minTurns = turnsByGame.values().stream()
            .map(turnsList -> turnsList.size())
            .min(Integer::compareTo)
            .get();
        }
        return minTurns;
    }
}
