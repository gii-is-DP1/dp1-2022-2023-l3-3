package sevenislands.user;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	private UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	public void save(User user) throws DataAccessException {
		userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public Optional<User> findUser(Integer id) {
		return userRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<User> findUser(String nickname) {
		return userRepository.findByNickname(nickname);
	}

	@Transactional(readOnly = true)
	public Iterable<User> findAll() {
		return userRepository.findAll();
	}

	@Transactional
	public void deleteUser(Integer id) {
		userRepository.deleteById(id);
	}

	@Transactional
	public void deleteUser(String nickname) {
		userRepository.delete(nickname);
	}

	@Transactional
	public Boolean checkUserByName(String nickname) {
		return userRepository.checkUser(nickname);
	}

	@Transactional
	public Boolean checkUserLobbyByName(String nickname) {
		return userRepository.checkUserLobby(nickname);
	}

	@Transactional 
	public void update(User user) {
	    userRepository.updateUser(user, user.getId());
	}

	@Transactional
	public void remove(User user) {
		userRepository.delete(user);
	}

	@Transactional
	public List<String> findDistinctAuthorities() {
		return userRepository.findAuthorities();
	}
}
