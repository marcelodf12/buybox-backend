package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import py.com.buybox.trackingSystem.entities.ClienteEntity;
import py.com.buybox.trackingSystem.entities.PaqueteEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ClienteDTO {
    private Integer idCliente;
    private String nombre;
    private String apellido;
    private String correo;
    private String celular;
    private String razonSocial;
    private String ruc;
    private String direccion;
    private Integer idSegmento;
    private String casilla;
    private Integer idSucursal;
    private String pass;

    public ClienteDTO(ClienteEntity _client){
        if(_client!=null) {
            this.idCliente = _client.getIdCliente();
            this.nombre = _client.getNombre();
            this.apellido = _client.getApellido();
            this.correo = _client.getCorreo();
            this.celular = _client.getCelular();
            this.razonSocial = _client.getRazonSocial();
            this.ruc = _client.getRuc();
            this.direccion = _client.getDireccion();
            this.casilla = _client.getCasilla();
            if (_client.getSucursal() != null)
                this.idSucursal = _client.getSucursal().getIdSucursal();
            if (_client.getSegmento() != null)
                this.idSegmento = _client.getSegmento().getIdSegmento();
        }
    }

    public static List<ClienteDTO> listFromEntity(List<ClienteEntity> entityList){
        if(entityList!=null){
            List<ClienteDTO> list = entityList.stream().map(ClienteDTO::new).collect(Collectors.toList());
            return list;
        }
        return null;
    }
}
