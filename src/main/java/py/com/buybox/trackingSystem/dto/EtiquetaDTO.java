package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import py.com.buybox.trackingSystem.entities.EstadoEntity;
import py.com.buybox.trackingSystem.entities.EtiquetaEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class EtiquetaDTO {
    private String clave;
    private String valor;
    private Byte editable;
    private String contexto;

    public EtiquetaDTO(EtiquetaEntity _e){
        this.clave = _e.getClave();
        this.valor = _e.getValor();
        this.contexto = _e.getContexto();
    }

    public static List<EtiquetaDTO> listFromEntity(List<EtiquetaEntity> entityList){
        if(entityList!=null){
            List<EtiquetaDTO> list = entityList.stream().map(EtiquetaDTO::new).collect(Collectors.toList());
            return list;
        }
        return null;
    }
}
