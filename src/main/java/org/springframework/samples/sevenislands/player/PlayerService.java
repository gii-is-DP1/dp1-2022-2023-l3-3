package org.springframework.samples.sevenislands.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataAccessException;

@Service
public class PlayerService {

    private PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional
    public void savePlayer(Player player) throws DataAccessException {
        // creating player
        playerRepository.save(player);
    }

    @Transactional(readOnly = true)
    public Player findUser(Integer id) {
        // retrieving player by id
        return playerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Player findPlayer(String nickname) {
        // retrieving player by nickname
        return playerRepository.findByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public Iterable<Player> findAll() {
        // retrieving all players
        return playerRepository.findAll();
    }

    @Transactional
    public void deleteUser(Integer id) {
        // deleting user by id
        playerRepository.deleteById(id);
    }

    @Transactional
    public void deleteUser(String nickname) {
        // deleting user by nickname
        playerRepository.delete(nickname);
    }
}
