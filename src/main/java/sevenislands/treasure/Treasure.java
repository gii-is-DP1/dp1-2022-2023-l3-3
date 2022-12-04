package sevenislands.treasure;

import javax.persistence.Column;
import javax.persistence.Entity;

import sevenislands.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Treasure extends BaseEntity {
        @Column(name= "name", unique = true, nullable = false)
        private String name;
}
