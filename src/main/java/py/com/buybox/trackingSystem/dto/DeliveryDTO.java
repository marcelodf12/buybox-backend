package py.com.buybox.trackingSystem.dto;

import lombok.Data;

@Data
public class DeliveryDTO {
    private Integer idDelivery;
    private Integer idCliente;
    private Integer longitud;
    private Integer latitud;
    private Integer referencia;
    private Integer idDepartamento;
    private Integer idCiudad;
    private Integer idBarrio;
    private Integer idPaquete;
}
