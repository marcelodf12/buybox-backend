package py.com.buybox.trackingSystem.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "bb_segmento")
@Entity
@Data
public class SegmentoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_segmento", insertable = false, nullable = false)
    private Integer idSegmento;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "segmento")
    private List<ClienteEntity> clienteList;

    @OneToOne
    @JoinTable(
            name = "bb_precio_segmento",
            joinColumns = @JoinColumn(name = "id_segmento"),
            inverseJoinColumns = @JoinColumn(name = "id_precio"))
    private PrecioEntity precio;
    
}