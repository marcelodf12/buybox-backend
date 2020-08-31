package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import py.com.buybox.trackingSystem.entities.CiudadEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CiudadDTO {
    private Integer i;
    private String n;
    private Integer iD;

    public CiudadDTO(CiudadEntity _c){
        this.i = _c.getIdCiudad();
        this.n = _c.getNombre();
        if(_c.getDepartamento()!=null) this.iD = _c.getDepartamento().getIdDepartamento();
    }

    public static List<CiudadDTO> listFromEntity(List<CiudadEntity> entityList){
        if(entityList!=null){
            List<CiudadDTO> list = entityList.stream().map(CiudadDTO::new).collect(Collectors.toList());
            return list;
        }
        return null;
    }
}
