package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import py.com.buybox.trackingSystem.entities.PaqueteEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PaqueteDTO {
    private Integer idPaquete;
    private String numeroTracking;
    private String vuelo;
    private Integer peso;
    private Integer longitud;
    private Integer ancho;
    private Integer altura;
    private Integer volumen;
    private Integer precio;
    private String descripcion;
    private String autorizadoNombre;
    private String autorizadoDocumento;
    private Long montoTotal;
    private Integer idCliente;
    private String codigoInterno;
    private String codigoExterno;
    private Integer idEstado;

    public PaqueteDTO(PaqueteEntity paqueteEntity){
        this.idPaquete=paqueteEntity.getIdPaquete();
        this.numeroTracking=paqueteEntity.getNumeroTracking();
        this.vuelo=paqueteEntity.getVuelo();
        this.peso=paqueteEntity.getPeso();
        this.longitud=paqueteEntity.getLongitud();
        this.ancho=paqueteEntity.getAncho();
        this.altura=paqueteEntity.getAltura();
        this.volumen=paqueteEntity.getVolumen();
        this.precio=paqueteEntity.getPrecio();
        this.descripcion=paqueteEntity.getDescripcion();
        this.autorizadoNombre=paqueteEntity.getAutorizadoNombre();
        this.autorizadoDocumento=paqueteEntity.getAutorizadoDocumento();
        this.montoTotal=paqueteEntity.getMontoTotal();
        this.idCliente=paqueteEntity.getCliente().getIdCliente();
        this.codigoInterno=paqueteEntity.getCodigoInterno();
        this.codigoExterno=paqueteEntity.getCodigoExterno();
        this.idEstado=paqueteEntity.getEstado().getIdEstado();
    }

    public static List<PaqueteDTO> listFromEntity(List<PaqueteEntity> paqueteEntityList){
        if(paqueteEntityList!=null){
            List<PaqueteDTO> list = paqueteEntityList.stream().map(PaqueteDTO::new).collect(Collectors.toList());
            return list;
        }
        return null;
    }

}
