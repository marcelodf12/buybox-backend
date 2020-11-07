package py.com.buybox.trackingSystem.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GeoDto {

    private BigDecimal lng;
    private BigDecimal lat;
}
