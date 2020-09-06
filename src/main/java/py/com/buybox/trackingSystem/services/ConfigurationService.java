package py.com.buybox.trackingSystem.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import py.com.buybox.trackingSystem.dto.*;
import py.com.buybox.trackingSystem.repository.*;

@Service
public class ConfigurationService {

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

    @Cacheable("listarConfiguration")
    public ConfigurationDTO listarConfiguracion(){
        ConfigurationDTO c;
        try {
            c = new ConfigurationDTO();
            c.setEstados(EstadoDTO.listFromEntity(estadoEntityRepository.findAll()));
            c.setSucursales(SucursalMinDTO.listFromEntity(sucursalEntityRepository.findAll()));
            c.setCiudades(CiudadDTO.listFromEntity(ciudadEntityRepository.findAll()));
            //c.setBarrios(BarrioDTO.listFromEntity(barrioEntityRepository.findAll()));
            c.setDepartamentos(DepartamentoDTO.listFromEntity(departamentoEntityRepository.findAll()));
            c.setCategorias(CategoriaDTO.listFromEntity(categoriaEntityRepository.findAll()));
            c.setEtiquestas(EtiquetaDTO.listFromEntity(etiquetaEntityRepository.findAll()));
            return c;
        }catch (Exception e){
            this.logger.error(e);
        }
        return null;
    }
}
