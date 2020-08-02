package py.com.buybox.trackingSystem.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "bb_barrio")
@Entity
@Data
public class BarrioEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, name = "id_barrio", nullable = false)
    private Integer idBarrio;

    @ManyToOne
    @JoinColumn(name = "id_ciudad", nullable = false)
    private CiudadEntity ciudad;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    
}