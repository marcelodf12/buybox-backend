package py.com.buybox.trackingSystem.rest.backoffice;

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
import org.springframework.web.multipart.MultipartFile;
import py.com.buybox.trackingSystem.commons.constants.Constants;
import py.com.buybox.trackingSystem.commons.constants.HeadersCodes;
import py.com.buybox.trackingSystem.commons.dto.GeneralResponse;
import py.com.buybox.trackingSystem.commons.dto.Paginable;
import py.com.buybox.trackingSystem.commons.util.SortUtil;
import py.com.buybox.trackingSystem.dto.PaqueteDTO;
import py.com.buybox.trackingSystem.dto.PaqueteImportDto;
import py.com.buybox.trackingSystem.entities.PaqueteEntity;
import py.com.buybox.trackingSystem.repository.PaqueteEntityRepository;
import py.com.buybox.trackingSystem.repository.RastreoEntityRepository;
import py.com.buybox.trackingSystem.security.JwtUtil;
import py.com.buybox.trackingSystem.services.PaqueteImportService;
import py.com.buybox.trackingSystem.services.PaqueteService;
import py.com.buybox.trackingSystem.services.RastreoService;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("api/v1/paquete")
public class PaqueteBackOfficeRest {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private PaqueteEntityRepository paqueteEntityRepository;

    @Autowired
    private PaqueteImportService paqueteImportService;

    @Autowired
    private PaqueteService paqueteService;

    @Autowired
    private JwtUtil jwtUtil;

    @PreAuthorize("hasRole('LIST_PAQUETE')")
    @GetMapping()
    public ResponseEntity listarPaquetesRest(
            @RequestParam(defaultValue = "") String codigoInterno,
            @RequestParam(defaultValue = "") String codigoExterno,
            @RequestParam(defaultValue = "") String vuelo,
            @RequestParam(defaultValue = "") String casilla,
            @RequestParam(defaultValue = "") String numeroTracking,
            @RequestParam(defaultValue = "") String cliente,
            @RequestParam(defaultValue = "") String actual,
            @RequestParam(defaultValue = "") String destino,
            @RequestParam(defaultValue = "1999-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desde,
            @RequestParam(defaultValue = "2050-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate hasta,
            @RequestParam Integer idSucursal,
            @RequestParam(defaultValue = "0") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer perPage,
            @RequestParam(defaultValue = "*bc.casilla") String sorting
    ){
        GeneralResponse<List<PaqueteDTO>, Paginable> r = (new GeneralResponse<>());
        Page<PaqueteEntity> pagePaquete = null;
        try {
            logger.debug(sorting);
            pagePaquete = paqueteEntityRepository.findPaquete(
                    codigoExterno,
                    codigoInterno,
                    vuelo,
                    casilla,
                    idSucursal,
                    desde,
                    hasta.plusDays(1),
                    numeroTracking,
                    cliente,
                    actual,
                    destino,
                    PageRequest.of(currentPage, perPage, Sort.by(SortUtil.sortingList(sorting, "paquetes")))

            );
            logger.debug(pagePaquete);
        }catch (Exception e){
            logger.error(e);
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<PaqueteDTO> paquetes = PaqueteDTO.listFromEntity(pagePaquete.toList());
        r.setBody(paquetes);
        r.setMeta(new Paginable(pagePaquete));
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('DETAIL_PAQUETE')")
    @GetMapping("/{numeroTracking}")
    public ResponseEntity getPaquetesRest(
            @PathVariable String numeroTracking
    ){
        GeneralResponse<PaqueteDTO, Object> r = (new GeneralResponse<>());
        PaqueteEntity paqueteEntity = null;
        try {
            paqueteEntity = paqueteEntityRepository.findByNumeroTracking(numeroTracking);
        }catch (Exception e){
            logger.error(e);
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(paqueteEntity!=null){
            PaqueteDTO paqueteDTO = new PaqueteDTO(paqueteEntity, paqueteEntity.getRastreoList());
            r.setBody(paqueteDTO);
        }else{
            r.setHeader(HeadersCodes.PAQUETE_NOT_EXIST, true, Constants.LEVEL_ERROR, Constants.TYPE_TOAST);
            return new ResponseEntity<>(r, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @PostMapping("import")
    @PreAuthorize("hasRole('ALTA_PAQUETE')")
    public ResponseEntity importarPaquetesRest(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String token
    ){
        String mail = jwtUtil.getSubject(token);
        GeneralResponse<List<PaqueteImportDto>, Paginable> r = (new GeneralResponse<>());
        List<PaqueteImportDto> paquetes = this.paqueteImportService.preImport(file, mail);
        r.setBody(paquetes);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @PutMapping("{idPaquete}/move/{idSucursal}")
    @PreAuthorize("hasRole('MOVE_PAQUETE')")
    public ResponseEntity moverPaquete(
            @PathVariable(name = "idPaquete") Integer idPaquete,
            @PathVariable(name = "idSucursal") Integer idSucursal,
            @RequestHeader("Authorization") String token ){
        String mail = jwtUtil.getSubject(token);
        GeneralResponse<PaqueteDTO, Object> r = (new GeneralResponse<>());
        PaqueteDTO paquete = null;
        PaqueteEntity paqueteEntity = null;
        try {
            paqueteEntity = this.paqueteService.mover(idPaquete, idSucursal, mail);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e);
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(paqueteEntity==null){
            r.setHeader(HeadersCodes.ENTITY_NOT_EXIST, true, Constants.LEVEL_ERROR, Constants.TYPE_TOAST);
            return new ResponseEntity<>(r, HttpStatus.BAD_REQUEST);
        }else{
            paquete = new PaqueteDTO(paqueteEntity, paqueteEntity.getRastreoList());
            r.setBody(paquete);
            r.setHeader(HeadersCodes.EDICION_CORRECTA, true, Constants.LEVEL_SUCCESS, Constants.TYPE_TOAST);
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }


}
