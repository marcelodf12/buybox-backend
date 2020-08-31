package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import py.com.buybox.trackingSystem.entities.EstadoEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class EstadoDTO {
    private Integer i;
    private String s;
    private String e;
    private String c;

    public EstadoDTO(EstadoEntity _e){
        this.i = _e.getIdEstado();
        this.s = _e.getEstado();
        this.e = _e.getEtiqueta();
        this.c = _e.getColor();
    }

    public static List<EstadoDTO> listFromEntity(List<EstadoEntity> entityList){
        if(entityList!=null){
            List<EstadoDTO> list = entityList.stream().map(EstadoDTO::new).collect(Collectors.toList());
            return list;
        }
        return null;
    }

}
