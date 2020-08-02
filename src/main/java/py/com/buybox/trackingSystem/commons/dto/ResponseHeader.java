package py.com.buybox.trackingSystem.commons.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

@Data
public class ResponseHeader implements Serializable {

    private Integer code;

    private Boolean show;

    private String type;

    private String level;

    private HashMap<String, String> additionalParams;

    public ResponseHeader() {
        additionalParams = new HashMap<>();
    }

}
