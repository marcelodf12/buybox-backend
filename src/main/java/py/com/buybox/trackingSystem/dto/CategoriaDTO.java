package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import py.com.buybox.trackingSystem.entities.CategoriaEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CategoriaDTO {
    private Integer i;
    private String n;

   public CategoriaDTO(CategoriaEntity _c){
       this.i = _c.getIdCategoria();
       this.n = _c.getNombre();
   }

    public static List<CategoriaDTO> listFromEntity(List<CategoriaEntity> entityList){
        if(entityList!=null){
            List<CategoriaDTO> list = entityList.stream().map(CategoriaDTO::new).collect(Collectors.toList());
            return list;
        }
        return null;
    }
}
