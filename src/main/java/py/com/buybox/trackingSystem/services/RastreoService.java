package py.com.buybox.trackingSystem.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import py.com.buybox.trackingSystem.AppConfig;
import py.com.buybox.trackingSystem.entities.PaqueteEntity;
import py.com.buybox.trackingSystem.entities.RastreoEntity;
import py.com.buybox.trackingSystem.entities.SucursalEntity;
import py.com.buybox.trackingSystem.entities.UsuarioEntity;
import py.com.buybox.trackingSystem.repository.PaqueteEntityRepository;
import py.com.buybox.trackingSystem.repository.SucursalEntityRepository;
import py.com.buybox.trackingSystem.repository.UsuarioEntityRepository;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


@Service
public class RastreoService {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private PaqueteEntityRepository paqueteEntityRepository;

    @Autowired
    private SucursalEntityRepository sucursalEntityRepository;

    @Autowired
    private UsuarioEntityRepository usuarioEntityRepository;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private SenderMailService senderMailService;

    @Autowired
    private TemplateEngine htmlTemplateEngine;


    public PaqueteEntity mover(PaqueteEntity paqueteEntity, SucursalEntity sucursalEntity, UsuarioEntity userEntity) throws IOException, MessagingException {
        if(sucursalEntity!=null && paqueteEntity!=null && userEntity!=null){
            logger.debug("Rastreo Service mover");
            RastreoEntity rastreo = new RastreoEntity();
            rastreo.setPaquete(paqueteEntity);
            rastreo.setSucursal(sucursalEntity);
            Calendar calendar = Calendar.getInstance();
            rastreo.setFechaHora(calendar);
            rastreo.setUsuario(userEntity);
            if(paqueteEntity.getRastreoList()==null) paqueteEntity.setRastreoList(new ArrayList<>());
            paqueteEntity.getRastreoList().add(rastreo);
            paqueteEntity.setSucursalActual(sucursalEntity);
            paqueteEntity.setEstado(sucursalEntity.getEstadoDefecto());
            Locale locale = new Locale("es_ES");
            final Context ctx = new Context(locale);

            if( sucursalEntity.getNotificableFinal()==1 &&
                    paqueteEntity.getCliente()!=null &&
                    paqueteEntity.getSucursalDestino() != null &&
                    sucursalEntity.getIdSucursal() == paqueteEntity.getSucursalDestino().getIdSucursal()
            ){
                ctx.setVariable("titulo", this.appConfig.tituloRecepcionPaquete);
                ctx.setVariable("cuerpo", this.replaceText(sucursalEntity.getMensajeAlClienteFinal(), paqueteEntity, sucursalEntity));
                final String htmlContent = this.htmlTemplateEngine.process("generic-template.html", ctx);
                senderMailService.sendEmail(this.appConfig.tituloRecepcionPaquete, htmlContent ,paqueteEntity.getCliente().getCorreo());
            } else if( sucursalEntity.getNotificableLlegada()==1 &&
                paqueteEntity.getCliente()!=null
            ){
                ctx.setVariable("titulo", this.appConfig.tituloMoverPaquete);
                ctx.setVariable("cuerpo", this.replaceText(sucursalEntity.getMensajeAlCliente(), paqueteEntity,sucursalEntity));
                final String htmlContent = this.htmlTemplateEngine.process("generic-template.html", ctx);
                senderMailService.sendEmail(this.appConfig.tituloMoverPaquete, htmlContent ,paqueteEntity.getCliente().getCorreo());
            }



            return paqueteEntity;
        }else{
            logger.debug("Alguna entidad en null");
            return null;
        }

    }

    private String replaceText(String text, PaqueteEntity paqueteEntity, SucursalEntity sucursalEntity){
        logger.debug(text);
        text = text.replaceAll("##SUCURSAL##", sucursalEntity.getNombre());
        text = text.replaceAll("##RASTREO##", paqueteEntity.getNumeroTracking());
        text = text.replaceAll("##DESCRIPCION##", paqueteEntity.getDescripcion());
        text = text.replaceAll("##PESO##", String.valueOf((float)paqueteEntity.getPeso()/1000));
        text = text.replaceAll("##PRECIO##", String.valueOf((float)paqueteEntity.getMontoTotal()/100));
        logger.debug(text);
        return text;
    }


}
