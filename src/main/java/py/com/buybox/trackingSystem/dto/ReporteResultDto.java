package py.com.buybox.trackingSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteResultDto {
    private BigDecimal peso;
    private BigDecimal precio;
    private BigDecimal volumen;
    private BigDecimal montoTotal;
    private LocalDate fecha;
    private String estado;
    private String segmento;

    public ReporteResultDto(long _peso, long _precio, long _volumen, long _montoTotal, LocalDate _fecha, String _estado, String _segmento){
        this.peso = BigDecimal.valueOf(_peso).divide( BigDecimal.valueOf(100));
        this.precio = new BigDecimal(_precio);
        this.volumen = BigDecimal.valueOf(_volumen).divide( BigDecimal.valueOf(1000000));
        this.montoTotal = BigDecimal.valueOf(_montoTotal).divide( BigDecimal.valueOf(100));
        this.fecha = _fecha;
        this.estado = _estado;
        this.segmento = _segmento;
    }
}
