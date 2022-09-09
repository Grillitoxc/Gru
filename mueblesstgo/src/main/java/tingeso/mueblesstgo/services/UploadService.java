package tingeso.mueblesstgo.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class UploadService {
    private String folder = "mueblesstgo//uploads//";
    private final Logger logger = LoggerFactory.getLogger(UploadService.class);

    public String saveFile(MultipartFile file) {
        if (!file.isEmpty()){
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(folder + file.getOriginalFilename());
                Files.write(path, bytes);
                logger.info("Archivo guardado.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "Archivo guardado correctamente.";
    }

    private String path = "mueblesstgo//uploads//Data.txt";
    public String readFile(String filename) {
        File file = new File(path);

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Archivo leido correctamente.";
    }




}
