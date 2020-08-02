package py.com.buybox.trackingSystem.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoDTO {
    private Integer idProducto;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private java.sql.Timestamp disponibleDesde;
    private java.sql.Timestamp disponibleHasta;
    private Integer stock;
    private Integer idCategoria;

}
