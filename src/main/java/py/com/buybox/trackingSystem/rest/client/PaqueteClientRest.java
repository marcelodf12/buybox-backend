package py.com.buybox.trackingSystem.rest.client;

import org.apache.commons.lang3.StringUtils;
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
import py.com.buybox.trackingSystem.dto.GeoDto;
import py.com.buybox.trackingSystem.dto.PaqueteDTO;
import py.com.buybox.trackingSystem.entities.PaqueteEntity;
import py.com.buybox.trackingSystem.repository.PaqueteEntityRepository;
import py.com.buybox.trackingSystem.security.JwtUtil;
import py.com.buybox.trackingSystem.services.PaqueteService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/v1/client/paquete")
public class PaqueteClientRest {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private PaqueteEntityRepository paqueteEntityRepository;

    @Autowired
    private PaqueteService paqueteService;

    @Autowired
    private JwtUtil jwtUtil;

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping()
    public ResponseEntity listarPaquetesRest(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") Integer currentPage
    ){
        GeneralResponse<List<PaqueteDTO>, Paginable> r = (new GeneralResponse<>());
        Page<PaqueteEntity> pagePaquete = null;
        try {
            String casilla = jwtUtil.getClaim(token, "casilla");
            logger.debug(casilla);
            if (!StringUtils.isEmpty(casilla)) {
                pagePaquete = paqueteEntityRepository.findPaqueteByCasilla(
                        casilla,
                        PageRequest.of(currentPage, 10, Sort.by(SortUtil.sortingList("-ingreso", "paquetes"))));
            }
        }catch (Exception e){
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(pagePaquete!=null && !pagePaquete.isEmpty()){
            List<PaqueteDTO> paquetes = PaqueteDTO.listFromEntity(pagePaquete.toList());
            r.setBody(paquetes);
            r.setMeta(new Paginable(pagePaquete));
        }else{
            r.setBody(new ArrayList<>());
            r.setHeader(HeadersCodes.CLIENTE_SIN_PAQUETES, true, Constants.LEVEL_WARN, Constants.TYPE_TOAST);
            return new ResponseEntity<>(r, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("{id}")
    public ResponseEntity rastreoPaquete(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Integer idPaquete
    ){
        GeneralResponse<PaqueteDTO, Paginable> r = (new GeneralResponse<>());
        Optional<PaqueteEntity> paquete;
        PaqueteDTO paqueteDTO = null;
        try {
            String casilla = jwtUtil.getClaim(token, "casilla");
            logger.debug(casilla);
            if (!StringUtils.isEmpty(casilla)) {
                paquete = paqueteEntityRepository.findById(idPaquete);
                if(paquete.get()!=null){
                    paqueteDTO = new PaqueteDTO(paquete.get(), paquete.get().getRastreoList().stream().filter(
                            rastreoEntity -> (rastreoEntity.getSucursal().getRastreable() == 1)
                            ).collect(Collectors.toList())
                    );
                    if(casilla.compareTo(paqueteDTO.getCasilla())!=0){
                        paqueteDTO = null;
                    }
                }
            }
        }catch (Exception e){
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(paqueteDTO!=null){
            r.setBody(paqueteDTO);
        }else{
            r.setHeader(HeadersCodes.ENTITY_NOT_EXIST, true, Constants.LEVEL_WARN, Constants.TYPE_TOAST);
            return new ResponseEntity<>(r, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("{id}/delivery")
    public ResponseEntity solicitarDelivery(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Integer idPaquete,
            @RequestBody GeoDto geo
            ){
        GeneralResponse<PaqueteDTO, Paginable> r = (new GeneralResponse<>());
        PaqueteEntity paquete = null;
        try {
            String casilla = jwtUtil.getClaim(token, "casilla");
            logger.debug(casilla);
            if (!StringUtils.isEmpty(casilla)) {
                paquete = paqueteService.delivery(geo, idPaquete, casilla);
            }
        }catch (Exception e){
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(paquete!=null){
            r.setHeader(HeadersCodes.DELIVERY_REQUEST_SUCCESS, true, Constants.LEVEL_SUCCESS, Constants.TYPE_TOAST);
            r.setBody(new PaqueteDTO(paquete));
        }else{
            r.setHeader(HeadersCodes.ENTITY_NOT_EXIST, true, Constants.LEVEL_WARN, Constants.TYPE_TOAST);
            return new ResponseEntity<>(r, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("{id}/autorizar")
    public ResponseEntity solicitarDelivery(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Integer idPaquete,
            @RequestBody PaqueteDTO paqueteDTO
    ){
        GeneralResponse<PaqueteDTO, Paginable> r = (new GeneralResponse<>());
        PaqueteEntity paquete = null;
        try {
            String casilla = jwtUtil.getClaim(token, "casilla");
            logger.debug(casilla);
            if (!StringUtils.isEmpty(casilla)) {
                paquete = paqueteService.autorizar(paqueteDTO, idPaquete, casilla);
            }
        }catch (Exception e){
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(paquete!=null){
            r.setHeader(HeadersCodes.AUTORIZADO_SUCCESS, true, Constants.LEVEL_SUCCESS, Constants.TYPE_TOAST);
            r.setBody(new PaqueteDTO(paquete));
        }else{
            r.setHeader(HeadersCodes.ENTITY_NOT_EXIST, true, Constants.LEVEL_WARN, Constants.TYPE_TOAST);
            return new ResponseEntity<>(r, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

}
