package py.com.buybox.trackingSystem.rest.backoffice;

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
import py.com.buybox.trackingSystem.dto.SucursalDTO;
import py.com.buybox.trackingSystem.entities.ClienteEntity;
import py.com.buybox.trackingSystem.entities.SucursalEntity;
import py.com.buybox.trackingSystem.repository.SucursalEntityRepository;

import java.util.List;

@RestController
@RequestMapping("api/v1/sucursal")
public class SucursalBackOfficeRest {

    @Autowired
    private SucursalEntityRepository sucursalEntityRepository;

    @PreAuthorize("hasRole('LIST_SUCURSAL')")
    @GetMapping()
    public ResponseEntity listarSucursales(){
        GeneralResponse<List<SucursalDTO>, Object> r = (new GeneralResponse<>());
        List<SucursalEntity> sucursalEntityList = null;
        try {
            sucursalEntityList = sucursalEntityRepository.findAll();
        }catch (Exception e){
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<SucursalDTO> sucursales = SucursalDTO.listFromEntity(sucursalEntityList);
        r.setBody(sucursales);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }
}
