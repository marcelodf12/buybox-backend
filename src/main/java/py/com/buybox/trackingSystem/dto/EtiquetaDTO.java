package py.com.buybox.trackingSystem.dto;

import lombok.Data;

@Data
public class EtiquetaDTO {
    private String clave;
    private String valor;
    private Byte editable;
    private String contexto;
}
