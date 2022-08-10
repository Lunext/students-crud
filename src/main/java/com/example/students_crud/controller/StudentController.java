package com.example.students_crud.controller;

import com.example.students_crud.excel.ExcelExporter;
import com.example.students_crud.model.StudentDTO;
import com.example.students_crud.service.PdfService;
import com.example.students_crud.service.StudentService;
import com.example.students_crud.util.WebUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    private final PdfService pdfService;


    public StudentController(final StudentService studentService, PdfService pdfService) {
        this.studentService = studentService;
        this.pdfService = pdfService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("students", studentService.findAll());
        return "student/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("student") final StudentDTO studentDTO) {
        return "student/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("student") @Valid final StudentDTO studentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("matricula") && studentService.matriculaExists(studentDTO.getMatricula())) {
            bindingResult.rejectValue("matricula", "Exists.student.matricula");
        }
        if (!bindingResult.hasFieldErrors("email") && studentService.emailExists(studentDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.student.email");
        }
        if (bindingResult.hasErrors()) {
            return "student/add";
        }
        studentService.create(studentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("student.create.success"));
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("student", studentService.get(id));
        return "student/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("student") @Valid final StudentDTO studentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("matricula") &&
                !studentService.get(id).getMatricula().equalsIgnoreCase(studentDTO.getMatricula()) &&
                studentService.matriculaExists(studentDTO.getMatricula())) {
            bindingResult.rejectValue("matricula", "Exists.student.matricula");
        }
        if (!bindingResult.hasFieldErrors("email") &&
                !studentService.get(id).getEmail().equalsIgnoreCase(studentDTO.getEmail()) &&
                studentService.emailExists(studentDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.student.email");
        }
        if (bindingResult.hasErrors()) {
            return "student/edit";
        }
        studentService.update(id, studentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("student.update.success"));
        return "redirect:/students";
    }


    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        studentService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("student.delete.success"));
        return "redirect:/students";
    }
    @GetMapping ("/importData/excel")
    public String ImportData() throws IOException {


        System.setProperty("webdriver.gecko.driver", "D:\\Gecko\\geckodriver.exe");
        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(120));
        driver.get("http://localhost:8080/students/add");
        FileInputStream fis= new FileInputStream("D:\\Excels\\students.xlsx");
        XSSFWorkbook workbook= new XSSFWorkbook(fis);

        XSSFSheet sheet= workbook.getSheet("list");
        int rowCount= sheet.getLastRowNum();
        int colCount= sheet.getRow(1).getLastCellNum();


        for(int i=1; i<=rowCount; i++) {
            XSSFRow celldata = sheet.getRow(i);
            String name = celldata.getCell(0).getStringCellValue();
            String lastname = celldata.getCell(1).getStringCellValue();
            Long age =(long) celldata.getCell(2).getNumericCellValue();
            String major = celldata.getCell(3).getStringCellValue();
            String matricula = celldata.getCell(4).getStringCellValue();
            String quater = celldata.getCell(5).getStringCellValue();
            String email = celldata.getCell(6).getStringCellValue();
            driver.findElement(By.id("name")).clear();
            driver.findElement(By.id("name")).sendKeys(name);
            driver.findElement(By.id("lastname")).clear();
            driver.findElement(By.id("lastname")).sendKeys(lastname);
            driver.findElement(By.id("age")).clear();
            driver.findElement(By.id("age")).sendKeys(age.toString());
            driver.findElement(By.id("major")).clear();
            driver.findElement(By.id("major")).sendKeys(major);
            driver.findElement(By.id("matricula")).clear();
            driver.findElement(By.id("matricula")).sendKeys(matricula);
            driver.findElement(By.id("quater")).clear();
            driver.findElement(By.id("quater")).sendKeys(quater);
            driver.findElement(By.id("email")).clear();
            driver.findElement(By.id("email")).sendKeys(email);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
            driver.findElement(By.id("save")).click();




        }
        return "redirect:/students/add";
    }
    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter= new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime= dateFormatter.format(new Date());

        String headerKey= "Content-Disposition";
        String headerValue= "attachment; filename=Agendas" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<StudentDTO> listStudents= studentService.findAll();

        ExcelExporter excelExporter= new ExcelExporter(listStudents);
        excelExporter.export(response);
    }

    @GetMapping("/pdf")
    public void downloadPdf(HttpServletResponse response){
        try{
            Path file = Paths.get(pdfService.generatePlacesPdf().getAbsolutePath());
            if (Files.exists(file)){
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename"+ file.getFileName());
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }



}
