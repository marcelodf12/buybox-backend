package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import py.com.buybox.trackingSystem.entities.SucursalEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SucursalDTO {
    private Integer idSucursal;
    private String nombre;
    private Integer idDepartamento;
    private Integer idCiudad;
    private Integer idBarrio;
    private Integer latitud;
    private Integer longitud;
    private Integer rastreable;
    private Integer idEstadoDefecto;
    private Integer editable;
    private Integer notificableLlegada;
    private Integer notificableFinal;
    private String mensajeAlCliente;
    private String mensajeAlClienteFinal;

    public SucursalDTO(SucursalEntity _sucursal){
        this.idSucursal=_sucursal.getIdSucursal();
        this.nombre=_sucursal.getNombre();
        this.latitud=_sucursal.getLatitud();
        this.longitud=_sucursal.getLongitud();
        this.rastreable=_sucursal.getRastreable();
        this.editable=_sucursal.getEditable();
        this.notificableLlegada=_sucursal.getNotificableLlegada();
        this.notificableFinal=_sucursal.getNotificableFinal();
        this.mensajeAlCliente=_sucursal.getMensajeAlCliente();
        this.mensajeAlClienteFinal=_sucursal.getMensajeAlClienteFinal();
        if(_sucursal.getDepartamento()!=null)
            this.idDepartamento=_sucursal.getDepartamento().getIdDepartamento();
        if(_sucursal.getCiudad()!=null)
            this.idCiudad=_sucursal.getCiudad().getIdCiudad();
        if(_sucursal.getBarrio()!=null)
            this.idBarrio=_sucursal.getBarrio().getIdBarrio();
        if(_sucursal.getEstadoDefecto()!=null)
            this.idEstadoDefecto=_sucursal.getEstadoDefecto().getIdEstado();
    }

    public static List<SucursalDTO> listFromEntity(List<SucursalEntity> entityList){
        if(entityList!=null){
            List<SucursalDTO> list = entityList.stream().map(SucursalDTO::new).collect(Collectors.toList());
            return list;
        }
        return null;
    }

}
