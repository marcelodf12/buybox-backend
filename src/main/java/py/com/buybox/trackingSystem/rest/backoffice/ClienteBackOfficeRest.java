package py.com.buybox.trackingSystem.rest.backoffice;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import py.com.buybox.trackingSystem.commons.constants.Constants;
import py.com.buybox.trackingSystem.commons.constants.HeadersCodes;
import py.com.buybox.trackingSystem.commons.dto.GeneralResponse;
import py.com.buybox.trackingSystem.commons.dto.Paginable;
import py.com.buybox.trackingSystem.commons.util.SortUtil;
import py.com.buybox.trackingSystem.dto.ClienteDTO;
import py.com.buybox.trackingSystem.dto.PaqueteDTO;
import py.com.buybox.trackingSystem.entities.ClienteEntity;
import py.com.buybox.trackingSystem.entities.PaqueteEntity;
import py.com.buybox.trackingSystem.repository.ClienteEntityRepository;
import py.com.buybox.trackingSystem.services.ClienteService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/cliente")
public class ClienteBackOfficeRest {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private ClienteEntityRepository clienteEntityRepository;

    @Autowired
    private ClienteService clienteService;

    @PreAuthorize("hasRole('LIST_CLIENTE')")
    @GetMapping()
    public ResponseEntity listarPaquetesRest(
            @RequestParam(defaultValue = "") String casilla,
            @RequestParam(defaultValue = "") String cliente,
            @RequestParam(defaultValue = "") String cedula,
            @RequestParam(defaultValue = "") String correo,
            @RequestParam(defaultValue = "0") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer perPage,
            @RequestParam(defaultValue = "*bc.casilla") String sorting
    ){
        GeneralResponse<List<ClienteDTO>, Paginable> r = (new GeneralResponse<>());
        Page<ClienteEntity> pageCliente = null;
        try {
            logger.debug(sorting);
            pageCliente = clienteEntityRepository.findCliente(
                    casilla,
                    cliente,
                    cedula,
                    correo,
                    PageRequest.of(currentPage, perPage, Sort.by(SortUtil.sortingList(sorting, "clientes")))
            );
            logger.debug(pageCliente);
        }catch (Exception e){
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<ClienteDTO> clientes = ClienteDTO.listFromEntity(pageCliente.toList());
        r.setBody(clientes);
        r.setMeta(new Paginable(pageCliente));
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('EDIT_CLIENTE')")
    @PutMapping()
    public ResponseEntity editarClient(
            @RequestBody ClienteDTO cliente
    ){
        GeneralResponse<ClienteDTO, Object> r = (new GeneralResponse<>());
        ClienteEntity clienteEntity = null;
        try {
            clienteEntity = clienteService.edit(cliente);
        }catch (Exception e){
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        r.setHeader(HeadersCodes.EDICION_CORRECTA, true, Constants.LEVEL_SUCCESS, Constants.TYPE_TOAST);
        r.setBody(new ClienteDTO(clienteEntity));
        return new ResponseEntity<>(r, HttpStatus.OK);
    }
}
