package py.com.buybox.trackingSystem.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "bb_cliente")
@Entity
@Data
public class ClienteEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente", insertable = false, nullable = false)
    private Integer idCliente;

    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Column(name = "casilla")
    private String casilla;

    @Column(name = "celular")
    private String celular;

    @Column(name = "correo", unique = true)
    private String correo;

    @Column(name = "direccion")
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "id_segmento", nullable = false)
    private SegmentoEntity segmento;

    @ManyToOne
    @JoinColumn(name = "id_sucursal", nullable = false)
    private SucursalEntity sucursal;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "razon_social")
    private String razonSocial;

    @Column(name = "ruc")
    private String ruc;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente")
    private List<PaqueteEntity> paqueteList;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    
}
