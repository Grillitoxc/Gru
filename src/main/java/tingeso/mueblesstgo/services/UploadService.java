package tingeso.mueblesstgo.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tingeso.mueblesstgo.repositories.EmployeeRepository;
import tingeso.mueblesstgo.repositories.ClockRepository;
import tingeso.mueblesstgo.entities.ClockEntity;


@Service
public class UploadService {
    @Autowired
    private ClockRepository clockRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ClockService clockService;

    private final Logger logger = LoggerFactory.getLogger(UploadService.class);

    public void saveFile(MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String folder = "mueblesstgo//src//main//resources//static//uploads//";
                Path path = Paths.get(folder + file.getOriginalFilename());
                Files.write(path, bytes);
                logger.info("Archivo guardado.");
            } catch (Exception e) {
                logger.error("Error al guardar el archivo.");
            }
        }
    }

    public void readFile() {
        String path = "mueblesstgo//src//main//resources//static//uploads//";
        File file = new File(path);
        String dateTemp = "";
        String checkInTemp = "";
        String rutTemp = "";
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                ClockEntity clockTemp = new ClockEntity();
                String line = scanner.nextLine();
                dateTemp = line.substring(0, 10);
                checkInTemp = line.substring(11, 16);
                rutTemp = line.substring(17, 29);
                if (clockRepository.findByDateAndEmployee(dateTemp, employeeRepository.findByRut(rutTemp)) == null) {
                    clockTemp.setDate(dateTemp);
                    clockTemp.setCheckInTime(checkInTemp);
                    clockTemp.setEmployee(employeeRepository.findByRut(rutTemp));
                    if (clockService.calculateDiscount(checkInTemp) == 0)
                        clockTemp.setDiscount(0);
                    else if (clockService.calculateDiscount(checkInTemp) == 1)
                        clockTemp.setDiscount(1);
                    else if (clockService.calculateDiscount(checkInTemp) == 3)
                        clockTemp.setDiscount(3);
                    else if (clockService.calculateDiscount(checkInTemp) == 6)
                        clockTemp.setDiscount(6);
                    else
                        clockTemp.setDiscount(15);
                    clockRepository.save(clockTemp);
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("Error al leer el archivo.");
        }
    }
}
