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
import py.com.buybox.trackingSystem.AppConfig;
import py.com.buybox.trackingSystem.commons.constants.EntitiesValues;
import py.com.buybox.trackingSystem.entities.UsuarioEntity;
import py.com.buybox.trackingSystem.repository.PermisoEntityRepository;
import py.com.buybox.trackingSystem.repository.UsuarioEntityRepository;

import java.util.Calendar;
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
    AppConfig appConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        logger.debug("Authentication authenticate(Authentication authentication)");

        String name = authentication.getName();

        UsuarioEntity usuario = usuarioEntityRepository.findByCorreo(name);

        Calendar cal = Calendar.getInstance();

        if (    usuario!=null &&
                usuario.getActivo()==EntitiesValues.USUARIO_ACTIVO &&
                passwordEncoder.matches(authentication.getCredentials().toString(), usuario.getPass()) &&
                (usuario.getBloqueadoHasta()==null || cal.after(usuario.getBloqueadoHasta()))
        ) {
            List<GrantedAuthority> grantedAuthorityList = permisoEntityRepository.findByUsuario(usuario.getIdUsuario()).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            usuario.setIntentosFallidos(0);
            usuario.setBloqueadoHasta(null);
            usuarioEntityRepository.save(usuario);
            return new UsernamePasswordAuthenticationToken(
                    name, usuario.getPass(), grantedAuthorityList);
        } else {
            if(usuario!=null){
                usuario.setIntentosFallidos(usuario.getIntentosFallidos()+1);
                logger.debug("Login failed: Intentos=" + usuario.getIntentosFallidos() + " AppConfigIntentos=" + appConfig.loginIntentos );
                if(usuario.getIntentosFallidos()>=appConfig.loginIntentos){
                    cal.add(Calendar.MINUTE, appConfig.blockMin);
                    usuario.setBloqueadoHasta(cal);
                    usuario.setIntentosFallidos(0);
                }
                usuarioEntityRepository.save(usuario);
            }
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
