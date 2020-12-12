package py.com.buybox.trackingSystem.rest.backoffice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import py.com.buybox.trackingSystem.commons.constants.Constants;
import py.com.buybox.trackingSystem.commons.constants.HeadersCodes;
import py.com.buybox.trackingSystem.commons.dto.GeneralResponse;
import py.com.buybox.trackingSystem.commons.dto.Paginable;
import py.com.buybox.trackingSystem.dto.RolDTO;
import py.com.buybox.trackingSystem.dto.UsuarioDTO;
import py.com.buybox.trackingSystem.entities.UsuarioEntity;
import py.com.buybox.trackingSystem.repository.RolEntityRepository;
import py.com.buybox.trackingSystem.repository.UsuarioEntityRepository;
import py.com.buybox.trackingSystem.services.UsuarioService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/usuario")
public class UsuarioBackOfficeRest {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private UsuarioEntityRepository usuarioEntityRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolEntityRepository rolEntityRepository;

    @PreAuthorize("hasRole('LIST_USER')")
    @GetMapping()
    public ResponseEntity getUsers(
            @RequestParam(defaultValue = "0") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer perPage
    ){
        GeneralResponse<List<UsuarioDTO>, Paginable> r = (new GeneralResponse<>());
        Page<UsuarioEntity> pageUsuario = null;
        String roles = null;
        try {
            pageUsuario = usuarioEntityRepository.findAllEmployees(PageRequest.of(currentPage, perPage));
            roles = rolEntityRepository.findAllAdministrative().stream().map(rol -> rol.getRol()).collect(Collectors.joining(","));
            logger.debug(roles);
        }catch (Exception e) {
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<UsuarioDTO> usuarios = UsuarioDTO.listFromEntity(pageUsuario.toList());
        r.setBody(usuarios);
        r.setMeta(new Paginable(pageUsuario));
        HashMap<String, String> additionals = new HashMap();
        additionals.put("roles", roles);
        r.getHeader().setAdditionalParams(additionals);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('EDIT_USER')")
    @PutMapping("{id}")
    public ResponseEntity editarUser(
            @RequestBody UsuarioDTO userDto,
            @PathVariable(name = "id") Integer id
    ){
        GeneralResponse<UsuarioDTO, Object> r = (new GeneralResponse<>());
        UsuarioEntity usuarioEntity = null;
        userDto.setIdUsuario(id);
        try {
            usuarioEntity = usuarioService.edit(userDto);
        }catch (Exception e){
            logger.error(e);
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(usuarioEntity != null ) {
            r.setHeader(HeadersCodes.EDICION_CORRECTA, true, Constants.LEVEL_SUCCESS, Constants.TYPE_TOAST);
            r.setBody(new UsuarioDTO(usuarioEntity));
        }else {
            r.setHeader(HeadersCodes.ENTITY_NOT_EXIST, true, Constants.LEVEL_ERROR, Constants.TYPE_TOAST);
            return new ResponseEntity<>(r, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('EDIT_USER')")
    @PostMapping()
    public ResponseEntity nuevoUser(
            @RequestBody UsuarioDTO userDto
    ) {
        GeneralResponse<UsuarioDTO, Object> r = (new GeneralResponse<>());
        UsuarioEntity usuarioEntity = null;
        try {
            usuarioEntity = usuarioService.nuevo(userDto);
        }catch (DataIntegrityViolationException e){
            r.setHeader(HeadersCodes.DUPLICATED_USER, true, Constants.LEVEL_ERROR, Constants.TYPE_TOAST);
            return new ResponseEntity<>(r, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            logger.error(e);
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(usuarioEntity != null ) {
            r.setHeader(HeadersCodes.USER_CREATED_SUCCESS, true, Constants.LEVEL_SUCCESS, Constants.TYPE_TOAST);
            r.setBody(new UsuarioDTO(usuarioEntity));
        }else {
            r.setHeader(HeadersCodes.ENTITY_NOT_EXIST, true, Constants.LEVEL_ERROR, Constants.TYPE_TOAST);
            return new ResponseEntity<>(r, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(r, HttpStatus.OK);
    }
}
