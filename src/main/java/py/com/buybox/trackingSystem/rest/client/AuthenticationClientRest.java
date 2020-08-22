package py.com.buybox.trackingSystem.rest.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import py.com.buybox.trackingSystem.commons.constants.Constants;
import py.com.buybox.trackingSystem.commons.constants.HeadersCodes;
import py.com.buybox.trackingSystem.commons.dto.GeneralResponse;
import py.com.buybox.trackingSystem.dto.UsuarioDTO;
import py.com.buybox.trackingSystem.entities.UsuarioEntity;
import py.com.buybox.trackingSystem.services.AuthenticationService;

import java.util.List;

@RestController
@RequestMapping("api/v1/client/authentication")
public class AuthenticationClientRest {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping()
    @RequestMapping("register")
    public ResponseEntity register(@RequestBody UsuarioDTO usuarioDTO){
        GeneralResponse<UsuarioDTO, Object> r = new GeneralResponse<>();
        UsuarioEntity nuevoUser = null;

        if(!StringUtils.isAnyEmpty(usuarioDTO.getNombre(), usuarioDTO.getApellido(), usuarioDTO.getCorreo(), usuarioDTO.getPass())) {
            try {
                nuevoUser = authenticationService.registerNewUserAccount(usuarioDTO, List.of("CLIENTE"));
            }catch (DataIntegrityViolationException e){
                r.setHeader(HeadersCodes.DUPLICATED_USER, true, Constants.LEVEL_ERROR, Constants.TYPE_TOAST);
                return new ResponseEntity<>(r, HttpStatus.BAD_REQUEST);
            }catch (Exception e){
                return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            UsuarioDTO u = new UsuarioDTO(nuevoUser);
            u.setLinkDeRecuperacion("");
            r.setBody(u);
        }else{
            r.setHeader(HeadersCodes.FIELD_MISSING, true, Constants.LEVEL_ERROR, Constants.TYPE_TOAST);
            return new ResponseEntity<>(r, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }



}
