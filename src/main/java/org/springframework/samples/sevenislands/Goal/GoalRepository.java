package org.springframework.samples.sevenislands.Goal;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface GoalRepository extends Repository<GoalEntity, Integer> {
    
    void save(GoalEntity goalEntity) throws DataAccessException;

    @Query("Select distinc goal from Goal goal where goal.tipoLogro = :tipoLogro")
    public Collection<GoalEntity> findByType();
}
