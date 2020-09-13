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
import py.com.buybox.trackingSystem.AppConfig;
import py.com.buybox.trackingSystem.commons.constants.Constants;
import py.com.buybox.trackingSystem.commons.constants.HeadersCodes;
import py.com.buybox.trackingSystem.commons.dto.GeneralResponse;
import py.com.buybox.trackingSystem.dto.UsuarioDTO;
import py.com.buybox.trackingSystem.entities.UsuarioEntity;
import py.com.buybox.trackingSystem.services.AuthenticationService;
import py.com.buybox.trackingSystem.services.SenderMailService;

import java.util.List;

@RestController
@RequestMapping("api/v1/client/authentication")
public class AuthenticationClientRest {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping()
    @RequestMapping("new/register")
    public ResponseEntity newRegister(@RequestBody UsuarioDTO usuarioDTO){
        GeneralResponse<UsuarioDTO, Object> r = new GeneralResponse<>();
        UsuarioEntity nuevoUser = null;

        if(!StringUtils.isAnyEmpty(usuarioDTO.getNombre(), usuarioDTO.getApellido(), usuarioDTO.getCorreo(), usuarioDTO.getPass())) {
            try {
                nuevoUser = authenticationService.registerNewUserAccount(usuarioDTO, List.of("CLIENTE")).getUsuario();
            }catch (DataIntegrityViolationException e){
                r.setHeader(HeadersCodes.DUPLICATED_USER, true, Constants.LEVEL_DANGER, Constants.TYPE_TOAST);
                return new ResponseEntity<>(r, HttpStatus.BAD_REQUEST);
            }catch (Exception e){
                return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            UsuarioDTO u = new UsuarioDTO(nuevoUser);
            u.setLinkDeRecuperacion("");
            r.setBody(u);
            r.setHeader(HeadersCodes.USER_CREATED_SUCCESS,true, Constants.LEVEL_SUCCESS, Constants.TYPE_TOAST);
        }else{
            r.setHeader(HeadersCodes.FIELD_MISSING, true, Constants.LEVEL_ERROR, Constants.TYPE_TOAST);
            return new ResponseEntity<>(r, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @GetMapping()
    @RequestMapping("confirmar/register")
    public ResponseEntity confirmarRegister(@RequestParam String check){
        GeneralResponse<UsuarioDTO, Object> r = new GeneralResponse<>();
        if(!StringUtils.isEmpty(check)) {
            UsuarioEntity u = this.authenticationService.confirmarRegistro(check);
            if (u != null) {
                UsuarioDTO uDto = new UsuarioDTO(u);
                uDto.setCasilla(u.getCliente().getCasilla());
                this.logger.debug(uDto);
                r.setBody(uDto);
                return new ResponseEntity<>(r, HttpStatus.OK);
            }else{
                this.logger.debug("Usuario no encontrado al intentar confirmar el registro");
            }
        }
        return new ResponseEntity<>(r, HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    @RequestMapping("new/recovery")
    public ResponseEntity newRecovery(@RequestBody UsuarioDTO usuarioDTO){
        GeneralResponse<Object, Object> r = new GeneralResponse<>();
        UsuarioEntity user = null;
        if(!StringUtils.isAnyEmpty(usuarioDTO.getCorreo())) {
            try {
                user = authenticationService.generateRecovery(usuarioDTO);
            }catch (Exception e){
                return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(user!=null){
                r.setHeader(HeadersCodes.GENERAL_SUCCESS,true, Constants.LEVEL_SUCCESS, Constants.TYPE_TOAST);
                return new ResponseEntity<>(r, HttpStatus.OK);
            }else{
                r.setHeader(HeadersCodes.EMAIL_NOT_EXIST,true, Constants.LEVEL_ERROR, Constants.TYPE_TOAST);
            }
        }else{
            r.setHeader(HeadersCodes.FIELD_MISSING, true, Constants.LEVEL_ERROR, Constants.TYPE_TOAST);
        }
        return new ResponseEntity<>(r, HttpStatus.BAD_REQUEST);
    }

    @PostMapping()
    @RequestMapping("confirmar/recovery")
    public ResponseEntity confirmarRecovery(@RequestBody UsuarioDTO usuarioDTO){
        GeneralResponse<Object, Object> r = new GeneralResponse<>();
        if(!StringUtils.isAnyEmpty(usuarioDTO.getCorreo(), usuarioDTO.getLinkDeRecuperacion())) {
            try {
                authenticationService.recovery(usuarioDTO);
            }catch (Exception e){
                return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            r.setHeader(HeadersCodes.GENERAL_SUCCESS,true, Constants.LEVEL_SUCCESS, Constants.TYPE_TOAST);
        }else{
            r.setHeader(HeadersCodes.FIELD_MISSING, true, Constants.LEVEL_ERROR, Constants.TYPE_TOAST);
            return new ResponseEntity<>(r, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

}
