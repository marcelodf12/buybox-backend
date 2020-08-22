package py.com.buybox.trackingSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import py.com.buybox.trackingSystem.commons.util.RandomUtil;
import py.com.buybox.trackingSystem.dto.UsuarioDTO;
import py.com.buybox.trackingSystem.entities.UsuarioEntity;
import py.com.buybox.trackingSystem.repository.RolEntityRepository;
import py.com.buybox.trackingSystem.repository.UsuarioEntityRepository;

import java.util.Calendar;
import java.util.List;

@Service
public class AuthenticationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolEntityRepository rolEntityRepository;

    @Autowired
    private UsuarioEntityRepository usuarioEntityRepository;

    public UsuarioEntity registerNewUserAccount(UsuarioDTO accountDto, List<String> roles) throws DataIntegrityViolationException {

        UsuarioEntity user = UsuarioDTO.newEntity(accountDto);

        Calendar vencimiento = Calendar.getInstance();
        vencimiento.add(Calendar.DAY_OF_YEAR, 2);
        user.setLinkFechaVencimiento(vencimiento);

        user.setLinkDeRecuperacion(RandomUtil.stringRandom(256));

        user.setActivo(0);
        user.setIntentosFallidos(0);
        user.setPass(passwordEncoder.encode(accountDto.getPass()));

        user.setRolList(this.rolEntityRepository.findRolIn(roles));

        return usuarioEntityRepository.save(user);

    }

}
