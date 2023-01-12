package sevenislands.lobby;

import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import sevenislands.model.BaseEntity;
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
      List<Integer> asciiNotAllowed=List.of(58,59,60,61,62,63,64,91,92,93,94,95,96);
      Integer contadorCode=0;
      while(randomString.length()<RANDOM_STRING_LENGTH){
          Random randomGenerator = new Random();
          Integer ascii = randomGenerator.nextInt(maxAscii-minAscii)+minAscii;
          if(!asciiNotAllowed.contains(ascii)){
            char ch = Character.toString(ascii == 96 ? ascii+1 : ascii).charAt(0); //Por comodidad para el usuario, eliminamos un acento grave
            randomString.append(ch);
          }
          contadorCode++;
          
      }
      // SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
      // randomString.append(formatter.format(new Date(System.currentTimeMillis())));
      String code = randomString.toString();
      this.setCode(code);
      return code;
  }

 
}
