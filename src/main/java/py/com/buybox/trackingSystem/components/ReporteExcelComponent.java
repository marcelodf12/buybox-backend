package py.com.buybox.trackingSystem.components;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import py.com.buybox.trackingSystem.dto.ReporteResultDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component("reportePaquetesExcel")
public class ReporteExcelComponent extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        @SuppressWarnings("unchecked")
        List<ReporteResultDto> reporte = (List<ReporteResultDto>) model.get("reporte");
        String filename = (String) model.get("filename");

        // change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\""+filename+".xls\"");

        // create excel xls sheet
        Sheet sheet = workbook.createSheet("Resumen");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFPalette palette = hwb.getCustomPalette();
        HSSFColor redColor = palette.findSimilarColor(100, 20, 18);
        HSSFColor whiteColor = palette.findSimilarColor(255, 255, 255);
        style.setFillForegroundColor(redColor.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        font.setBold(true);
        font.setColor(whiteColor.getIndex());
        style.setFont(font);


        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Fecha");
        header.getCell(0).setCellStyle(style);
        header.createCell(1).setCellValue("Estado");
        header.getCell(1).setCellStyle(style);
        header.createCell(2).setCellValue("Segmento");
        header.getCell(2).setCellStyle(style);
        header.createCell(3).setCellValue("Peso (Kg)");
        header.getCell(3).setCellStyle(style);
        header.createCell(4).setCellValue("Precio Paquete (USD)");
        header.getCell(4).setCellStyle(style);
        header.createCell(5).setCellValue("Volumen (m3)");
        header.getCell(5).setCellStyle(style);
        header.createCell(6).setCellValue("Monto Total (USD)");
        header.getCell(6).setCellStyle(style);

        int rowCount = 1;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);


        for(ReporteResultDto row : reporte){
            Row userRow =  sheet.createRow(rowCount++);
            userRow.createCell(0).setCellValue(row.getFecha().format(formatter));
            userRow.createCell(1).setCellValue(row.getEstado());
            userRow.createCell(2).setCellValue(row.getSegmento());
            userRow.createCell(3).setCellValue(row.getPeso().doubleValue());
            userRow.createCell(4).setCellValue(row.getPrecio().doubleValue());
            userRow.createCell(5).setCellValue(row.getVolumen().doubleValue());
            userRow.createCell(6).setCellValue(row.getMontoTotal().doubleValue());
        }

    }
}
