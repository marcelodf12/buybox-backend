package py.com.buybox.trackingSystem.rest.backoffice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import py.com.buybox.trackingSystem.AppConfig;
import py.com.buybox.trackingSystem.commons.constants.Constants;
import py.com.buybox.trackingSystem.dto.ReporteResultDto;
import py.com.buybox.trackingSystem.repository.PaqueteEntityRepository;
import py.com.buybox.trackingSystem.security.JwtUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("public/downloads/")
public class ExcelBackOfficeCtrl {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private PaqueteEntityRepository paqueteEntityRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("reporte/paquetes/resumen.xls")
    public String handleXLSReport(
            Model model,
            @RequestParam(defaultValue = "1999-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desde,
            @RequestParam(defaultValue = "2050-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate hasta,
            @RequestParam(defaultValue = "") String token
    ) {
        String permissions = jwtUtil.getClaim(token, Constants.JWT_PERMISSION);
        if(permissions != null) {
            this.logger.debug(permissions);
            boolean hasPermission = permissions.indexOf("GET_REPORTE_PAQUETE") >= 0;
            this.logger.debug(permissions.indexOf("GET_REPORTE_PAQUETE"));
            if(hasPermission){
                List<ReporteResultDto> listPaquete = null;
                listPaquete = paqueteEntityRepository.reporte(desde, hasta);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                model.addAttribute("reporte", listPaquete);
                model.addAttribute("filename", "Resumen " + desde.format(formatter) + " al " + hasta.format(formatter));
                return "reportePaquetesExcel";
            }
        }
        return "error404";
    }
}
