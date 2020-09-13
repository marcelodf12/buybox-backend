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
import py.com.buybox.trackingSystem.commons.dto.GeneralResponse;
import py.com.buybox.trackingSystem.commons.dto.Paginable;
import py.com.buybox.trackingSystem.commons.util.SortUtil;
import py.com.buybox.trackingSystem.dto.PaqueteDTO;
import py.com.buybox.trackingSystem.dto.PaqueteImportDto;
import py.com.buybox.trackingSystem.entities.PaqueteEntity;
import py.com.buybox.trackingSystem.repository.PaqueteEntityRepository;
import py.com.buybox.trackingSystem.security.JwtUtil;
import py.com.buybox.trackingSystem.services.PaqueteImportService;

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
                    PageRequest.of(currentPage, perPage, Sort.by(SortUtil.sortingList(sorting, "paquetes")))

            );
            logger.debug(pagePaquete);
        }catch (Exception e){
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<PaqueteDTO> paquetes = PaqueteDTO.listFromEntity(pagePaquete.toList());
        r.setBody(paquetes);
        r.setMeta(new Paginable(pagePaquete));
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


}
