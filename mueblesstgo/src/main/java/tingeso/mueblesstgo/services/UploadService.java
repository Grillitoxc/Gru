package tingeso.mueblesstgo.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class UploadService {
    private String folder = "uploads//";
    private final Logger logger = LoggerFactory.getLogger(UploadService.class);

    public String saveFile(MultipartFile file) {
        if (!file.isEmpty()){
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(folder + file.getOriginalFilename());
                Files.write(path, bytes);
                return "You successfully uploaded " + file.getOriginalFilename() + "!";
            } catch (IOException e) {
                return "You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage();
            }
        }
        return "You successfully uploadedx2";
    }
}
