package com.example.students_crud.excel;

import com.example.students_crud.model.StudentDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<StudentDTO> listStudents;

    public ExcelExporter(List<StudentDTO> listStudents){
        this.listStudents= listStudents;
        workbook= new XSSFWorkbook();
    }
    private void writeHeaderLine(){
        sheet= workbook.createSheet("Students");

        Row row= sheet.createRow(0);

        CellStyle style= workbook.createCellStyle();
        XSSFFont font= workbook.createFont();
        font.setBold(true);
        style.setFillBackgroundColor((short) 44);

        font.setFontHeight(18);
        style.setFont(font);

        createCell (row,0, "ID", style);
        createCell (row,1, "Student Name", style);
        createCell (row, 2, "Student Lastname", style);
        createCell(row, 3, "Student  Age", style);
        createCell(row, 4, "Student Major", style);
        createCell(row, 5, "Student Matricula", style);
        createCell(row, 6, "Student Quater", style);
        createCell(row, 7, "Student Email", style);
        createCell(row, 8, "Exported date", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style){
        sheet.autoSizeColumn(columnCount);
        Cell cell= row.createCell(columnCount);
        if(value instanceof  Integer){
            cell.setCellValue((Integer) value);
        }
        else if(value instanceof  Boolean){
            cell.setCellValue((Boolean) value);
        }
        else{
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
    private void writeDataLines(){
        int rowCount= 1;

        CellStyle style= workbook.createCellStyle();
        XSSFFont font= workbook.createFont();
        font.setFontHeight(16);
        style.setFont(font);
        DateFormat dateFormatter= new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime= dateFormatter.format(new Date());

        for(StudentDTO student: listStudents){
            Row row= sheet.createRow(rowCount++);
            int columnCount=0;


            createCell(row, columnCount++, student.getId(), style);
            createCell(row, columnCount++, student.getName(), style);
            createCell(row, columnCount++, student.getLastname(), style);
            createCell(row, columnCount++, student.getAge().toString(), style);
            createCell(row, columnCount++, student.getMajor(), style);
            createCell(row, columnCount++, student.getMatricula(), style);
            createCell(row, columnCount++, student.getQuater(), style);
            createCell(row, columnCount++, student.getEmail().toString(), style);
            createCell(row, columnCount++, currentDateTime, style);

        }
    }

    public void export(HttpServletResponse response)  throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream= response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }
}
