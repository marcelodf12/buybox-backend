    package py.com.buybox.trackingSystem.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import py.com.buybox.trackingSystem.dto.ClienteDTO;
import py.com.buybox.trackingSystem.entities.ClienteEntity;
import py.com.buybox.trackingSystem.entities.SegmentoEntity;
import py.com.buybox.trackingSystem.entities.SucursalEntity;
import py.com.buybox.trackingSystem.repository.ClienteEntityRepository;
import py.com.buybox.trackingSystem.repository.SegmentoEntityRepository;
import py.com.buybox.trackingSystem.repository.SucursalEntityRepository;

import java.util.Optional;

@Service
public class ClienteService {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private ClienteEntityRepository clienteEntityRepository;

    @Autowired
    private SucursalEntityRepository sucursalEntityRepository;

    @Autowired
    private SegmentoEntityRepository segmentoEntityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ClienteEntity edit(ClienteDTO clienteDTO, String casilla){
        ClienteEntity clienteEntity = clienteEntityRepository.findByCasilla(casilla);
        logger.debug("Edit cliente: ");
        logger.debug(new ClienteDTO(clienteEntity));
        setProperties(clienteEntity, clienteDTO);
        return clienteEntityRepository.save(clienteEntity);
    }

    public ClienteEntity edit(ClienteDTO clienteDTO){
        Optional<ClienteEntity> clienteEntity = clienteEntityRepository.findById(clienteDTO.getIdCliente());
        if(clienteEntity.isPresent()){
            logger.debug("Edit cliente: ");
            logger.debug(new ClienteDTO(clienteEntity.get()));
            setProperties(clienteEntity.get(), clienteDTO);
            return clienteEntityRepository.save(clienteEntity.get());
        }
        return null;
    }

    private void setProperties(ClienteEntity clienteEntity, ClienteDTO clienteDTO){
        if(clienteDTO.getNombre()!=null)
            clienteEntity.setNombre(clienteDTO.getNombre());

        if(clienteDTO.getApellido()!=null)
            clienteEntity.setApellido(clienteDTO.getApellido());

        if(clienteDTO.getCelular()!=null)
            clienteEntity.setCelular(clienteDTO.getCelular());

        if(clienteDTO.getDireccion()!=null)
            clienteEntity.setDireccion(clienteDTO.getDireccion());

        if(clienteDTO.getRuc()!=null)
            clienteEntity.setRuc(clienteDTO.getRuc());

        if(clienteDTO.getRazonSocial()!=null)
            clienteEntity.setRazonSocial(clienteDTO.getRazonSocial());

        if(clienteDTO.getPass()!=null)
            clienteEntity.getUsuario().setPass(passwordEncoder.encode(clienteDTO.getPass()));

        if(clienteDTO.getIdSucursal()!=null){
            Optional<SucursalEntity> sucursalEntity = sucursalEntityRepository.findById(clienteDTO.getIdSucursal());
            if(sucursalEntity.isPresent())
                clienteEntity.setSucursal(sucursalEntity.get());
        }

        if(clienteDTO.getIdSegmento()!=null){
            Optional<SegmentoEntity> segmentoEntity = segmentoEntityRepository.findById(clienteDTO.getIdSegmento());
            if(segmentoEntity.isPresent())
                clienteEntity.setSegmento(segmentoEntity.get());
        }

        if(clienteEntity.getUsuario()!=null){
            if(clienteDTO.getNombre()!=null)
                clienteEntity.getUsuario().setNombre(clienteDTO.getNombre());

            if(clienteDTO.getApellido()!=null)
                clienteEntity.getUsuario().setApellido(clienteDTO.getApellido());
        }
    }
}
