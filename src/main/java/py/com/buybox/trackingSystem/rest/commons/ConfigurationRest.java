package py.com.buybox.trackingSystem.rest.commons;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import py.com.buybox.trackingSystem.commons.constants.HeadersCodes;
import py.com.buybox.trackingSystem.commons.dto.GeneralResponse;
import py.com.buybox.trackingSystem.dto.*;
import py.com.buybox.trackingSystem.repository.*;

@RestController
@RequestMapping("api/v1/commons")
public class ConfigurationRest {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private EstadoEntityRepository estadoEntityRepository;

    @Autowired
    private SucursalEntityRepository sucursalEntityRepository;

    @Autowired
    private CiudadEntityRepository ciudadEntityRepository;

    @Autowired
    private BarrioEntityRepository barrioEntityRepository;

    @Autowired
    private DepartamentoEntityRepository departamentoEntityRepository;

    @Autowired
    private CategoriaEntityRepository categoriaEntityRepository;

    @Autowired
    private EtiquetaEntityRepository etiquetaEntityRepository;

    @GetMapping()
    @Cacheable("listarConfiguration")
    public ResponseEntity listarConfiguracion(){
        GeneralResponse<ConfigurationDTO, Object> r = (new GeneralResponse<>());
        try {
            ConfigurationDTO c = new ConfigurationDTO();
            c.setEstados(EstadoDTO.listFromEntity(estadoEntityRepository.findAll()));
            c.setSucursales(SucursalMinDTO.listFromEntity(sucursalEntityRepository.findAll()));
            c.setCiudades(CiudadDTO.listFromEntity(ciudadEntityRepository.findAll()));
            c.setBarrios(BarrioDTO.listFromEntity(barrioEntityRepository.findAll()));
            c.setDepartamentos(DepartamentoDTO.listFromEntity(departamentoEntityRepository.findAll()));
            c.setCategorias(CategoriaDTO.listFromEntity(categoriaEntityRepository.findAll()));
            c.setEtiquestas(EtiquetaDTO.listFromEntity(etiquetaEntityRepository.findAll()));
            r.setBody(c);
            r.setHeader(HeadersCodes.GENERAL_SUCCESS,false,"","");
        }catch (Exception e){
            return new ResponseEntity<>(new GeneralResponse<>(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(r, HttpStatus.OK);
    }
}
