package org.springframework.samples.Goal;

import org.springframework.data.repository.Repository;

public interface GoalRepository extends Repository<GoalEntity, Integer> {
    
    void save(GoalEntity goalEntity) throws DataAccessException;
}
