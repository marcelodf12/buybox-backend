package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import py.com.buybox.trackingSystem.entities.EtiquetaEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class EtiquetaDTO {
    private String k;
    private String v;
    private Byte e;
    private String c;

    public EtiquetaDTO(EtiquetaEntity _e){
        this.k = _e.getClave();
        this.v = _e.getValor();
        this.c = _e.getContexto();
    }

    public static List<EtiquetaDTO> listFromEntity(List<EtiquetaEntity> entityList){
        if(entityList!=null){
            List<EtiquetaDTO> list = entityList.stream().map(EtiquetaDTO::new).collect(Collectors.toList());
            return list;
        }
        return null;
    }
}
