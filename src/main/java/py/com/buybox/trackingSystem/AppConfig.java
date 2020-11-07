package py.com.buybox.trackingSystem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySources({
        @PropertySource("classpath:configurations.properties"),
})
public class AppConfig {

    @Value("${jwt.secret}")
    public String secret;

    @Value("${jwt.expiration}")
    public Long expiration;

    @Value("${register.expiration.day}")
    public Integer registerExpirationDays;

    @Value("${register.secret}")
    public String registerSecret;

    @Value("${user.login.intentos}")
    public Integer loginIntentos;

    @Value("${user.login.block.min}")
    public Integer blockMin;

    @Value("${default.segmento}")
    public Integer defaultIdSegmento;

    @Value("${default.sucursal.destino}")
    public Integer defaultIdSucursalDestino;

    @Value("${default.sucursal.origen}")
    public Integer defaultIdSucursalOrigen;

    @Value("${prefix.casilla}")
    public String prefixCasilla;

    @Value("${mail.subject.registration}")
    public String subjectRegister;

    @Value("${mail.subject.recovery}")
    public String subjectRecovery;

    @Value("${paquete.mover.titulo}")
    public String tituloMoverPaquete;

    @Value("${paquete.recepcion.titulo}")
    public String tituloRecepcionPaquete;

    @Value("${mail.subject.contacto}")
    public String tituloFormularioContacto;

    @Value("${google.recaptcha.key.site}")
    public String googleSite;

    @Value("${google.recaptcha.key.secret}")
    public String googleSecret;

    @Value("${google.recaptcha.key.threshold}")
    public float googleThreshold;

    @Value("${atencion.mail}")
    public String atencionMail;

    @Value("${mail.body.contacto}")
    public String bodyContactoMail;

    @Value("${mail.contacto.success}")
    public String mailContactoSuccess;

    @Value("${mail.contacto.error}")
    public String mailContactoError;

    @Value("${delivery.title}")
    public String titleDelivery;

    @Value("${delivery.body}")
    public String bodyDelivery;


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
