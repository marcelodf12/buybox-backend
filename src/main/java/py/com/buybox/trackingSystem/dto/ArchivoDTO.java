package py.com.buybox.trackingSystem.dto;

import lombok.Data;

@Data
public class ArchivoDTO {
    private Integer idArchivo;
    private String hash;
    private String nombre;
    private java.sql.Timestamp fechaCreacion;
    private String contexto;
    private Integer entidad;
    private String tipo;

}
