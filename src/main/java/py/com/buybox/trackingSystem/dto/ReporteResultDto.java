package py.com.buybox.trackingSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Calendar;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteResultDto {
    private Long peso;
    private Long precio;
    private Long volumen;
    private LocalDate fecha;
    private Integer idEstado;
    private Integer idSegmento;
}
