package py.com.buybox.trackingSystem.rest;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import py.com.buybox.trackingSystem.commons.dto.GeneralResponse;
import py.com.buybox.trackingSystem.commons.dto.Pageable;
import py.com.buybox.trackingSystem.dto.PaqueteDTO;
import py.com.buybox.trackingSystem.entities.PaqueteEntity;
import py.com.buybox.trackingSystem.repository.PaqueteEntityRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("api/v1/paquete")
public class PaqueteRest {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private PaqueteEntityRepository paqueteEntityRepository;

    @GetMapping()
    public GeneralResponse<List<PaqueteDTO>, Pageable> listarPaquetesRest(
            @RequestParam(defaultValue = "") String codigoInterno,
            @RequestParam(defaultValue = "") String codigoExterno,
            @RequestParam(defaultValue = "") String vuelo,
            @RequestParam(defaultValue = "") String casilla,
            @RequestParam(defaultValue = "1999-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") Date desde,
            @RequestParam(defaultValue = "2050-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") Date hasta,
            @RequestParam Integer idSucursal,
            @RequestParam Long currentPage,
            @RequestParam Long perPage,
            @RequestParam("sorting") String sortingStr
    ){
        GeneralResponse<List<PaqueteDTO>, Pageable> r = (new GeneralResponse<>());
        List<PaqueteDTO> paquetes = PaqueteDTO.listFromEntity(paqueteEntityRepository.findPaquete(
                codigoExterno,
                codigoInterno,
                vuelo,
                casilla,
                idSucursal,
                DateUtils.toCalendar(desde),
                DateUtils.toCalendar(hasta)
        ));
        r.setBody(paquetes);
        return r;
    }

}
