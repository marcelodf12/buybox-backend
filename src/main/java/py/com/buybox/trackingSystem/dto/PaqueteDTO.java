package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import py.com.buybox.trackingSystem.entities.PaqueteEntity;
import py.com.buybox.trackingSystem.entities.RastreoEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
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
    private String clienteNombreApellido;
    private String codigoInterno;
    private String codigoExterno;
    private Integer idEstado;
    private LocalDate ingreso;
    private String destino;
    private Integer idSucursalDestino;
    private String sucursalActual;
    private Integer idSucursalActual;
    private String casilla;
    private Integer updated;
    private List<RastreoDTO> rastreo;
    private Integer delivery;
    private BigDecimal lng;
    private BigDecimal lat;

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
        this.codigoInterno=paqueteEntity.getCodigoInterno();
        this.codigoExterno=paqueteEntity.getCodigoExterno();
        this.ingreso=paqueteEntity.getIngreso();
        this.lat=paqueteEntity.getLat();
        this.lng=paqueteEntity.getLng();
        if(paqueteEntity.getCliente()!=null) {
            this.idCliente = paqueteEntity.getCliente().getIdCliente();
            this.clienteNombreApellido=paqueteEntity.getCliente().getApellido() + ", " + paqueteEntity.getCliente().getNombre();
            this.casilla=paqueteEntity.getCliente().getCasilla();
        }
        if(paqueteEntity.getEstado()!=null){
            this.idEstado=paqueteEntity.getEstado().getIdEstado();
        }
        if(paqueteEntity.getSucursalDestino()!=null){
            this.destino=paqueteEntity.getSucursalDestino().getNombre();
            this.idSucursalDestino=paqueteEntity.getSucursalDestino().getIdSucursal();
        }
        if(paqueteEntity.getSucursalActual()!=null){
            this.sucursalActual=paqueteEntity.getSucursalActual().getNombre();
            this.idSucursalActual=paqueteEntity.getSucursalActual().getIdSucursal();
            this.delivery = -1;
            if(paqueteEntity.getSucursalActual().getIsDelivery() == 1 ){
                if(paqueteEntity.getSucursalActual().getIdSucursal() == paqueteEntity.getCliente().getSucursal().getIdSucursal()){
                    if(paqueteEntity.getLng() == null || paqueteEntity.getLat() == null){
                        this.delivery = 0;
                    }else{
                        this.delivery = 1;
                    }

                }
            }
        }
        // Esto en un futuro hay que refactorizar
        if("retirado".compareTo(paqueteEntity.getEstado().getEstado())==0){
            this.delivery = -2;
        }
    }

    public PaqueteDTO(PaqueteEntity paqueteEntity, List<RastreoEntity> rastreoEntityList){
        this(paqueteEntity);
        this.rastreo = new ArrayList<>();
        for(RastreoEntity r: rastreoEntityList){
            this.rastreo.add(new RastreoDTO(r));
        }
    }

    public static List<PaqueteDTO> listFromEntity(List<PaqueteEntity> paqueteEntityList){
        if(paqueteEntityList!=null){
            List<PaqueteDTO> list = paqueteEntityList.stream().map(PaqueteDTO::new).collect(Collectors.toList());
            return list;
        }
        return null;
    }

}
