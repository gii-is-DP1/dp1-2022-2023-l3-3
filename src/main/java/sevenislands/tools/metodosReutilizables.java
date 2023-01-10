package sevenislands.tools;

import org.springframework.stereotype.Component;

@Component
public class metodosReutilizables {
    
    

    public static Integer editPaginaControlPanel(Integer id,Integer tamanoPaginacion){
        id=id-1;
        return id/tamanoPaginacion;
    }

    public static Integer deletePaginaControlPanel(Integer id,Integer tamanoPaginacion){
        id=id-2;
        if(id>=0){
            return id/tamanoPaginacion;
        }else{
            return 0;
        }
    }
}
