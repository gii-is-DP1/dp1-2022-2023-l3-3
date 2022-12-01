package sevenislands.tools;

import org.springframework.stereotype.Component;

@Component
public class metodosReutilizables {
    
    public static Integer EditPaginaControlPanel(Integer id){
        id=id-1;
        return id/5;
    }

    public static Integer DeletePaginaControlPanel(Integer id){
        id=id-2;
        if(id>=0){
            return id/5;
        }else{
            return 0;
        }
    }
}
