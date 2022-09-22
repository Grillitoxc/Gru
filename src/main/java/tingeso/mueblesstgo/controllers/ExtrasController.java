package tingeso.mueblesstgo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tingeso.mueblesstgo.entities.EmployeeEntity;
import tingeso.mueblesstgo.services.ClockService;
import tingeso.mueblesstgo.services.EmployeeService;
import tingeso.mueblesstgo.services.UploadService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping
@Controller
public class ExtrasController {
    @Autowired
    private ClockService clockService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/extra_data")
    public String spreadsheets(Model model) {
        uploadService.readFile();
        List<EmployeeEntity> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "extras";
    }

    @PostMapping("/extra_data/justifier")
    public String getJustifiers(@RequestParam("dateImput") String dateImput, @RequestParam("employeeName") String employeeName, RedirectAttributes ms) {
        boolean mensaje = clockService.setJustifier(dateImput, employeeName);
        ms.addFlashAttribute("msgJustifier", mensaje);
        return "redirect:/extra_data";
    }

    @PostMapping("/extra_data/hours")
    public String getHours(@RequestParam("extraHours") String extraHours, @RequestParam("employeeName") String employeeName, RedirectAttributes ms) {
        boolean mensaje = clockService.setExtraHours(employeeName, extraHours);
        ms.addFlashAttribute("msgExtraHour", mensaje);
        return "redirect:/extra_data";
    }
}
