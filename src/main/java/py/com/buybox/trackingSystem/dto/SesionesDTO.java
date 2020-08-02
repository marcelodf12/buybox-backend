package py.com.buybox.trackingSystem.dto;

import lombok.Data;

@Data
public class SesionesDTO {
    private String token;
    private java.sql.Timestamp vencimiento;
    private Integer idUsuario;

}
