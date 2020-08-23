package py.com.buybox.trackingSystem.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import py.com.buybox.trackingSystem.commons.constants.EntitiesValues;
import py.com.buybox.trackingSystem.entities.UsuarioEntity;
import py.com.buybox.trackingSystem.repository.PermisoEntityRepository;
import py.com.buybox.trackingSystem.repository.UsuarioEntityRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthProvider implements AuthenticationProvider {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    UsuarioEntityRepository usuarioEntityRepository;

    @Autowired
    PermisoEntityRepository permisoEntityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        logger.debug("Authentication authenticate(Authentication authentication)");

        String name = authentication.getName();

        UsuarioEntity usuario = usuarioEntityRepository.findByCorreo(name);

        logger.debug(authentication.getCredentials().toString());
        logger.debug(usuario.getPass());

        if (usuario!=null && usuario.getActivo()==EntitiesValues.USUARIO_ACTIVO && passwordEncoder.matches(authentication.getCredentials().toString(), usuario.getPass())) {
            List<GrantedAuthority> grantedAuthorityList = permisoEntityRepository.findByUsuario(usuario.getIdUsuario()).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(
                    name, usuario.getPass(), grantedAuthorityList);
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
