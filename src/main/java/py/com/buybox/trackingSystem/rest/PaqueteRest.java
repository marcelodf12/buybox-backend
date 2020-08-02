package py.com.buybox.trackingSystem.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;


@RestController
@RequestMapping("api/v1/paquete")
public class PaqueteRest {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private PaqueteEntityRepository paqueteEntityRepository;

    @GetMapping()
    public GeneralResponse<List<PaqueteDTO>, Pageable> listarPaquetesRest(
            @RequestParam String codigoInterno,
            @RequestParam String codigoExterno,
            @RequestParam String vuelo,
            @RequestParam String casilla,
            @RequestParam String desde,
            @RequestParam String hasta,
            @RequestParam String sucursal,
            @RequestParam Long currentPage,
            @RequestParam Long perPage,
            @RequestParam("sorting") String sortingStr
    ){
        GeneralResponse<List<PaqueteDTO>, Pageable> r = (new GeneralResponse<>());
        List<PaqueteDTO> paquetes = PaqueteDTO.listFromEntity(paqueteEntityRepository.findAll());
        r.setBody(paquetes);
        return r;
    }

}
