package py.com.buybox.trackingSystem.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

@Table(name = "bb_usuario")
@Entity
@Data
public class UsuarioEntity implements Serializable {

    /*
        ESTADOS:
            -1  : Eliminado
            0   : Inactivo
            1   : Activo

     */

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", insertable = false, nullable = false)
    private Integer idUsuario;

    @Column(name = "activo")
    private Integer activo;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "bloqueado_hasta")
    private Calendar bloqueadoHasta;

    @Column(name = "correo", nullable = false)
    private String correo;

    @Column(name = "intentos_fallidos")
    private Integer intentosFallidos;

    @Column(name = "link_de_recuperacion", unique=true)
    private String linkDeRecuperacion;

    @Column(name = "link_fecha_vencimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar linkFechaVencimiento;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "pass")
    private String pass;

    @ManyToMany
    @JoinTable(
            name = "bb_usuario_rol",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol"))
    private List<RolEntity> rolList;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "usuario")
    private ClienteEntity cliente;

    
}
