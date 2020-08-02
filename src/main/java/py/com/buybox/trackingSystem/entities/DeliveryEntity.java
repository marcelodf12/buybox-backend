package py.com.buybox.trackingSystem.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "bb_delivery")
@Data
public class DeliveryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_delivery", insertable = false, nullable = false)
    private Integer idDelivery;

    @ManyToOne
    @JoinColumn(name = "id_barrio", nullable = false)
    private BarrioEntity barrio;

    @ManyToOne
    @JoinColumn(name = "id_ciudad", nullable = false)
    private CiudadEntity ciudad;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private ClienteEntity cliente;

    @ManyToOne
    @JoinColumn(name = "id_departamento", nullable = false)
    private DepartamentoEntity departamento;

    @OneToOne
    @JoinColumn(name = "id_paquete", nullable = false)
    private PaqueteEntity paquete;

    @Column(name = "latitud")
    private Integer latitud;

    @Column(name = "longitud")
    private Integer longitud;

    @Column(name = "referencia")
    private Integer referencia;

    
}