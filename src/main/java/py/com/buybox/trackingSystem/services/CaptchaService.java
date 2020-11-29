package py.com.buybox.trackingSystem.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import py.com.buybox.trackingSystem.AppConfig;
import py.com.buybox.trackingSystem.commons.util.TxtBuilder;
import py.com.buybox.trackingSystem.dto.GoogleMail;
import py.com.buybox.trackingSystem.dto.GoogleResponse;
import py.com.buybox.trackingSystem.exceptions.InvalidReCaptchaException;
import py.com.buybox.trackingSystem.exceptions.ReCaptchaInvalidException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class CaptchaService {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private TemplateEngine htmlTemplateEngine;

    @Autowired
    private SenderMailService senderMailService;

    private static Pattern RESPONSE_PATTERN = Pattern.compile("[=\\.@A-Za-z:áéíóúäëïöüâêîôû~àèìòù%&0-9/\\d\\n\\r\\s\\t,\\+\\-\\*\\?¿\\!\\¡\\\"\\;_-]+");

    public void processResponse(GoogleMail response, String ip) throws InvalidReCaptchaException, ReCaptchaInvalidException, IOException, MessagingException {
        if(
                !responseSanityCheck(response.getApi()) ||
                !responseSanityCheck(response.getAsunto()) ||
                !responseSanityCheck(response.getCuerpo()) ||
                !responseSanityCheck(response.getEmail()) ||
                !responseSanityCheck(response.getNombre())
        ) {
            throw new InvalidReCaptchaException("Response contains invalid characters");
        }

        URI verifyUri = URI.create(String.format(
                "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s",
                appConfig.googleSecret, response.getApi(), ip));

        GoogleResponse googleResponse = restTemplate().getForObject(verifyUri, GoogleResponse.class);
        this.logger.info(response);

        if(!googleResponse.isSuccess() || googleResponse.getScore() < appConfig.googleThreshold) {
            throw new ReCaptchaInvalidException("reCaptcha was not successfully validated");
        }
        Locale locale = new Locale("es_ES");
        final Context ctx = new Context(locale);

        HashMap<String, String> cuerpo = new HashMap<>();
        cuerpo.put("%\\{NOMBRE}", response.getNombre());
        cuerpo.put("%\\{MAIL}", response.getEmail());
        cuerpo.put("%\\{BODY}", response.getCuerpo());
        cuerpo.put("%\\{ASUNTO}", response.getAsunto());

        ctx.setVariable("titulo", "Formulario de contacto");
        ctx.setVariable("cuerpo", (new TxtBuilder(appConfig.bodyContactoMail, null, null, null, cuerpo)).toString());
        final String htmlContent = this.htmlTemplateEngine.process("generic-template.html", ctx);
        senderMailService.sendEmail(this.appConfig.tituloFormularioContacto, htmlContent , this.appConfig.atencionMail);

        this.logger.info("Validación pasada");

    }

    private boolean responseSanityCheck(String response) {
        boolean r = StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
        this.logger.debug("Check Sanity: " + response + " -- " + r);
        return r;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
