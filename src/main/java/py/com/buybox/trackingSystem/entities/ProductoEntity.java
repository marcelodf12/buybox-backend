package py.com.buybox.trackingSystem.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Calendar;

@Entity
@Table(name = "bb_producto")
@Data
public class ProductoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto", insertable = false, nullable = false)
    private Integer idProducto;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "disponible_desde", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar disponibleDesde;

    @Column(name = "disponible_hasta", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar disponibleHasta;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaEntity categoria;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "precio", nullable = false)
    private BigDecimal precio;

    @Column(name = "stock")
    private Integer stock;

    
}