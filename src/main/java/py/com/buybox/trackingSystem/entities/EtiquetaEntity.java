package py.com.buybox.trackingSystem.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "bb_etiqueta")
@Data
@Entity
public class EtiquetaEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(insertable = false, name = "clave", nullable = false)
    private String clave;

    @Column(name = "contexto", nullable = false)
    private String contexto;

    @Column(name = "editable", nullable = false)
    private Integer editable = 0;

    @Column(name = "valor")
    private String valor;

    
}