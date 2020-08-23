package py.com.buybox.trackingSystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import py.com.buybox.trackingSystem.entities.UsuarioEntity;
import py.com.buybox.trackingSystem.repository.PermisoEntityRepository;
import py.com.buybox.trackingSystem.repository.UsuarioEntityRepository;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    protected static final Log logger = LogFactory.getLog(LoginFilter.class);

    UsuarioEntityRepository usuarioEntityRepository;

    PermisoEntityRepository permisoEntityRepository;

    JwtUtil jwtUtil;

    public LoginFilter(String url, AuthenticationManager authManager, ApplicationContext ctx) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
        usuarioEntityRepository = ctx.getBean(UsuarioEntityRepository.class);
        permisoEntityRepository = ctx.getBean(PermisoEntityRepository.class);
        jwtUtil = ctx.getBean(JwtUtil.class);

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException, IOException, ServletException {
        logger.debug("attemptAuthentication(HttpServletRequest req, HttpServletResponse res)");

        // obtenemos el body de la peticion que asumimos viene en formato JSON
        InputStream body = req.getInputStream();

        // Asumimos que el body tendrá el siguiente JSON  {"username":"ask", "password":"123"}
        // Realizamos un mapeo a nuestra clase User para tener ahi los datos
        UsuarioEntity user = new ObjectMapper().readValue(body, UsuarioEntity.class);

        // Finalmente autenticamos
        // Spring comparará el user/password recibidos
        // contra el que definimos en la clase SecurityConfig
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getCorreo(),
                        user.getPass(),
                        new ArrayList<>()
                )
        );
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse res, FilterChain chain,
            Authentication auth) throws IOException, ServletException {

        // Si la autenticacion fue exitosa, agregamos el token a la respuesta
        jwtUtil.addAuthentication(res, auth.getName(), auth);
    }
}
