package py.com.buybox.trackingSystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import py.com.buybox.trackingSystem.AppConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

public class JwtUtil {

    protected static final Log logger = LogFactory.getLog(JwtUtil.class);

    static AppConfig appConfig;

    // Método para crear el JWT y enviarlo al cliente en el header de la respuesta
    static void addAuthentication(HttpServletResponse res, String username) {

        logger.debug(appConfig.secret);

        /*
        ESTO hay que borrar
        */
        String permiso = "";
        if(username.compareTo("admin")==0)
            permiso = "ROLE_ADMIN";
        else
            permiso = "ROLE_EMPLOY";


        String token = Jwts.builder()
                .setSubject(username)

                // Vamos a asignar un tiempo de expiracion de 1 minuto
                // solo con fines demostrativos en el video que hay al final
                .setExpiration(new Date(System.currentTimeMillis() + (appConfig.expiration*1000)))

                // Hash con el que firmaremos la clave
                .signWith(SignatureAlgorithm.HS512, appConfig.secret)
                .claim("permissions", permiso)
                .compact();

        //agregamos al encabezado el token
        res.addHeader("Authorization", "Bearer " + token);
    }

    // Método para validar el token enviado por el cliente
    static Authentication getAuthentication(HttpServletRequest request) {

        logger.debug("request.getServletPath()");
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
                String permissionsStr = claims.get("permissions").toString();
                logger.debug(permissionsStr);
                permissions = Arrays.stream(permissionsStr.split("\\|"))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                logger.debug(permissions);
            }catch (Exception e){
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
        return null;
    }

    public static void setAppConfig ( AppConfig _appConfig ) {
        JwtUtil.appConfig = _appConfig;
    }

}
