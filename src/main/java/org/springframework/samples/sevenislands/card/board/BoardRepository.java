package org.springframework.samples.sevenislands.card.board;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;

public interface BoardRepository extends CrudRepository<Board, Integer> {

}
