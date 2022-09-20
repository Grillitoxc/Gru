package tingeso.mueblesstgo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import tingeso.mueblesstgo.services.EmployeeService;
import tingeso.mueblesstgo.services.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping
@Controller
public class IndexController {
    @Autowired
    private UploadService uploadService;
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/insert_data")
    public String insertData() {
        employeeService.fillDatabase();
        return "redirect:/";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("archivos") MultipartFile file, RedirectAttributes message) {
        uploadService.saveFile(file);
        message.addFlashAttribute("message", "Archivo subido correctamente.");
        return "redirect:/";
    }
}
