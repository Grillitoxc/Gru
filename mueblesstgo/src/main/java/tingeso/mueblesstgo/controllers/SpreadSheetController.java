package tingeso.mueblesstgo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tingeso.mueblesstgo.entities.EmployeeEntity;
import tingeso.mueblesstgo.services.EmployeeService;
import tingeso.mueblesstgo.services.SalaryCalculatorService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SpreadSheetController {
    @Autowired
    private SalaryCalculatorService salaryCalculatorService;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/spreadsheet")
    public String spreadsheets(Model model) {
        salaryCalculatorService.calculateSalary();
        List<EmployeeEntity> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "spreadsheet";
    }
}
