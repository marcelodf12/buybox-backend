package py.com.buybox.trackingSystem.dto;

import lombok.Data;

@Data
public class SucursalDTO {
    private Integer idSucursal;
    private String nombre;
    private Integer idDepartamento;
    private Integer idCiudad;
    private Integer idBarrio;
    private Integer latitud;
    private Integer longitud;
    private Byte rastreable;
    private Integer idEstadoDefecto;
    private Byte editable;
    private Byte notificableLlegada;
    private Byte notificableFinal;
    private String mensajeAlCliente;
    private String mensajeAlClienteFinal;

}
