package py.com.buybox.trackingSystem.rest.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import py.com.buybox.trackingSystem.commons.constants.Constants;
import py.com.buybox.trackingSystem.commons.constants.HeadersCodes;
import py.com.buybox.trackingSystem.commons.dto.GeneralResponse;
import py.com.buybox.trackingSystem.dto.ClienteDTO;
import py.com.buybox.trackingSystem.entities.ClienteEntity;
import py.com.buybox.trackingSystem.repository.ClienteEntityRepository;
import py.com.buybox.trackingSystem.security.JwtUtil;

import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/client/perfil")
public class ProfileClientRest {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ClienteEntityRepository clienteEntityRepository;

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping()
    public ResponseEntity listarPaquetesRest(
            @RequestHeader("Authorization") String token
    ){
        GeneralResponse<ClienteDTO, Object> r = (new GeneralResponse<>());
        String casilla = jwtUtil.getClaim(token, "casilla");
        ClienteEntity clienteEntity = null;
        try {
            clienteEntity = clienteEntityRepository.findByCasilla(casilla);
        }catch (Exception e){
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(clienteEntity!=null){
            r.setHeader(HeadersCodes.GENERAL_SUCCESS, false, "", "");
            r.setBody(new ClienteDTO(clienteEntity));
        }else{
            r.setHeader(HeadersCodes.ENTITY_NOT_EXIST, true, Constants.LEVEL_WARN, Constants.TYPE_TOAST);
            return new ResponseEntity<>(r, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }
}