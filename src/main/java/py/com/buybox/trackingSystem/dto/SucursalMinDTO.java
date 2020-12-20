package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import py.com.buybox.trackingSystem.entities.SucursalEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SucursalMinDTO {
    private Integer i;
    private String n;
    private Integer iB;
    private Integer lt;
    private Integer ln;
    private String m;
    private String mf;
    private Integer bm;
    private Integer bmf;
    private Integer f;

    public SucursalMinDTO(SucursalEntity _s){
        this.i = _s.getIdSucursal();
        this.n = _s.getNombre();
        if(_s.getBarrio()!=null) this.iB = _s.getBarrio().getIdBarrio();
        this.lt = _s.getLatitud();
        this.ln = _s.getLongitud();
        this.m = _s.getMensajeAlCliente();
        this.mf = _s.getMensajeAlClienteFinal();
        this.bm = _s.getNotificableLlegada();
        this.bmf = _s.getNotificableFinal();
        this.f = _s.getIsFinal();
    }

    public static List<SucursalMinDTO> listFromEntity(List<SucursalEntity> entityList){
        if(entityList!=null){
            List<SucursalMinDTO> list = entityList.stream().map(SucursalMinDTO::new).collect(Collectors.toList());
            return list;
        }
        return null;
    }

    

}
