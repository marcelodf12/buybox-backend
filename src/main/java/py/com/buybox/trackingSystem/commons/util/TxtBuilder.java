package py.com.buybox.trackingSystem.commons.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import py.com.buybox.trackingSystem.entities.ClienteEntity;
import py.com.buybox.trackingSystem.entities.PaqueteEntity;
import py.com.buybox.trackingSystem.entities.SucursalEntity;

import java.util.HashMap;
import java.util.Map;

public class TxtBuilder {

    protected final Log logger = LogFactory.getLog(this.getClass());

    private String textInit;

    private PaqueteEntity paquete;

    private SucursalEntity sucursal;

    private ClienteEntity cliente;

    private HashMap<String, String> otros = new HashMap<String, String>();

    public TxtBuilder(String _textInit, PaqueteEntity _paquete, SucursalEntity _sucursal, ClienteEntity _cliente, HashMap<String, String> _otros){
        this.textInit = _textInit;
        this.paquete = _paquete;
        this.sucursal = _sucursal;
        this.cliente = _cliente;
        this.otros = _otros;
    }

    private String replaceText(String text, String name, String mail, String body, String subject){
        logger.debug(text);
        text = text.replaceAll("%\\{NOMBRE}", name);
        text = text.replaceAll("%\\{MAIL}", mail);
        text = text.replaceAll("%\\{BODY}", body);
        text = text.replaceAll("%\\{ASUNTO}", subject);
        logger.debug(text);
        return text;
    }

    @Override
    public String toString() {
        String text = textInit;
        if(sucursal !=null) {
            text = text.replaceAll("%\\{SUCURSAL}", sucursal.getNombre());
        }
        if(paquete !=null){
            text = text.replaceAll("%\\{RASTREO}", paquete.getNumeroTracking());
            text = text.replaceAll("%\\{DESCRIPCION}", paquete.getDescripcion());
            text = text.replaceAll("%\\{PESO}", String.valueOf((float) paquete.getPeso()/1000));
            text = text.replaceAll("%\\{PRECIO}", String.valueOf((float) paquete.getMontoTotal()/100));
            text = text.replaceAll("%\\{CASILLA}", paquete.getCliente().getCasilla());
            text = text.replaceAll("%\\{LNG}", String.valueOf(paquete.getLng()));
            text = text.replaceAll("%\\{LAT}", String.valueOf(paquete.getLat()));
            text = text.replaceAll("%\\{CELULAR}", paquete.getCliente().getCelular());
            text = text.replaceAll("%\\{NOMBRE}", paquete.getCliente().getNombre() + " " + paquete.getCliente().getApellido());
        }
        if(otros !=null) {
            for (Map.Entry<String, String> entry : otros.entrySet()) {
                text = text.replaceAll(entry.getKey(), entry.getValue());
            }
        }
        return text;
    }
}
