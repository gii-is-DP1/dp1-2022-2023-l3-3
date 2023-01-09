package sevenislands.lobby;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import sevenislands.model.BaseEntity;
import sevenislands.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Lobby extends BaseEntity {

  @Column(name = "code", unique = true, nullable = false)
  private String code;

  @Column(name = "active", unique = false, nullable = false)
  private boolean active;

   /**
     * Crea un código aleatorio para la lobby.
     * @return String
     */
    public String generatorCode() {
      Integer minAscii = 48;
      Integer maxAscii = 122;
      Integer RANDOM_STRING_LENGTH = 8;
      StringBuffer randomString = new StringBuffer();
      
      for(int i = 0; i<RANDOM_STRING_LENGTH; i++) {
          Random randomGenerator = new Random();
          Integer ascii = randomGenerator.nextInt(maxAscii-minAscii)+minAscii;
          char ch = Character.toString(ascii == 96 ? ascii+1 : ascii).charAt(0); //Por comodidad para el usuario, eliminamos un acento grave
          randomString.append(ch);
      }
      // SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
      // randomString.append(formatter.format(new Date(System.currentTimeMillis())));
      String code = randomString.toString();
      this.setCode(code);
      return code;
  }

 
}
