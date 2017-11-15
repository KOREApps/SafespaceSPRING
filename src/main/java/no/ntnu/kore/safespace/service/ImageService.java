package no.ntnu.kore.safespace.service;

import no.ntnu.kore.safespace.entity.Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.Base64;

@Service
public class ImageService {

    @Value("${image.dir}")
    private String path;


    public void saveToDisk(Image image, byte[] data) throws IOException {
        Path file = getFilePath(image);
        Files.write(file, data);
    }

    public void saveB64ToDisk(Image image, String b64String) throws IOException {
        String[] strings = b64String.split(",");
        byte[] data = null;
        if (strings.length == 1) {
            data = Base64.getDecoder().decode(strings[0]);
        } else {
            data = Base64.getDecoder().decode(strings[1]);
        }
        saveToDisk(image, data);
    }

    public byte[] getDataFromDisk(Image image) throws IOException {
        Path file = getFilePath(image);
        return Files.readAllBytes(file);
    }

    public String getB64FromDisk(Image image) throws IOException {
        byte[] data = getDataFromDisk(image);
        return Base64.getEncoder().encodeToString(data);
    }

    private Path getFilePath(Image image){
        return Paths.get("/", getFilePathString(image).split("/"));
    }

    private String getFilePathString(Image image){
        return path + getImageFileName(image);
    }

    private String getImageFileName(Image image) {
        return image.getName() + "." + image.getFileExtension();
    }

}
