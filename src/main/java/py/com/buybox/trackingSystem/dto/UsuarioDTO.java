package py.com.buybox.trackingSystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import py.com.buybox.trackingSystem.entities.PaqueteEntity;
import py.com.buybox.trackingSystem.entities.UsuarioEntity;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UsuarioDTO {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private Calendar bloqueadoHasta;
    private Integer activo;
    private Integer intentosFallidos;
    private String linkDeRecuperacion;
    private Calendar linkFechaVencimiento;
    private String pass;
    private String ruc;
    private String razonSocial;
    private String direccion;
    private String celular;
    private String casilla;
    private List<String> roles;

    public UsuarioDTO(UsuarioEntity _user){
        if(_user!=null) {
            if (_user.getIdUsuario() != null) {
                this.idUsuario = _user.getIdUsuario();
            }
            if (_user.getCliente() != null ){
                this.celular = _user.getCliente().getCelular();
            }
            this.nombre = _user.getNombre();
            this.apellido = _user.getApellido();
            this.correo = _user.getCorreo();
            this.bloqueadoHasta = _user.getBloqueadoHasta();
            this.activo = _user.getActivo();
            this.intentosFallidos = _user.getIntentosFallidos();
            this.linkDeRecuperacion = _user.getLinkDeRecuperacion();
            this.linkFechaVencimiento = _user.getLinkFechaVencimiento();
            if (_user.getRolList() != null) {
                this.roles = _user.getRolList().stream().map( x -> x.getRol()).collect(Collectors.toList());
            }
        }
    }

    public static UsuarioEntity newEntity(UsuarioDTO _user){
        UsuarioEntity user = new UsuarioEntity();
        if(_user.getIdUsuario()!=null){
            user.setIdUsuario(_user.getIdUsuario());
        }
        user.setNombre(_user.getNombre());
        user.setApellido(_user.getApellido());
        user.setCorreo(_user.getCorreo());
        user.setBloqueadoHasta(_user.getBloqueadoHasta());
        user.setActivo(_user.getActivo());
        user.setIntentosFallidos(_user.getIntentosFallidos());
        user.setLinkDeRecuperacion(_user.getLinkDeRecuperacion());
        user.setLinkFechaVencimiento(_user.getLinkFechaVencimiento());
        return user;
    }

    public static List<UsuarioDTO> listFromEntity(List<UsuarioEntity> usuarioEntityList){
        if(usuarioEntityList!=null){
            List<UsuarioDTO> list = usuarioEntityList.stream().map(UsuarioDTO::new).collect(Collectors.toList());
            return list;
        }
        return null;
    }

}
