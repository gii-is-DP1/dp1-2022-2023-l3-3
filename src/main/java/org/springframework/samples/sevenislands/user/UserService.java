package org.springframework.samples.sevenislands.user;


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
	public void saveUser(User user) throws DataAccessException {
		// creating user
		userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public Optional<User> findUserById(Integer id) {
		// retrieving user by id
		return userRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Optional<User> findUser(String nickname) {
		// retrieving user by nickname
		return userRepository.findByNickname(nickname);
	}

	@Transactional(readOnly = true)
	public Iterable<User> findAll() {
		return userRepository.findAll();
	}

	@Transactional
	public void deleteUser(Integer id) {
		// deleting user by id
		userRepository.deleteById(id);
	}

	@Transactional
	public void deleteUser(String nickname) {
		// deleting user by nickname
		userRepository.delete(nickname);
	}

	@Transactional
	public Boolean checkUserByName(String nickname) {
		return userRepository.checkUser(nickname);
	}

}
