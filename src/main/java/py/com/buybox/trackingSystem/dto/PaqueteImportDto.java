package py.com.buybox.trackingSystem.dto;

import lombok.Data;

@Data
public class PaqueteImportDto {

    private int orden;

    private PaqueteDTO p;

    private int result;

}
