package py.com.buybox.trackingSystem.rest.backoffice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import py.com.buybox.trackingSystem.commons.dto.GeneralResponse;
import py.com.buybox.trackingSystem.dto.ReporteResultDto;
import py.com.buybox.trackingSystem.repository.PaqueteEntityRepository;
import py.com.buybox.trackingSystem.security.JwtUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/reporte")
public class ReporteBackOfficeRest {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private PaqueteEntityRepository paqueteEntityRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PreAuthorize("hasRole('GET_REPORTE')")
    @GetMapping()
    public ResponseEntity getReporte(
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

    @PreAuthorize("hasRole('GET_REPORTE')")
    @GetMapping("auth")
    public ResponseEntity getTokenOneTime(){
        GeneralResponse<String, Object> r = (new GeneralResponse<>());
        List<String> permissions = new ArrayList<>();
        permissions.add("GET_REPORTE_PAQUETE");
        r.setBody(jwtUtil.createTokenOneTime(permissions,600));
        return new ResponseEntity<>(r, HttpStatus.OK);
    };


}
