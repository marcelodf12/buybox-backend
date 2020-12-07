package py.com.buybox.trackingSystem.rest.backoffice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import py.com.buybox.trackingSystem.commons.dto.GeneralResponse;
import py.com.buybox.trackingSystem.commons.dto.Paginable;
import py.com.buybox.trackingSystem.commons.util.SortUtil;
import py.com.buybox.trackingSystem.dto.ClienteDTO;
import py.com.buybox.trackingSystem.dto.UsuarioDTO;
import py.com.buybox.trackingSystem.entities.ClienteEntity;
import py.com.buybox.trackingSystem.entities.UsuarioEntity;
import py.com.buybox.trackingSystem.repository.UsuarioEntityRepository;

import java.util.List;

@RestController
@RequestMapping("api/v1/usuario")
public class UsuarioBackOfficeRest {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private UsuarioEntityRepository usuarioEntityRepository;

    @PreAuthorize("hasRole('LIST_USER')")
    @GetMapping()
    public ResponseEntity getClientes(
            @RequestParam(defaultValue = "0") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer perPage
    ){
        GeneralResponse<List<UsuarioDTO>, Paginable> r = (new GeneralResponse<>());
        Page<UsuarioEntity> pageUsuario = null;
        try {
            pageUsuario = usuarioEntityRepository.findAllEmployees(PageRequest.of(currentPage, perPage));
        }catch (Exception e){
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<UsuarioDTO> usuarios = UsuarioDTO.listFromEntity(pageUsuario.toList());
        r.setBody(usuarios);
        r.setMeta(new Paginable(pageUsuario));
        return new ResponseEntity<>(r, HttpStatus.OK);
    }
}
