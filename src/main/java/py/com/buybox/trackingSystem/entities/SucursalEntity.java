package py.com.buybox.trackingSystem.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "bb_sucursal")
@Data
public class SucursalEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, name = "id_sucursal", nullable = false)
    private Integer idSucursal;

    @Column(name = "editable", nullable = false)
    private Integer editable = 1;

    @ManyToOne
    @JoinColumn(name = "id_barrio")
    private BarrioEntity barrio;

    @ManyToOne
    @JoinColumn(name = "id_ciudad")
    private CiudadEntity ciudad;

    @ManyToOne
    @JoinColumn(name = "id_departamento")
    private DepartamentoEntity departamento;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_estado_defecto", nullable = false)
    private EstadoEntity estadoDefecto;

    @Column(name = "latitud")
    private Integer latitud;

    @Column(name = "longitud")
    private Integer longitud;

    @Column(name = "mensaje_al_cliente")
    private String mensajeAlCliente;

    @Column(name = "mensaje_al_cliente_final")
    private String mensajeAlClienteFinal;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "notificable_final")
    private Integer notificableFinal = 0;

    @Column(name = "notificable_llegada", nullable = false)
    private Integer notificableLlegada = 0;

    @Column(name = "rastreable", nullable = false)
    private Integer rastreable;

    @OneToMany(mappedBy="sucursal", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    private List<RastreoEntity> rastreoList;

    @OneToMany(mappedBy="sucursalDestino", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    private List<PaqueteEntity> paquetesDestinos;

    @OneToMany(mappedBy="sucursalActual", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    private List<PaqueteEntity> paquetesActuales;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sucursal", fetch=FetchType.LAZY)
    private List<ClienteEntity> clienteList;

    @Column(name = "is_final", nullable = true)
    private Integer isFinal;

    @Column(name = "is_delivery", nullable = true)
    private Integer isDelivery;

    @Column(name = "mail_delivery", nullable = true)
    private String mailDelivery;

    
}
