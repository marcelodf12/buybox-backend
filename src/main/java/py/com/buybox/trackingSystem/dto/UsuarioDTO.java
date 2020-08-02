package py.com.buybox.trackingSystem.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private java.sql.Timestamp bloqueadoHasta;
    private Byte activo;
    private Integer intentosFallidos;
    private String linkDeRecuperacion;
    private java.sql.Timestamp linkFechaVencimiento;
    private String pass;
    private String salt;

}
