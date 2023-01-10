package sevenislands.archievement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sevenislands.achievement.Achievement;
import sevenislands.achievement.AchievementRepository;
import sevenislands.achievement.AchievementService;
import sevenislands.card.CardRepository;
import sevenislands.card.CardService;
import sevenislands.enums.AchievementType;
import sevenislands.game.Game;
import sevenislands.game.GameRepository;
import sevenislands.game.GameService;
import sevenislands.game.island.IslandRepository;
import sevenislands.game.island.IslandService;
import sevenislands.game.round.RoundRepository;
import sevenislands.game.round.RoundService;
import sevenislands.game.turn.TurnRepository;
import sevenislands.game.turn.TurnService;
import sevenislands.gameDetails.GameDetailsRepository;
import sevenislands.gameDetails.GameDetailsService;
import sevenislands.lobby.Lobby;
import sevenislands.lobby.LobbyRepository;
import sevenislands.lobby.LobbyService;
import sevenislands.register.RegisterRepository;
import sevenislands.register.RegisterService;
import sevenislands.user.User;
import sevenislands.user.UserRepository;
import sevenislands.user.UserService;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {

    @Mock
    TurnRepository turnRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    RoundRepository roundRepository;
    @Mock
    IslandRepository islandRepository;
    @Mock
    CardRepository cardRepository;
    @Mock
    LobbyRepository lobbyRepository;
    @Mock
    AchievementRepository achievementRepository;
    @Mock
    GameRepository gameRepository;
    @Mock
    GameDetailsRepository gameDetailsRepository;
    @Mock
    RegisterRepository registerRepository;

    AchievementService achievementService;
    GameService gameService;
    GameDetailsService gameDetailsService;
    RegisterService registerService;
    UserService userService;
    TurnService turnService;
    RoundService roundService;
    IslandService islandService;
    CardService cardService;
    LobbyService lobbyService;

    Achievement achievement;
    Achievement achievement2;
    Achievement achievement3;
    Achievement achievement4;

    User user1;
    User user2;

    Game game1;

    Lobby lobby1;

    @BeforeEach
    public void config(){
        roundService = new RoundService(roundRepository);
        lobbyService = new LobbyService(lobbyRepository);
        gameService = new GameService(roundService, gameRepository,null,lobbyService);
        cardService = new CardService(cardRepository);
        islandService = new IslandService(islandRepository);
        userService = new UserService(null, null,
        null,userRepository, null, null);
        turnService = new TurnService(turnRepository, gameService, roundService, islandService, cardService,null);
        gameDetailsService = new GameDetailsService(gameDetailsRepository, gameService, turnService,null);
        registerService = new RegisterService(registerRepository);
        achievementService = new AchievementService(achievementRepository, gameService, gameDetailsService, registerService,null);
        
        
        achievement = new Achievement();
        achievement.setId(1);
        achievement.setThreshold(100);
        achievement.setBadgeImage("null");
        achievement.setDescription("null");
        achievement.setName("Name");
        achievement.setAchievementType(AchievementType.Games);
        achievement2 = new Achievement();
        achievement2.setId(2);
        achievement2.setThreshold(300);
        achievement2.setBadgeImage("null");
        achievement2.setDescription("null");
        achievement2.setName("Name2");
        achievement2.setAchievementType(AchievementType.Punctuation);
        achievement3 = new Achievement();
        achievement3.setId(1);
        achievement3.setThreshold(5);
        achievement3.setBadgeImage("null");
        achievement3.setDescription("null");
        achievement3.setName("Name");
        achievement3.setAchievementType(AchievementType.TieBreaker);
        achievement4 = new Achievement();
        achievement4.setId(2);
        achievement4.setThreshold(40);
        achievement4.setBadgeImage("null");
        achievement4.setDescription("null");
        achievement4.setName("Name2");
        achievement4.setAchievementType(AchievementType.Victories);

        user1 = userService.createUser(1, "userFalse1", "userFalse1@gmail.com");
        user2 = userService.createUser(2, "userFalse2", "userFalse2@gmail.com");

        lobby1 = new Lobby();
        lobby1.setId(1);
        lobby1.generatorCode();
        lobby1.setActive(true);
        lobby1.addPlayer(user1);
        lobby1.addPlayer(user2);

        game1 = new Game();
        game1.setId(1);
        game1.setActive(true);
        game1.setCreationDate( LocalDateTime.now());
        game1.setLobby(lobby1);
        
    }

    @Test
    public void findAllTest(){
        when(achievementRepository.findAll()).thenReturn(List.of(achievement));
        assertEquals(1, achievementService.findAll().size());
    }

    @Test
    public void findAchievementByIdTest(){
        when(achievementRepository.findById(any())).thenReturn(Optional.of(achievement));
        assertEquals(AchievementType.Games, achievementService.findAchievementById(achievement.getId()).orElse(null).getAchievementType());
    }

    @Test
    public void findAchievementByTypeTest(){
        when(achievementRepository.findByType(any())).thenReturn(List.of(achievement));
        assertEquals(1, achievementService.getAchievementByType(achievement.getAchievementType()).size());
    }

    @Test
    public void saveAchievementTest(){
        when(achievementRepository.save(any())).thenReturn((achievement));
        assertEquals(AchievementType.Games, achievementService.saveAchievement(achievement).getAchievementType());
    }

    @Test
    public void updateAchievementTest(){
        assertThrows(Exception.class, ()->achievementService.updateAchievement(achievement2, achievement.getId()));
        when(achievementRepository.findById(any())).thenReturn(Optional.of(achievement));
        when(achievementRepository.save(any())).thenReturn((achievement2));
        assertEquals(AchievementType.Punctuation, achievementService.updateAchievement(achievement, achievement.getId()).getAchievementType());
    }

    @Test
    public void calculateAchievementTest(){
        when(gameRepository.findGameByNickname(any())).thenReturn(Optional.of(List.of(game1)));
        when(gameDetailsRepository.findPunctuationByNickname(any())).thenReturn(400L);
        when(gameRepository.findVictoriesByNickname(any())).thenReturn(50L);
        when(gameRepository.findTieBreaksByNickname(any())).thenReturn(7L);
        when(gameDetailsRepository.findAllByNickname(any())).thenReturn(200L);
        when(achievementRepository.findAll()).thenReturn(List.of(achievement,achievement2,achievement3,achievement4));
        assertEquals(List.of(true,true,true, true), achievementService.calculateAchievements(user1));
    }

    @Test
    public void addchievementTest(){
        assertEquals("logroJugarGames.png", achievementService.addAchievement(achievement).getBadgeImage());
        assertEquals("logroJugarGames.png", achievementService.addAchievement(achievement2).getBadgeImage());
        assertEquals("logroTieBreaker.png", achievementService.addAchievement(achievement3).getBadgeImage());
        assertEquals("logroVictories.png", achievementService.addAchievement(achievement4).getBadgeImage());
       
    }

    
}
