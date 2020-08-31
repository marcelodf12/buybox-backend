package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import py.com.buybox.trackingSystem.entities.BarrioEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class BarrioDTO {
    private Integer i;
    private String n;
    private Integer iC;

    public BarrioDTO(BarrioEntity _b){
        this.i = _b.getIdBarrio();
        this.n = _b.getNombre();
        if(_b.getCiudad() != null)this.iC = _b.getCiudad().getIdCiudad();
    }

    public static List<BarrioDTO> listFromEntity(List<BarrioEntity> entityList){
        if(entityList!=null){
            List<BarrioDTO> list = entityList.stream().map(BarrioDTO::new).collect(Collectors.toList());
            return list;
        }
        return null;
    }
}
