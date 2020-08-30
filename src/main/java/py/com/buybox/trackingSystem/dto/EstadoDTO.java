package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import py.com.buybox.trackingSystem.entities.DepartamentoEntity;
import py.com.buybox.trackingSystem.entities.EstadoEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class EstadoDTO {
    private Integer idEstado;
    private String estado;
    private String etiqueta;
    private String color;

    public EstadoDTO(EstadoEntity _e){
        this.idEstado = _e.getIdEstado();
        this.estado = _e.getEstado();
        this.etiqueta = _e.getEtiqueta();
        this.color = _e.getColor();
    }

    public static List<EstadoDTO> listFromEntity(List<EstadoEntity> entityList){
        if(entityList!=null){
            List<EstadoDTO> list = entityList.stream().map(EstadoDTO::new).collect(Collectors.toList());
            return list;
        }
        return null;
    }

}
