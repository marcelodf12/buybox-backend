package py.com.buybox.trackingSystem.entities;

import lombok.Data;

import javax.persistence.*;

@Table(name = "bb_casilla")
@Entity
@Data
public class CasillaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, name = "numero_casilla", nullable = false)
    private Integer numeroCasilla;

}
