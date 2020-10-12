package py.com.buybox.trackingSystem.rest.commons;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import py.com.buybox.trackingSystem.AppConfig;
import py.com.buybox.trackingSystem.commons.dto.GeneralResponse;
import py.com.buybox.trackingSystem.dto.GoogleMail;
import py.com.buybox.trackingSystem.exceptions.InvalidReCaptchaException;
import py.com.buybox.trackingSystem.exceptions.ReCaptchaInvalidException;
import py.com.buybox.trackingSystem.services.CaptchaService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("public")
public class PublicRest {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    CaptchaService captchaService;

    @Autowired
    AppConfig appConfig;

    @PostMapping("mail")
    public ResponseEntity sendMail(GoogleMail googleMail, HttpServletRequest request) throws JsonProcessingException {
        GeneralResponse<String, String> r = (new GeneralResponse<>());
        String remoteAddr = request.getHeader("X-FORWARDED-FOR");
        try {
            captchaService.processResponse(googleMail, remoteAddr);
        }catch (Exception | InvalidReCaptchaException | ReCaptchaInvalidException e){
            r.setBody(appConfig.mailContactoError);
            this.logger.error(e);
            return new ResponseEntity<>(r, HttpStatus.BAD_REQUEST);
        }
        r.setBody(appConfig.mailContactoSuccess);
        return new ResponseEntity<>(r, HttpStatus.OK);
    };
}
