package py.com.buybox.trackingSystem.rest.commons;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import py.com.buybox.trackingSystem.commons.constants.HeadersCodes;
import py.com.buybox.trackingSystem.commons.dto.GeneralResponse;
import py.com.buybox.trackingSystem.dto.*;
import py.com.buybox.trackingSystem.repository.*;
import py.com.buybox.trackingSystem.services.ConfigurationService;

@RestController
@RequestMapping("api/v1/commons")
public class ConfigurationRest {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    ConfigurationService configurationService;

    @GetMapping()
    public ResponseEntity listarConfiguracion(){
        GeneralResponse<ConfigurationDTO, Object> r = (new GeneralResponse<>());
        try {
            ConfigurationDTO c = configurationService.listarConfiguracion();
            r.setBody(c);
            r.setHeader(HeadersCodes.GENERAL_SUCCESS,false,"","");
        }catch (Exception e){
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }
}
