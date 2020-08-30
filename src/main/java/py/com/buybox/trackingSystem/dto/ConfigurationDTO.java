package py.com.buybox.trackingSystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class ConfigurationDTO {

    List<EstadoDTO> estados;

    List<SucursalMinDTO> sucursales;

    List<CiudadDTO> ciudades;

    List<BarrioDTO> barrios;

    List<DepartamentoDTO> departamentos;

    List<CategoriaDTO> categorias;

    List<EtiquetaDTO> etiquestas;

}
