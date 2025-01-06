package org.example.Controller;

import org.example.Service.ExcelDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/excel")
public class ExcelImportController {
    @Autowired
    private ExcelDataService excelDataService;

    @PostMapping("/import")
    public String importExcel(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return "Please upload a valid Excel file.";
        }
        try {
            excelDataService.importExcelData(file);
            return "Excel data has been imported successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to import Excel data.";
        }
    }
}