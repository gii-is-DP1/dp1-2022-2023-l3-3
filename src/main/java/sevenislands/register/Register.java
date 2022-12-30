package sevenislands.register;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Past;

import sevenislands.model.BaseEntity;
import sevenislands.user.User;
import sevenislands.achievement.Achievement;

import org.springframework.format.annotation.DateTimeFormat;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Register extends BaseEntity{
    
    @Past
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Column(name = "acquisition_date", nullable = false)
	private Date acquisitionDate;

    @ManyToOne
    private User user;
    
    @ManyToOne
    private Achievement achievement;

}
