package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import py.com.buybox.trackingSystem.entities.BarrioEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class BarrioDTO {
    private Integer idBarrio;
    private String nombre;
    private Integer idCiudad;

    public BarrioDTO(BarrioEntity _b){
        this.idBarrio = _b.getIdBarrio();
        this.nombre = _b.getNombre();
        if(_b.getCiudad() != null)this.idCiudad = _b.getCiudad().getIdCiudad();
    }

    public static List<BarrioDTO> listFromEntity(List<BarrioEntity> entityList){
        if(entityList!=null){
            List<BarrioDTO> list = entityList.stream().map(BarrioDTO::new).collect(Collectors.toList());
            return list;
        }
        return null;
    }
}
