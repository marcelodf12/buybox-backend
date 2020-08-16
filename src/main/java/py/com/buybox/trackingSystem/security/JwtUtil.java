package py.com.buybox.trackingSystem.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import py.com.buybox.trackingSystem.AppConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static java.util.Collections.emptyList;

public class JwtUtil {

    protected static final Log logger = LogFactory.getLog(JwtUtil.class);

    static AppConfig appConfig;

    // Método para crear el JWT y enviarlo al cliente en el header de la respuesta
    static void addAuthentication(HttpServletResponse res, String username) {

        logger.debug(appConfig.secret);

        String token = Jwts.builder()
                .setSubject(username)

                // Vamos a asignar un tiempo de expiracion de 1 minuto
                // solo con fines demostrativos en el video que hay al final
                .setExpiration(new Date(System.currentTimeMillis() + (appConfig.expiration*1000)))

                // Hash con el que firmaremos la clave
                .signWith(SignatureAlgorithm.HS512, appConfig.secret)
                .compact();

        //agregamos al encabezado el token
        res.addHeader("Authorization", "Bearer " + token);
    }

    // Método para validar el token enviado por el cliente
    static Authentication getAuthentication(HttpServletRequest request) {

        // Obtenemos el token que viene en el encabezado de la peticion
        String token = request.getHeader("Authorization");

        // si hay un token presente, entonces lo validamos
        if (token != null) {
            String user;
            try {
                user = Jwts.parser()
                        .setSigningKey(appConfig.secret)
                        .parseClaimsJws(token.replace("Bearer", "").trim()) //este metodo es el que valida
                        .getBody()
                        .getSubject();
            }catch (Exception e){
                return null;
            }
            // Recordamos que para las demás peticiones que no sean /login
            // no requerimos una autenticacion por username/password
            // por este motivo podemos devolver un UsernamePasswordAuthenticationToken sin password
            return user != null ?
                    new UsernamePasswordAuthenticationToken(user, null, emptyList()) :
                    null;
        }
        return null;
    }

    public static void setAppConfig ( AppConfig _appConfig ) {
        JwtUtil.appConfig = _appConfig;
    }

}
