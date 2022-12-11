package sevenislands.game.turn;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    public List<Island> IslandToChoose(Turn turn,String nickName, List<Island> islandList){
        List<Island> islas=new ArrayList<>();
        Map<Card, Integer> playerCardsMap=findPlayerCardsLastTurn(nickName);
        Integer numCardMazo=playerCardsMap.values().stream().mapToInt(x->x).sum();
        if(turn.getDice()!=null){
            for(int i=1; i<=(turn.getDice()+numCardMazo);i++){
                Integer cambioIsla=turn.getDice()-i;
                if(i<=islandList.size() && numCardMazo>=cambioIsla &&islandList.get(i-1).getNum()!=0){
                    islas.add(islandList.get(i-1));
                }
                
        }
    }
        return islas;
    }

    @Transactional
    public void save(Turn turn) {
        turnRepository.save(turn);
    }

    @Transactional
    public void initTurn(User logedUser, Round round, List<User> userList, List<Card> cards) {
        Turn turn = new Turn();
        Integer nextUser = (userList.indexOf(logedUser) + 1) % userList.size();
        turn.setStartTime(LocalDateTime.now());
        turn.setRound(round);
        turn.setUser(userList.get(nextUser));
        List<Card> treasureList = new ArrayList<>();
        if (cards == null) {
            Turn prevTurn = turnRepository.findTurnByNickname(userList.get(nextUser).getNickname()).get(0);
            treasureList.addAll(prevTurn.getCards());
        } else
            treasureList.addAll(cards);
        turn.setCards(treasureList);
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
        List<Card> treasureList = new ArrayList<>();
        round.setGame(game.get());
        treasureList.add(cardService.findCardByGameAndTreasure(game.get().getId(), Tipo.Doblon));
        treasureList.add(cardService.findCardByGameAndTreasure(game.get().getId(), Tipo.Doblon));
        treasureList.add(cardService.findCardByGameAndTreasure(game.get().getId(), Tipo.Doblon));
        roundService.save(round);
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
            Integer randomCardNumber = randomGenerator.nextInt(cardList.size());
            Card selectedCard = allCards.get(cardList.get(randomCardNumber));
            island.setCard(selectedCard);
            selectedCard.setMultiplicity(selectedCard.getMultiplicity() - 1);
            cardService.save(selectedCard);
            islandService.save(island);
        }
    }

    @Transactional
    public void refreshDesk(Integer idIsla,User logedUser, Optional<Game> game){
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
        }else {
            Island island=islandList.get(idIsla-1);
            island.setNum(0);
            islandService.save(island);
        }
    }

    @Transactional
    public void checkUserGame(User logedUser) throws NotExistLobbyException {
        try {
            if (gameService.findGameByNickname(logedUser.getNickname(), true).isPresent()) {

                // TODO: Poner el Lobby como Optional<Lobby> y realizar la comprobación de que
                // existe
                Lobby lobby = lobbyService.findLobbyByPlayerId(logedUser.getId());
                Optional<Game> game = gameService.findGameByNickname(logedUser.getNickname(), true);
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
    public Map<Card, Integer> findPlayerCardsLastTurn(String nickname) {
        Turn lastPlayerTurn = turnRepository.findTurnByNickname(nickname).get(0);
        Map<Card, Integer> map = new HashMap<>();
        for (Card t : lastPlayerTurn.getCards()) {
            if (map.containsKey(t)) {
                Integer newValue = map.get(t) + 1;
                map.put(t, newValue);
            } else {
                map.put(t, 1);
            }
        }
        return map.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
    @Transactional
    public List<Card> findPlayerCardsPenultimateTurn(String nickname) {
        Turn lastPlayerTurn = turnRepository.findTurnByNickname(nickname).get(1);
        return lastPlayerTurn.getCards();
    }

    @Transactional
    public void AnadirCarta(Integer id,String nickname){
        Turn lastPlayerTurn = turnRepository.findTurnByNickname(nickname).get(0);
        List<Card> cartasLastTurn=lastPlayerTurn.getCards();
        Island island=islandService.findIslandById(id);
        Card card=cardService.findCardById(island.getCard().getId());
        cartasLastTurn.add(card);
        lastPlayerTurn.setCards(cartasLastTurn);
        turnRepository.save(lastPlayerTurn);
    }

    @Transactional
    public void DeleteCard(Integer id,String nickname){
        
        Turn lastPlayerTurn = turnRepository.findTurnByNickname(nickname).get(0);
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
    }


@Transactional
public boolean endGame(Game game){
    boolean res=true;
    List<Island> islandList=islandService.findIslandsByGameId(game.getId());
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


    
}
