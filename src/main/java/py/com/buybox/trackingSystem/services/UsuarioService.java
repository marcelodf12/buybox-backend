package py.com.buybox.trackingSystem.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import py.com.buybox.trackingSystem.dto.UsuarioDTO;
import py.com.buybox.trackingSystem.entities.RolEntity;
import py.com.buybox.trackingSystem.entities.UsuarioEntity;
import py.com.buybox.trackingSystem.repository.RolEntityRepository;
import py.com.buybox.trackingSystem.repository.UsuarioEntityRepository;

import java.util.*;

@Service
public class UsuarioService {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private UsuarioEntityRepository usuarioEntityRepository;

    @Autowired
    private RolEntityRepository rolEntityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioEntity edit(UsuarioDTO usuarioDTO){
        UsuarioEntity usuarioEntity = usuarioEntityRepository.findById(usuarioDTO.getIdUsuario()).orElse(null);
        if(usuarioEntity != null){
            setProperties(usuarioEntity, usuarioDTO);
            usuarioEntityRepository.save(usuarioEntity);
        }
        return usuarioEntity;
    }

    public UsuarioEntity nuevo(UsuarioDTO usuarioDTO){
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setCorreo(usuarioDTO.getCorreo());
        if(usuarioDTO.getRoles() == null) {
            usuarioDTO.setRoles(new ArrayList<>());
        }
        setProperties(usuarioEntity, usuarioDTO);
        usuarioEntityRepository.save(usuarioEntity);
        return usuarioEntity;
    }

    private void setProperties(UsuarioEntity usuarioEntity, UsuarioDTO usuarioDTO){
        logger.debug(usuarioDTO);
        if(usuarioDTO.getNombre()!=null) usuarioEntity.setNombre(usuarioDTO.getNombre());
        if(usuarioDTO.getApellido()!=null) usuarioEntity.setApellido(usuarioDTO.getApellido());
        if(usuarioDTO.getActivo()!=null) usuarioEntity.setActivo(usuarioDTO.getActivo());
        usuarioEntity.setIntentosFallidos(0);
        usuarioEntity.setBloqueadoHasta(null);
        if(usuarioDTO.getPass()!=null) usuarioEntity.setPass(passwordEncoder.encode(usuarioDTO.getPass()));
        if(usuarioDTO.getRoles() != null && usuarioDTO.getActivo() >= 0){
            usuarioDTO.getRoles().add("USUARIO");
            usuarioEntity.setRolList(rolEntityRepository.findRolIn(usuarioDTO.getRoles()));
        }else{
            usuarioEntity.setRolList(null);
        }
    }
}
