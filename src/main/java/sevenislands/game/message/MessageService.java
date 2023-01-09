package sevenislands.game.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sevenislands.game.Game;
import sevenislands.user.User;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Transactional
    public void saveMessage(User user, String sentMessage, Game game) {
        Message message = new Message();
        message.setMessage(sentMessage);
        message.setSender(user);
        message.setGame(game);
        messageRepository.save(message);
    }

    @Transactional
    public List<Message> getMessages(Game game) {
        return messageRepository.findByGame(game);
    }

}
