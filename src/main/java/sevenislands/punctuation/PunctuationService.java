package sevenislands.punctuation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.game.Game;
import sevenislands.user.User;

@Service
public class PunctuationService {
    
    private PunctuationRepository punctuationRepository;

    @Autowired
    public PunctuationService(PunctuationRepository punctuationRepository) {
        this.punctuationRepository = punctuationRepository;
    }

    @Transactional
    public List<Object []> findPunctuations() {
        return punctuationRepository.findPunctuations();
    }

    @Transactional
    public List<Object []> findPunctuationByGame(Game game) {
        return punctuationRepository.findPunctuationByGame(game.getId());
    }

    @Transactional
    public Long findPunctuationByNickname(String nickname) {
        return punctuationRepository.findPunctuationByNickname(nickname);
    }

    @Transactional
    public void save(Punctuation punctuation) {
        punctuationRepository.save(punctuation);
    }

    @Transactional
    public Boolean checkPunctuationByGameAndUser(Game game, User user) {
        return punctuationRepository.findPunctuationByGame(game.getId(), user.getNickname());
    }
}
