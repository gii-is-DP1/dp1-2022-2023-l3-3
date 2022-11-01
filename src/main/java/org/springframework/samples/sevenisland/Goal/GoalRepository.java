package org.springframework.samples.sevenisland.Goal;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface GoalRepository extends CrudRepository<GoalEntity, Integer> {
    

    @Query("Select distinc goal from Goal goal where goal.tipoLogro = :tipoLogro")
    public Collection<GoalEntity> findByType();
}
