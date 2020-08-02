package py.com.buybox.trackingSystem.dto;

import lombok.Data;

@Data
public class RastreoDTO {
    private Integer idRastreo;
    private Integer idPaquete;
    private Integer idSucursal;
    private java.sql.Timestamp fechaHora;
    private Integer idUsuario;
}
