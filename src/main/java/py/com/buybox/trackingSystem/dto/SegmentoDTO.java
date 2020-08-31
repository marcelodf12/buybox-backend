package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import py.com.buybox.trackingSystem.entities.SegmentoEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SegmentoDTO {
    private Integer i;
    private String n;

    public SegmentoDTO(SegmentoEntity _s){
        this.i = _s.getIdSegmento();
        this.n = _s.getNombre();
    }

    public static List<SegmentoDTO> listFromEntity(List<SegmentoEntity> entityList){
        if(entityList!=null){
            List<SegmentoDTO> list = entityList.stream().map(SegmentoDTO::new).collect(Collectors.toList());
            return list;
        }
        return null;
    }
}
