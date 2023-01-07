package sevenislands.gameDetails;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.card.Card;
import sevenislands.enums.Tipo;
import sevenislands.game.Game;
import sevenislands.game.GameService;
import sevenislands.game.turn.TurnService;
import sevenislands.user.User;

@Service
public class GameDetailsService {
    
    private GameDetailsRepository gameDetailsRepository;

    private GameService gameService;
    private TurnService turnService;

    @Autowired
    public GameDetailsService(GameDetailsRepository gameDetailsRepository, GameService gameService, TurnService turnService) {
        this.gameDetailsRepository = gameDetailsRepository;
        this.gameService = gameService;
        this.turnService = turnService;
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
    public void calculateDetails(User logedUser) {
        Optional<Game> game = gameService.findGameByNickname(logedUser.getNickname());

        if(game.isPresent()) {
            List<Integer> pointsList = List.of(1,3,7,13,21,30,40,50,60);
            List<Object []> details = new ArrayList<>();

            Integer userPoints = 0;
            Integer doblons = 0;

            Boolean tieBreak = false;

            User winner = null;
            Integer winnerPoints = -1;
            Integer winnerDoblon = -1;

            for(User user : game.get().getLobby().getUsers()) {
                doblons = 0;
                userPoints = 0;
                Map<Card, Integer> cards = turnService.findPlayerCardsLastTurn(user.getNickname());
                Card doblon = cards.keySet().stream().filter(card -> card.getTipo() == Tipo.Doblon).findFirst().orElse(null);
                if(doblon != null) {
                    doblons = cards.get(doblon);
                    cards.remove(doblon);
                }
                userPoints += doblons;
                List<Card> listKeys = new ArrayList<>(cards.keySet());
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
            }
        }
    }

    @Transactional
    public Long findGamesByNickname(String nickname) {
        return gameDetailsRepository.findAllByNickname(nickname);
    }

    @Transactional
    public Integer findTotalPunctuation() {
        return gameDetailsRepository.findTotalPunctuation();
    }

    @Transactional
    public Double findAveragePunctuation() {
        return (double) findTotalPunctuation() / gameService.findTotalGamesPlayedPerDay().size();
    }

    @Transactional
    public List<Integer> findSumPunctuationsPerDay() {
        return gameDetailsRepository.findSumPunctuationsPerDay();
    }

    @Transactional
    public Integer findMaxPunctuationADay() {
        return findSumPunctuationsPerDay().stream().max(Comparator.naturalOrder()).get();
    }

    @Transactional
    public Integer findMinPunctuationADay() {
        return findSumPunctuationsPerDay().stream().min(Comparator.naturalOrder()).get();
    }

    @Transactional
    public Integer findMaxPunctuation() {
        List<Integer> points = gameDetailsRepository.findSumPunctuations();
        return points.stream().max(Comparator.naturalOrder()).get();
    }

    @Transactional
    public Integer findMinPunctuation() {
        List<Integer> points = gameDetailsRepository.findSumPunctuations();
        return points.stream().min(Comparator.naturalOrder()).get();
    }

}
