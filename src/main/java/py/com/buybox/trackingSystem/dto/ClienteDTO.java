package py.com.buybox.trackingSystem.dto;

import lombok.Data;

@Data
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
}
