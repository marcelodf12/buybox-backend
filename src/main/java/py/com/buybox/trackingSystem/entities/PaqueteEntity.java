package py.com.buybox.trackingSystem.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

@Table(name = "bb_paquete")
@Entity
@Data
public class PaqueteEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paquete", insertable = false, nullable = false)
    private Integer idPaquete;

    @Column(name = "altura")
    private Integer altura;

    @Column(name = "ancho")
    private Integer ancho;

    @Column(name = "autorizado_documento")
    private String autorizadoDocumento;

    @Column(name = "autorizado_nombre")
    private String autorizadoNombre;

    @Column(name = "codigo_externo")
    private String codigoExterno;

    @Column(name = "codigo_interno")
    private String codigoInterno;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private ClienteEntity cliente;

    @ManyToOne
    @JoinColumn(name = "id_sucursal_destino", nullable = false)
    private SucursalEntity sucursalDestino;

    @ManyToOne
    @JoinColumn(name = "id_sucursal_actual", nullable = false)
    private SucursalEntity sucursalActual;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private EstadoEntity estado;

    @Column(name = "longitud")
    private Integer longitud;

    @Column(name = "monto_total")
    private Long montoTotal;

    @Column(name = "numero_tracking")
    private String numeroTracking;

    @Column(name = "peso")
    private Integer peso;

    @Column(name = "precio")
    private Integer precio;

    @Column(name = "volumen")
    private Integer volumen;

    @Column(name = "vuelo")
    private String vuelo;

    @OneToOne(mappedBy="paquete", cascade = CascadeType.ALL)
    private DeliveryEntity delivery;

    @OneToMany(mappedBy="paquete", cascade = CascadeType.ALL)
    private List<RastreoEntity> rastreoList;

    @Column(name = "ingreso", columnDefinition = "DATE")
    private LocalDate ingreso;
    
}
