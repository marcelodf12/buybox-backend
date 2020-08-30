package py.com.buybox.trackingSystem.commons.dto;

import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import py.com.buybox.trackingSystem.commons.constants.Constants;
import py.com.buybox.trackingSystem.commons.constants.HeadersCodes;

import java.beans.Transient;
import java.io.Serializable;

@Data
public class GeneralResponse<T, M> implements Serializable {

    protected final Log logger = LogFactory.getLog(this.getClass());

    private ResponseHeader header;

    private T body;

    private M meta;

    public GeneralResponse() {
        header = new ResponseHeader();
        header.setCode(HeadersCodes.GENERAL_SUCCESS);
        header.setShow(false);
    }

    public GeneralResponse(Exception e) {
        header = new ResponseHeader();
        header.setCode(HeadersCodes.GENERAL_ERROR);
        header.setDeveloperMessage(e.getMessage());
        header.setShow(true);
        header.setLevel(Constants.LEVEL_ERROR);
        header.setType(Constants.TYPE_TOAST);
    }

    @Transient
    public void setHeader(Integer code, Boolean show, String level, String type) {
        header.setCode(code);
        header.setShow(show);
        header.setLevel(level);
        header.setType(type);
    }

}
