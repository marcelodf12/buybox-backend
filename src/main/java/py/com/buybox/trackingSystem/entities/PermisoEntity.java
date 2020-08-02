package py.com.buybox.trackingSystem.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "bb_permiso")
public class PermisoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, name = "id_permiso", nullable = false)
    private Integer idPermiso;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "permiso", nullable = false)
    private String permiso;

    @ManyToMany
    @JoinTable(
            name = "bb_rol_permiso",
            joinColumns = @JoinColumn(name = "id_permiso"),
            inverseJoinColumns = @JoinColumn(name = "id_rol"))
    private List<RolEntity> rolList;

    
}