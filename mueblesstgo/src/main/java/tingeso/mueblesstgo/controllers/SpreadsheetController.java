package tingeso.mueblesstgo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tingeso.mueblesstgo.services.UploadService;

@Controller
public class SpreadsheetController {
    @Autowired
    private UploadService uploadService;

    @GetMapping("/list_spreadsheets")
    public String spreadsheets(Model model) {
        uploadService.readFile("Data.txt");
        return "spreadsheet";
    }

}
