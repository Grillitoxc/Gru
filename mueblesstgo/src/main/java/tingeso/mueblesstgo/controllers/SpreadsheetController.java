package tingeso.mueblesstgo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tingeso.mueblesstgo.entities.EmployeeEntity;
import tingeso.mueblesstgo.services.ClockService;
import tingeso.mueblesstgo.services.EmployeeService;
import tingeso.mueblesstgo.services.UploadService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class SpreadsheetController {
    @Autowired private ClockService clockService;
    @Autowired private UploadService uploadService;
    @Autowired private EmployeeService employeeService;

    @GetMapping("/list_spreadsheets")
    public String spreadsheets(Model model) {
        uploadService.readFile("Data.txt");
        ArrayList<EmployeeEntity> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "spreadsheet";
    }
    @PostMapping("/list_spreadsheets")
    public String getJustifiers(@RequestParam("dateImput") String dateImput, @RequestParam("employeeName") String employeeName, Model model) {
        clockService.setJustifier(dateImput, employeeName);
        return "redirect:/list_spreadsheets";
    }

    @PostMapping("/list_spreadsheets/hours")
    public String getHours(@RequestParam("extraHours") String extraHours, @RequestParam("employeeName") String employeeName, Model model) {
        clockService.setExtraHours(employeeName, extraHours);
        return "redirect:/list_spreadsheets";
    }
}
