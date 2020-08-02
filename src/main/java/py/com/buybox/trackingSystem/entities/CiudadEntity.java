package py.com.buybox.trackingSystem.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "bb_ciudad")
@Data
@Entity
public class CiudadEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ciudad", insertable = false, nullable = false)
    private Integer idCiudad;

    @ManyToOne
    @JoinColumn(name = "id_departamento", nullable = false)
    private DepartamentoEntity departamento;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ciudad")
    private List<BarrioEntity> barrioList;


    
}