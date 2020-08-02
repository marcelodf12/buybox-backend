package py.com.buybox.trackingSystem.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Table(name = "bb_rol")
@Entity
public class RolEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, name = "id_rol", nullable = false)
    private Integer idRol;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "rol", nullable = false)
    private String rol;

    @ManyToMany(mappedBy = "rolList")
    private List<PermisoEntity> permisoList;

    @ManyToMany(mappedBy = "rolList")
    private List<UsuarioEntity> usuarioList;
    
}