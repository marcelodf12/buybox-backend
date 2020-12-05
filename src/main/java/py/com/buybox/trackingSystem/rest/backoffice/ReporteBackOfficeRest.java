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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import py.com.buybox.trackingSystem.commons.dto.GeneralResponse;
import py.com.buybox.trackingSystem.commons.dto.Paginable;
import py.com.buybox.trackingSystem.commons.util.SortUtil;
import py.com.buybox.trackingSystem.dto.PaqueteDTO;
import py.com.buybox.trackingSystem.dto.ReporteResultDto;
import py.com.buybox.trackingSystem.entities.PaqueteEntity;
import py.com.buybox.trackingSystem.repository.PaqueteEntityRepository;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/reporte")
public class ReporteBackOfficeRest {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private PaqueteEntityRepository paqueteEntityRepository;

    @PreAuthorize("hasRole('GET_REPORTE')")
    @GetMapping() public ResponseEntity getReporte(
            @RequestParam(defaultValue = "1999-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desde,
            @RequestParam(defaultValue = "2050-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate hasta
    ){
        GeneralResponse<List<ReporteResultDto>, Object> r = (new GeneralResponse<>());
        List<ReporteResultDto> listPaquete = null;
        try {
            listPaquete = paqueteEntityRepository.reporte(desde, hasta);
        }catch (Exception e){
            logger.error(e);
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        r.setBody(listPaquete);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }
}
