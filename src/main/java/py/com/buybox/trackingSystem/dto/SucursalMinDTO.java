package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import py.com.buybox.trackingSystem.entities.CiudadEntity;
import py.com.buybox.trackingSystem.entities.SucursalEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SucursalMinDTO {
    private Integer idSucursal;
    private String nombre;
    private Integer idBarrio;
    private Integer latitud;
    private Integer longitud;

    public SucursalMinDTO(SucursalEntity _s){
        this.idSucursal = _s.getIdSucursal();
        this.nombre = _s.getNombre();
        if(_s.getBarrio()!=null) this.idBarrio = _s.getBarrio().getIdBarrio();
        this.latitud = _s.getLatitud();
        this.longitud = _s.getLongitud();
    }

    public static List<SucursalMinDTO> listFromEntity(List<SucursalEntity> entityList){
        if(entityList!=null){
            List<SucursalMinDTO> list = entityList.stream().map(SucursalMinDTO::new).collect(Collectors.toList());
            return list;
        }
        return null;
    }

    

}
