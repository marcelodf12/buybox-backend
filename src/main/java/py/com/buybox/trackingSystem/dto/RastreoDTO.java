package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import py.com.buybox.trackingSystem.entities.RastreoEntity;

import java.util.Calendar;

@Data
@NoArgsConstructor
public class RastreoDTO {
    private Integer idSucursal;
    private Calendar fechaHora;
    private String usuario;

    public RastreoDTO(RastreoEntity _rastreo){
        if(_rastreo.getSucursal()!=null)
            this.idSucursal = _rastreo.getSucursal().getIdSucursal();
        if(_rastreo.getUsuario()!=null)
            this.usuario = _rastreo.getUsuario().getCorreo();
        fechaHora = _rastreo.getFechaHora();
    }
}
