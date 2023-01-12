package sevenislands.gameDetails;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.card.Card;
import sevenislands.enums.Mode;
import sevenislands.enums.Tipo;
import sevenislands.exceptions.NotExistLobbyException;
import sevenislands.game.Game;
import sevenislands.game.GameService;
import sevenislands.game.turn.TurnService;
import sevenislands.lobby.lobbyUser.LobbyUserService;
import sevenislands.user.User;

@Service
public class GameDetailsService {
    
    private GameDetailsRepository gameDetailsRepository;

    private GameService gameService;
    private TurnService turnService;
    private final LobbyUserService lobbyUserService;

    @Autowired
    public GameDetailsService(GameDetailsRepository gameDetailsRepository, GameService gameService, TurnService turnService,
            LobbyUserService lobbyUserService) {
        this.gameDetailsRepository = gameDetailsRepository;
        this.gameService = gameService;
        this.turnService = turnService;
        this.lobbyUserService = lobbyUserService;
    }

    @Transactional
    public List<Object []> findPunctuations() {
        return gameDetailsRepository.findPunctuations();
    }

    @Transactional
    public List<Object []> findPunctuationByGame(Game game) {
        return gameDetailsRepository.findPunctuationByGame(game.getId());
    }

    @Transactional
    public Long findPunctuationByNickname(String nickname) {
        Long totalPoints = gameDetailsRepository.findPunctuationByNickname(nickname);
        totalPoints = totalPoints == null ? 0 : totalPoints;
        return totalPoints;
    }

    @Transactional
    public void save(GameDetails gameDetails) {
        gameDetailsRepository.save(gameDetails);
    }

    @Transactional
    public Boolean checkPunctuationByGameAndUser(Game game, User user) {
        return gameDetailsRepository.checkPunctuationByGameAndUser(game.getId(), user.getNickname());
    }

    @Transactional
    public Boolean checkPunctuationByGame(Game game) {
        return gameDetailsRepository.checkPunctuationByGame(game.getId());
    }

    @Transactional
    public List<GameDetails> calculateDetails(User logedUser) throws NotExistLobbyException {
        List<GameDetails> gameDetailsList = new ArrayList<>();
        Optional<Game> game = gameService.findGameByUser(logedUser);

        if(game.isPresent()) {
            List<Integer> pointsList = List.of(1,3,7,13,21,30,40,50,60);
            List<Object []> details = new ArrayList<>();

            Integer userPoints = 0;
            Integer doblons = 0;

            Boolean tieBreak = false;

            User winner = null;
            Integer winnerPoints = -1;
            Integer winnerDoblon = -1;

            for(User user : lobbyUserService.findUsersByLobbyAndMode(game.get().getLobby(), Mode.PLAYER)) {

                doblons = 0;
                userPoints = 0;
                Map<Card, Integer> cards = turnService.findPlayerCardsLastTurn(user.getNickname());

                Card doblon = cards.keySet().stream().filter(card -> card.getTipo() == Tipo.Doblon).findFirst().orElse(null);
                if(doblon != null) {
                    doblons = cards.get(doblon);
                    cards.remove(doblon);
                }
                userPoints += doblons;
                Map<Card, Integer> orderedCards = cards.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

                List<Card> listKeys = new ArrayList<>(orderedCards.keySet());

                for(Card card : listKeys) {
                    Integer multiplicity = cards.get(card);
                    cards.replaceAll((key,value) -> value - multiplicity);
                    userPoints += multiplicity*pointsList.get(listKeys.size()-listKeys.indexOf(card)-1);
                }

                if(userPoints > winnerPoints) {
                    winner = user;
                    winnerPoints = userPoints;
                    winnerDoblon = doblons;
                    tieBreak = false;
                } else if(userPoints == winnerPoints && doblons > winnerDoblon) {
                    winner = user;
                    winnerPoints = userPoints;
                    winnerDoblon = doblons;
                    tieBreak = true;
                }
                details.add(new Object[] {user, userPoints});
            }

            for(Object[] detail : details) {
                GameDetails gameDetails = new GameDetails();
                gameDetails.setGame(game.get());
                gameDetails.setUser((User) detail[0]);
                gameDetails.setPunctuation((Integer) detail[1]);
                game.get().setTieBreak(tieBreak);
                game.get().setWinner(winner);
                save(gameDetails);
                gameService.save(game.get());
                gameDetailsList.add(gameDetails);
            }
        }
        return gameDetailsList;
    }

    @Transactional
    public void recoverSelectedCards(User user, Map<Card,Integer> cardsList, HttpServletRequest request) {
        if(cardsList != null) {
            for(Card card : cardsList.keySet()) {
                for(int i = 0; i < cardsList.get(card); i++) {
                    turnService.addCardToUser(card.getId(), user);
                    turnService.changeCard(card.getId(), user, 1, request);
                }
            }
        }
    }

    @Transactional
    public Long findGamesByUser(User user) {
        return gameDetailsRepository.findAllByUser(user);
    }

    @Transactional
    public Integer findTotalPunctuation() {
        return gameDetailsRepository.findTotalPunctuation();
    }

    @Transactional
    public Double findDailyAveragePunctuation() {
        Double average = (double) findTotalPunctuation() / gameService.findTotalGamesPlayedByDay().size();
        return Math.round(average * 100.0) / 100.0;
    }

    @Transactional
    public Integer findMaxPunctuationADay() {
        return gameDetailsRepository.findSumPunctuationsByDay().stream().max(Comparator.naturalOrder()).get();
    }

    @Transactional
    public Integer findMinPunctuationADay() {
        return gameDetailsRepository.findSumPunctuationsByDay().stream().min(Comparator.naturalOrder()).get();
    }

    @Transactional
    public Double findAveragePunctuation() {
        Double average = (double) findTotalPunctuation() / gameService.gameCount();
        return Math.round(average * 100.0) / 100.0;
    }

    @Transactional
    public Integer findMaxPunctuation() {
        return gameDetailsRepository.findSumPunctuations().stream().max(Comparator.naturalOrder()).get();
    }

    @Transactional
    public Integer findMinPunctuation() {
        return gameDetailsRepository.findSumPunctuations().stream().min(Comparator.naturalOrder()).get();
    }

}
