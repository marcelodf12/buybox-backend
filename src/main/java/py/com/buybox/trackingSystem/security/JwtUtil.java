package py.com.buybox.trackingSystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import py.com.buybox.trackingSystem.AppConfig;
import py.com.buybox.trackingSystem.commons.constants.Constants;
import py.com.buybox.trackingSystem.entities.UsuarioEntity;
import py.com.buybox.trackingSystem.repository.UsuarioEntityRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    protected static final Log logger = LogFactory.getLog(JwtUtil.class);

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private UsuarioEntityRepository usuarioEntityRepository;

    // Método para crear el JWT y enviarlo al cliente en el header de la respuesta
    void addAuthentication(HttpServletResponse res, String username, Authentication auth) {
        logger.debug("addAuthentication(HttpServletResponse res, String username)");
        String casilla = "";
        UsuarioEntity usuario = usuarioEntityRepository.findByCorreo(username);
        if(usuario!=null && usuario.getCliente()!=null && usuario.getCliente().getCasilla()!=null){
            casilla = usuario.getCliente().getCasilla();
        }

        String permiso = auth.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.toString()).collect(Collectors.joining("|"));


        String token = Jwts.builder()
                .setSubject(username)

                // Vamos a asignar un tiempo de expiracion de 1 minuto
                // solo con fines demostrativos en el video que hay al final
                .setExpiration(new Date(System.currentTimeMillis() + (appConfig.expiration*1000)))

                // Hash con el que firmaremos la clave
                .signWith(SignatureAlgorithm.HS512, appConfig.secret)
                .claim(Constants.JWT_PERMISSION, permiso)
                .claim(Constants.JWT_CASILLA, casilla)
                .compact();

        //agregamos al encabezado el token
        res.addHeader("Authorization", "Bearer " + token);
    }

    // Método para validar el token enviado por el cliente
    Authentication getAuthentication(HttpServletRequest request) {
        if(request.getServletPath().compareTo("/error")!=0) {
            logger.debug("request.getServletPath(HttpServletRequest request)");
            logger.debug(request.getServletPath());

            // Obtenemos el token que viene en el encabezado de la peticion
            String token = request.getHeader("Authorization");
            logger.debug(token);

            // si hay un token presente, entonces lo validamos
            if (token != null) {
                String user;
                List<GrantedAuthority> permissions = new ArrayList<>();
                try {
                    Claims claims = Jwts.parser()
                            .setSigningKey(appConfig.secret)
                            .parseClaimsJws(token.replace("Bearer", "").trim())
                            .getBody();
                    user = claims.getSubject();
                    String permissionsStr = claims.get(Constants.JWT_PERMISSION).toString();
                    logger.debug(permissionsStr);
                    permissions = Arrays.stream(permissionsStr.split("\\|"))
                            .map(s -> "ROLE_" + s)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    logger.debug(permissions);
                } catch (Exception e) {
                    logger.error(e);
                    return null;
                }
                // Recordamos que para las demás peticiones que no sean /login
                // no requerimos una autenticacion por username/password
                // por este motivo podemos devolver un UsernamePasswordAuthenticationToken sin password
                return user != null ?
                        new UsernamePasswordAuthenticationToken(user, null, permissions) :
                        null;
            }
        }
        return null;
    }

    public String getClaim(String token, String claim){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(appConfig.secret)
                    .parseClaimsJws(token.replace("Bearer", "").trim())
                    .getBody();
            String claimStr = claims.get(claim).toString();
            logger.debug(claimStr);
            return claimStr;
        }catch (Exception e){
            logger.error(e);
            return null;
        }
    }

}
