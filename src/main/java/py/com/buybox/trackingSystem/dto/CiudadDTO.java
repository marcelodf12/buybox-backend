package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import py.com.buybox.trackingSystem.entities.CategoriaEntity;
import py.com.buybox.trackingSystem.entities.CiudadEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CiudadDTO {
    private Integer idCiudad;
    private String nombre;
    private Integer idDepartamento;

    public CiudadDTO(CiudadEntity _c){
        this.idCiudad = _c.getIdCiudad();
        this.nombre = _c.getNombre();
        if(_c.getDepartamento()!=null) this.idDepartamento = _c.getDepartamento().getIdDepartamento();
    }

    public static List<CiudadDTO> listFromEntity(List<CiudadEntity> entityList){
        if(entityList!=null){
            List<CiudadDTO> list = entityList.stream().map(CiudadDTO::new).collect(Collectors.toList());
            return list;
        }
        return null;
    }
}
