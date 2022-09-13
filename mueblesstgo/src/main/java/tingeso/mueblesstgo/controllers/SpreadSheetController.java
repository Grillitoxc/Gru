package tingeso.mueblesstgo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import tingeso.mueblesstgo.services.SalaryCalculatorService;
import tingeso.mueblesstgo.services.SpreadsheetService;

@Controller
public class SpreadSheetController {
    @Autowired private SalaryCalculatorService salaryCalculatorService;

    @GetMapping("/spreadsheet")
    public String spreadsheets() {
        salaryCalculatorService.calculateSalary();
        return "spreadsheet";
    }
}
