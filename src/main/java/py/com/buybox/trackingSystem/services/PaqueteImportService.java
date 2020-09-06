package py.com.buybox.trackingSystem.services;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import py.com.buybox.trackingSystem.AppConfig;
import py.com.buybox.trackingSystem.dto.PaqueteDTO;
import py.com.buybox.trackingSystem.dto.PaqueteImportDto;
import py.com.buybox.trackingSystem.entities.ClienteEntity;
import py.com.buybox.trackingSystem.entities.PaqueteEntity;
import py.com.buybox.trackingSystem.entities.SegmentoEntity;
import py.com.buybox.trackingSystem.entities.SucursalEntity;
import py.com.buybox.trackingSystem.repository.ClienteEntityRepository;
import py.com.buybox.trackingSystem.repository.PaqueteEntityRepository;
import py.com.buybox.trackingSystem.repository.SegmentoEntityRepository;
import py.com.buybox.trackingSystem.repository.SucursalEntityRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class PaqueteImportService {

    @Autowired
    private PaqueteEntityRepository paqueteEntityRepository;

    @Autowired
    private ClienteEntityRepository clienteEntityRepository;

    @Autowired
    private SegmentoEntityRepository segmentoEntityRepository;

    @Autowired
    private SucursalEntityRepository sucursalEntityRepository;

    @Autowired
    private AppConfig appConfig;

    protected final Log logger = LogFactory.getLog(this.getClass());

    public List<PaqueteImportDto> preImport(MultipartFile file) {
        List<PaqueteImportDto> lista = new ArrayList<>();
        try {
            Optional<SegmentoEntity> segmentoDefault = segmentoEntityRepository.findById(appConfig.defaultIdSegmento);
            Optional<SucursalEntity> sucursalDestinoDefault = sucursalEntityRepository.findById(appConfig.defaultIdSucursalDestino);
            Optional<SucursalEntity> sucursalOrigenDefault = sucursalEntityRepository.findById(appConfig.defaultIdSucursalOrigen);
            FileInputStream f = (FileInputStream) file.getInputStream();
            XSSFWorkbook libro = new XSSFWorkbook(f);
            XSSFSheet hoja = libro.getSheet("Parcel Cube");
            if(hoja!=null) {
                this.logger.debug("Hoja leida");
                Iterator<Row> rowIterator = hoja.rowIterator();
                int fila = 0;
                if (rowIterator != null) {
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();
                        fila++;
                        if (fila >= 8) {
                            PaqueteImportDto p = procesarLinea(row, fila, segmentoDefault.get(), sucursalDestinoDefault.get(), sucursalOrigenDefault.get());
                            if(p!=null)
                                lista.add(p);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
        return lista;
    }

    private PaqueteImportDto procesarLinea(Row row, int fila, SegmentoEntity segmentoDefault, SucursalEntity sucursalDestinoDefault, SucursalEntity sucursalOrigenDefault){
        this.logger.debug(" <--------------- Procesando fila="+fila+" ---------------> ");
        PaqueteImportDto paqueteLinea = new PaqueteImportDto();
        PaqueteEntity p = new PaqueteEntity();
        try {
            int columna = 0;
            Iterator<Cell> cellIterator = row.cellIterator();
            if (cellIterator != null) {
                String agente = "", casilla = "";
                while (cellIterator.hasNext()) {
                    columna++;
                    Cell cell = cellIterator.next();
                    this.logger.debug("(" + fila + "," + columna + ")=" + cell.toString());
                    if (cell.toString() != null && !cell.toString().isEmpty())
                        switch (columna) {
                            case 1:
                                paqueteLinea.setOrden(cell.getRowIndex()+1);
                            case 2:
                                p.setIngreso(LocalDate.now());
                                break;
                            case 3:
                                p.setLongitud((int) Math.floor(Double.parseDouble(cell.toString())));
                                break;
                            case 4:
                                p.setAncho((int) Math.floor(Double.parseDouble(cell.toString())));
                                break;
                            case 5:
                                p.setAltura((int) Math.floor(Double.parseDouble(cell.toString())));
                                break;
                            case 6:
                                p.setVolumen((int) Math.floor(cell.getNumericCellValue() * 1000000));
                                break;
                            case 7:
                                p.setPeso((int) Math.floor(cell.getNumericCellValue() * 1000));
                                break;
                            case 9:
                                p.setVuelo(cell.getStringCellValue());
                                break;
                            case 10:
                                agente = cell.getStringCellValue().trim();
                                break;
                            case 11:
                                casilla = cell.getStringCellValue().trim();
                                break;
                            case 12:
                                p.setDescripcion(cell.getStringCellValue());
                                break;
                            case 13:
                                p.setPrecio((int) Math.floor(Double.parseDouble(cell.toString())));
                                break;
                            case 14:
                                p.setNumeroTracking(cell.getStringCellValue().toUpperCase());
                                break;
                        }
                }
                if (p.getAltura() != null || p.getAncho() != null || p.getLongitud() != null || !agente.isEmpty() || !casilla.isEmpty()) {
                    p.setCodigoExterno(agente + casilla);
                    PaqueteEntity pAnt = paqueteEntityRepository.findByNumeroTracking(p.getNumeroTracking());
                    ClienteEntity cliente = clienteEntityRepository.findByCasilla(p.getCodigoExterno());
                    this.setearPrecio(p, pAnt, cliente, segmentoDefault);
                    p.setCliente(cliente);
                    if (pAnt != null) {
                        pAnt.setCliente(cliente);
                        pAnt.setIngreso(p.getIngreso());
                        pAnt.setLongitud(p.getLongitud());
                        pAnt.setAncho(p.getAncho());
                        pAnt.setAltura(p.getAltura());
                        pAnt.setVolumen(p.getVolumen());
                        pAnt.setPeso(p.getPeso());
                        pAnt.setVuelo(p.getVuelo());
                        pAnt.setCodigoExterno(p.getCodigoExterno());
                        pAnt.setDescripcion(p.getDescripcion());
                        pAnt.setPrecio(p.getPrecio());
                        if(pAnt.getCliente()!=null && cliente!=null && cliente.getIdCliente()!=pAnt.getCliente().getIdCliente()){
                            pAnt.setAutorizadoDocumento(null);
                            pAnt.setAutorizadoNombre(null);
                        }
                        p.setIngreso(pAnt.getIngreso());
                        paqueteEntityRepository.save(pAnt);
                        paqueteLinea.setP(new PaqueteDTO(pAnt));
                        paqueteLinea.setResult(1);
                    } else {
                        p.setSucursalActual(sucursalOrigenDefault);
                        if (cliente!=null && cliente.getSucursal()!=null ){
                            p.setSucursalDestino(cliente.getSucursal());
                        }else{
                            p.setSucursalDestino(sucursalDestinoDefault);
                        }
                        p.setEstado(p.getSucursalActual().getEstadoDefecto());
                        paqueteEntityRepository.save(p);
                        paqueteLinea.setP(new PaqueteDTO(p));
                        paqueteLinea.setResult(0);
                    }


                }else{
                    return null;
                }
            }
        }catch (Exception e){
            this.logger.error("Error al procesar fila="+fila, e);
            paqueteLinea.setResult(-1);
        }
        return paqueteLinea;
    }

    private void setearPrecio(PaqueteEntity p, PaqueteEntity pAnt, ClienteEntity cliente, SegmentoEntity segmentoDefault){
        String formula;
        if(cliente!=null && cliente.getSegmento()!= null && cliente.getSegmento().getPrecio()!=null){
            formula = cliente.getSegmento().getPrecio().getFormula();
        }else{
            formula = segmentoDefault.getPrecio().getFormula();
        }
        String formulas[] = formula.split("\\|");
        for(String expresion: formulas){
            String v[] = expresion.split("=");
            Integer limite = v[0].compareTo("MAX")==0?Integer.MAX_VALUE:Integer.parseInt(v[0]);
            this.logger.debug(p.getPeso()+"<="+limite+"="+(p.getPeso()<=limite));
            if(p.getPeso()<=limite){
                this.logger.debug("Formula aplicada = " + v[1]);
                Expression e = new ExpressionBuilder(v[1])
                        .variables("p", "x", "y", "v", "d", "z")
                        .build()
                        .setVariable("p", (double)p.getPeso()/1000)
                        .setVariable("x", p.getAncho())
                        .setVariable("y", p.getAltura())
                        .setVariable("v", p.getVolumen())
                        .setVariable("d", p.getPrecio())
                        .setVariable("z", p.getLongitud());
                double result = e.evaluate();
                p.setMontoTotal((long) Math.floor(result*100));
                this.logger.debug("p="+ (double)p.getPeso()/1000);
                this.logger.debug("x="+ p.getAncho());
                this.logger.debug("y="+ p.getAltura());
                this.logger.debug("v="+ p.getVolumen());
                this.logger.debug("d="+ p.getPrecio());
                this.logger.debug("z="+ p.getLongitud());
                this.logger.debug(v[1] + "=" + result);
                break;
            }
        }
    }
}
