package py.com.buybox.trackingSystem.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.com.buybox.trackingSystem.entities.PaqueteEntity;
import py.com.buybox.trackingSystem.entities.RastreoEntity;
import py.com.buybox.trackingSystem.entities.SucursalEntity;
import py.com.buybox.trackingSystem.entities.UsuarioEntity;
import py.com.buybox.trackingSystem.repository.PaqueteEntityRepository;
import py.com.buybox.trackingSystem.repository.SucursalEntityRepository;
import py.com.buybox.trackingSystem.repository.UsuarioEntityRepository;

import javax.mail.MessagingException;
import java.awt.image.Raster;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

@Service
public class PaqueteService {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private PaqueteEntityRepository paqueteEntityRepository;

    @Autowired
    private SucursalEntityRepository sucursalEntityRepository;

    @Autowired
    private UsuarioEntityRepository usuarioEntityRepository;

    @Autowired
    private RastreoService rastreoService;

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
}
