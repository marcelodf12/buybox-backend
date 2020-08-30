package py.com.buybox.trackingSystem.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import py.com.buybox.trackingSystem.AppConfig;
import py.com.buybox.trackingSystem.commons.constants.Constants;
import py.com.buybox.trackingSystem.commons.constants.EntitiesValues;
import py.com.buybox.trackingSystem.dto.UsuarioDTO;
import py.com.buybox.trackingSystem.entities.*;
import py.com.buybox.trackingSystem.repository.*;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.*;
import javax.mail.MessagingException;
import javax.transaction.Transactional;


@Service
public class AuthenticationService {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolEntityRepository rolEntityRepository;

    @Autowired
    private UsuarioEntityRepository usuarioEntityRepository;

    @Autowired
    private SegmentoEntityRepository segmentoEntityRepository;

    @Autowired
    private SucursalEntityRepository sucursalEntityRepository;

    @Autowired
    private ClienteEntityRepository clienteEntityRepository;

    @Autowired
    private CasillaEntityRepository casillaEntityRepository;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private SenderMailService senderMailService;

    @Autowired
    private TemplateEngine htmlTemplateEngine;

    @Transactional
    public ClienteEntity registerNewUserAccount(UsuarioDTO accountDto, List<String> roles) throws DataIntegrityViolationException, MessagingException, IOException {

        UsuarioEntity user = UsuarioDTO.newEntity(accountDto);

        Calendar vencimiento = Calendar.getInstance();
        vencimiento.add(Calendar.SECOND, appConfig.registerExpiration*1000);
        user.setLinkFechaVencimiento(vencimiento);

        user.setLinkDeRecuperacion(link(accountDto.getCorreo(), EntitiesValues.PERMISO_CONFIRMAR_REGISTRO));

        user.setActivo(EntitiesValues.USUARIO_CREADO);
        user.setIntentosFallidos(0);
        user.setPass(passwordEncoder.encode(accountDto.getPass()));

        user.setRolList(this.rolEntityRepository.findRolIn(roles));

        usuarioEntityRepository.save(user);

        ClienteEntity cliente = new ClienteEntity();
        cliente.setNombre(user.getNombre());
        cliente.setApellido(user.getApellido());
        cliente.setCorreo(user.getCorreo());
        cliente.setCelular(accountDto.getCelular());
        cliente.setDireccion(accountDto.getDireccion());
        cliente.setRazonSocial(accountDto.getRazonSocial());
        cliente.setRuc(accountDto.getRuc());
        Optional<SegmentoEntity> segmento = segmentoEntityRepository.findById(appConfig.defaultIdSegmento);
        cliente.setSegmento(segmento.isPresent()?segmento.get():null);
        Optional<SucursalEntity> sucursal = sucursalEntityRepository.findById(appConfig.defaultIdSucursal);
        cliente.setSucursal(sucursal.isPresent()?sucursal.get():null);
        cliente.setUsuario(user);

        Locale locale = new Locale("es_ES");
        final Context ctx = new Context(locale);
        ctx.setVariable("nombre", user.getNombre());
        ctx.setVariable("enlace", user.getLinkDeRecuperacion());

        final String htmlContent = this.htmlTemplateEngine.process("confirm-register.html", ctx);

        this.logger.debug(htmlContent);

        senderMailService.sendEmail(appConfig.subjectRegister, htmlContent ,user.getCorreo());

        return clienteEntityRepository.save(cliente);
    }

    public UsuarioEntity confirmarRegistro(String check){
        if(!StringUtils.isEmpty(check)) {
            try{
                UsuarioEntity user = findUsuarioByTokenWithPermission(check,EntitiesValues.PERMISO_CONFIRMAR_REGISTRO);
                user.setLinkDeRecuperacion(null);
                user.setActivo(EntitiesValues.USUARIO_ACTIVO);
                user.setBloqueadoHasta(null);
                user.setIntentosFallidos(0);

                CasillaEntity casilla = new CasillaEntity();
                casillaEntityRepository.save(casilla);
                user.getCliente().setCasilla(appConfig.prefixCasilla + String.valueOf(casilla.getNumeroCasilla()));

                usuarioEntityRepository.save(user);

                return user;
            }catch (Exception e){
                this.logger.error(e);
                return null;
            }

        }
        return null;
    }

    private String link(String email, String rol){
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + (appConfig.registerExpiration*1000)))
                .signWith(SignatureAlgorithm.HS512, appConfig.registerSecret)
                .claim(Constants.JWT_PERMISSION, rol)
                .compact();
    }

    private UsuarioEntity findUsuarioByTokenWithPermission(String token, String permission){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(appConfig.registerSecret)
                    .parseClaimsJws(token)
                    .getBody();
            if(claims!=null){
                String email = claims.getSubject();
                String permissionsStr = claims.get(Constants.JWT_PERMISSION).toString();
                if (permission.compareTo(permissionsStr) == 0) {
                    UsuarioEntity user = usuarioEntityRepository.findByLinkDeRecuperacion(token);
                    if (user != null) {
                        if (!StringUtils.isAnyEmpty(email, user.getCorreo())) {
                            if(email.compareTo(user.getCorreo())==0){
                                return user;
                            }
                        }
                    }
                }
            }
        }catch (Exception e){

        }
        return null;
    }

}
