/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.sevenisland.user;

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
	public Optional<User> findUser(Integer id) {
		// retrieving user by id
		return userRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public User findUser(String nickname) {
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

}
