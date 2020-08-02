package py.com.buybox.trackingSystem.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;

@Entity
@Table(name = "bb_archivo")
@Data
public class ArchivoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_archivo", insertable = false, nullable = false)
    private Integer idArchivo;

    @Column(name = "contexto", nullable = false)
    private String contexto;

    @Column(name = "entidad", nullable = false)
    private Integer entidad;

    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar fechaCreacion;

    @Column(name = "hash", nullable = false)
    private String hash;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    
}