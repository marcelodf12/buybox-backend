package py.com.buybox.trackingSystem.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "bb_estado")
public class EstadoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado", insertable = false, nullable = false)
    private Integer idEstado;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "etiqueta", nullable = false)
    private String etiqueta;

}