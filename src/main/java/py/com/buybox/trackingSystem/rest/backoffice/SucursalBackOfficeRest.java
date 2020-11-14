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
import org.springframework.web.bind.annotation.*;
import py.com.buybox.trackingSystem.commons.constants.Constants;
import py.com.buybox.trackingSystem.commons.constants.HeadersCodes;
import py.com.buybox.trackingSystem.commons.dto.GeneralResponse;
import py.com.buybox.trackingSystem.commons.dto.Paginable;
import py.com.buybox.trackingSystem.commons.util.SortUtil;
import py.com.buybox.trackingSystem.dto.ClienteDTO;
import py.com.buybox.trackingSystem.dto.SucursalDTO;
import py.com.buybox.trackingSystem.entities.ClienteEntity;
import py.com.buybox.trackingSystem.entities.EstadoEntity;
import py.com.buybox.trackingSystem.entities.SucursalEntity;
import py.com.buybox.trackingSystem.repository.EstadoEntityRepository;
import py.com.buybox.trackingSystem.repository.SucursalEntityRepository;

import java.util.List;

@RestController
@RequestMapping("api/v1/sucursal")
public class SucursalBackOfficeRest {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private SucursalEntityRepository sucursalEntityRepository;

    @Autowired
    private EstadoEntityRepository estadoEntityRepository;

    @PreAuthorize("hasRole('LIST_SUCURSAL')")
    @GetMapping()
    public ResponseEntity listarSucursales(){
        GeneralResponse<List<SucursalDTO>, Object> r = (new GeneralResponse<>());
        List<SucursalEntity> sucursalEntityList = null;
        try {
            sucursalEntityList = sucursalEntityRepository.findAll();
        }catch (Exception e){
            logger.error(e);
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<SucursalDTO> sucursales = SucursalDTO.listFromEntity(sucursalEntityList);
        r.setBody(sucursales);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('EDIT_SUCURSAL')")
    @PutMapping()
    public ResponseEntity saveSucursal(@RequestBody SucursalDTO sucursalDTO){
        GeneralResponse<SucursalDTO, Object> r = (new GeneralResponse<>());
        SucursalEntity sucursal;
        try {
            sucursal = sucursalEntityRepository.findById(sucursalDTO.getIdSucursal()).orElse(null);
            EstadoEntity estadoEntity = estadoEntityRepository.findById(sucursalDTO.getIdEstadoDefecto()).orElse(null);
            sucursal.modificar(sucursalDTO, estadoEntity);
            sucursalEntityRepository.save(sucursal);
        }catch (Exception e){
            logger.error(e);
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.NOT_FOUND);
        }
        r.setHeader(HeadersCodes.EDICION_CORRECTA, true, Constants.LEVEL_SUCCESS, Constants.TYPE_TOAST);
        r.setBody(new SucursalDTO(sucursal));
        return new ResponseEntity<>(r, HttpStatus.OK);
    }
}
