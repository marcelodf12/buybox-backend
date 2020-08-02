package py.com.buybox.trackingSystem.commons.dto;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.Transient;
import java.io.Serializable;

public class GeneralResponse<T, M> implements Serializable {

    protected final Log logger = LogFactory.getLog(this.getClass());

    private ResponseHeader header;

    private T body;

    private M meta;

    public GeneralResponse() {
        header = new ResponseHeader();
    }

    public ResponseHeader getHeader() {
        return header;
    }

    public void setHeader(ResponseHeader header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public M getMeta() {
        return meta;
    }

    public void setMeta(M meta) {
        this.meta = meta;
    }

    @Transient
    public void setHeader(Integer code, Boolean show, String level, String type) {
        header.setCode(code);
        header.setShow(show);
        header.setLevel(level);
        header.setType(type);
    }

}
