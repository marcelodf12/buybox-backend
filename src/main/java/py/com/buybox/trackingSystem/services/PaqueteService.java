package py.com.buybox.trackingSystem.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import py.com.buybox.trackingSystem.AppConfig;
import py.com.buybox.trackingSystem.commons.util.TxtBuilder;
import py.com.buybox.trackingSystem.dto.GeoDto;
import py.com.buybox.trackingSystem.dto.PaqueteDTO;
import py.com.buybox.trackingSystem.entities.PaqueteEntity;
import py.com.buybox.trackingSystem.entities.SucursalEntity;
import py.com.buybox.trackingSystem.entities.UsuarioEntity;
import py.com.buybox.trackingSystem.repository.PaqueteEntityRepository;
import py.com.buybox.trackingSystem.repository.SucursalEntityRepository;
import py.com.buybox.trackingSystem.repository.UsuarioEntityRepository;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Locale;

@Service
public class PaqueteService {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private PaqueteEntityRepository paqueteEntityRepository;

    @Autowired
    private SucursalEntityRepository sucursalEntityRepository;

    @Autowired
    private UsuarioEntityRepository usuarioEntityRepository;

    @Autowired SenderMailService senderMailService;

    @Autowired
    private RastreoService rastreoService;

    @Autowired
    private TemplateEngine htmlTemplateEngine;

    @Autowired
    private AppConfig appConfig;

    public PaqueteEntity mover(Integer idPaquete, Integer idSucursal, String userMail) throws IOException, MessagingException {
        SucursalEntity sucursalEntity = sucursalEntityRepository.findById(idSucursal).orElse(null);
        PaqueteEntity paqueteEntity = paqueteEntityRepository.findById(idPaquete).orElse(null);
        UsuarioEntity userEntity = usuarioEntityRepository.findByCorreo(userMail);
        if(sucursalEntity!=null && paqueteEntity!=null && userEntity!=null){
            paqueteEntity = this.rastreoService.mover(paqueteEntity, sucursalEntity, userEntity);
            return paqueteEntityRepository.save(paqueteEntity);
        }else{
            return null;
        }
    }

    public PaqueteEntity delivery(GeoDto geo, Integer idPaquete, String casilla) throws IOException, MessagingException {
        PaqueteEntity paqueteEntity = paqueteEntityRepository.findById(idPaquete).orElse(null);
        if(paqueteEntity != null && casilla.compareTo(paqueteEntity.getCliente().getCasilla()) == 0 ){
            paqueteEntity.setLat(geo.getLat());
            paqueteEntity.setLng(geo.getLng());
            Locale locale = new Locale("es_ES");
            final Context ctx = new Context(locale);
            String titulo = (new TxtBuilder(this.appConfig.titleDelivery, paqueteEntity, null, null, null)).toString();
            ctx.setVariable("titulo", titulo);
            ctx.setVariable("cuerpo", (new TxtBuilder(this.appConfig.bodyDelivery, paqueteEntity, null, null, null)).toString());
            final String htmlContent = this.htmlTemplateEngine.process("generic-template.html", ctx);
            senderMailService.sendEmail(titulo, htmlContent ,paqueteEntity.getCliente().getCorreo());
            return paqueteEntityRepository.save(paqueteEntity);
        }else{
            return null;
        }
    }

    public PaqueteEntity autorizar(PaqueteDTO paqueteDTO, Integer idPaquete, String casilla) {
        PaqueteEntity paqueteEntity = paqueteEntityRepository.findById(idPaquete).orElse(null);
        if(paqueteEntity != null && casilla.compareTo(paqueteEntity.getCliente().getCasilla()) == 0 ){
            paqueteEntity.setAutorizadoNombre(paqueteDTO.getAutorizadoNombre());
            paqueteEntity.setAutorizadoDocumento(paqueteDTO.getAutorizadoDocumento());
            return paqueteEntityRepository.save(paqueteEntity);
        }else{
            return null;
        }
    }

}
