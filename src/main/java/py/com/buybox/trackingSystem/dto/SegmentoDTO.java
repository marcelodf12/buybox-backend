package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import py.com.buybox.trackingSystem.entities.EtiquetaEntity;
import py.com.buybox.trackingSystem.entities.SegmentoEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SegmentoDTO {
    private Integer idSegmento;
    private String nombre;

    public SegmentoDTO(SegmentoEntity _s){
        this.idSegmento = _s.getIdSegmento();
        this.nombre = _s.getNombre();
    }

    public static List<SegmentoDTO> listFromEntity(List<SegmentoEntity> entityList){
        if(entityList!=null){
            List<SegmentoDTO> list = entityList.stream().map(SegmentoDTO::new).collect(Collectors.toList());
            return list;
        }
        return null;
    }
}
