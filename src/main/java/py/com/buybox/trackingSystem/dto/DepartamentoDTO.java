package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import py.com.buybox.trackingSystem.entities.DepartamentoEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class DepartamentoDTO {
    private Integer idDepartamento;
    private String nombre;

    public DepartamentoDTO(DepartamentoEntity _d){
        this.nombre = _d.getNombre();
        this.idDepartamento = _d.getIdDepartamento();
    }

    public static List<DepartamentoDTO> listFromEntity(List<DepartamentoEntity> entityList){
        if(entityList!=null){
            List<DepartamentoDTO> list = entityList.stream().map(DepartamentoDTO::new).collect(Collectors.toList());
            return list;
        }
        return null;
    }
}
