package py.com.buybox.trackingSystem.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "bb_precio")
@Entity
@Data
public class PrecioEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_precio", insertable = false, nullable = false)
    private Integer idPrecio;

    @Column(name = "formula", nullable = false)
    private String formula;

    @OneToOne(mappedBy = "precio")
    private SegmentoEntity segmento;
    
}