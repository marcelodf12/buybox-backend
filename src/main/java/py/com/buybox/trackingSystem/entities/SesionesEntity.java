package py.com.buybox.trackingSystem.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;

@Data
@Entity
@Table(name = "bb_sesiones")
public class SesionesEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "token", insertable = false, nullable = false)
    private String token;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity Usuario;

    @Column(name = "vencimiento", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar vencimiento;

    
}